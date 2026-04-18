package org.example.stockifyims.controller.websocket;

import org.example.stockifyims.entity.CommentVo;
import org.example.stockifyims.repository.user.UserRepository;
import org.example.stockifyims.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/comments")
public class WebSocketController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private org.example.stockifyims.repository.notification.NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/comment")
    public void sendComment(CommentVo commentVo) {
        CommentVo saved = commentService.saveComment(commentVo);

        String destination = "/topic/comments/"
                + commentVo.getModuleType() + "/"
                + commentVo.getModuleId();
        simpMessagingTemplate.convertAndSend(destination, saved);

        // Parse @mentions and notify only existing users.
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("@([A-Za-z0-9_]+)").matcher(commentVo.getContent());
        java.util.Set<String> sentMentions = new java.util.HashSet<>();
        while (matcher.find()) {
            String mentionedUser = matcher.group(1);
            if (sentMentions.contains(mentionedUser) || userRepository.findByUsername(mentionedUser).isEmpty()) {
                continue;
            }
            sentMentions.add(mentionedUser);
            String message = commentVo.getUsername() + " mentioned you in a comment!";

            org.example.stockifyims.entity.NotificationVo notification = new org.example.stockifyims.entity.NotificationVo();
            notification.setUsername(mentionedUser);
            notification.setMessage(message);
            notificationRepository.save(notification);

            simpMessagingTemplate.convertAndSend("/topic/notifications/" + mentionedUser, message);
        }
    }
}
