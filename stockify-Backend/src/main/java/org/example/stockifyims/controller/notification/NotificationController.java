package org.example.stockifyims.controller.notification;

import org.example.stockifyims.entity.NotificationVo;
import org.example.stockifyims.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/{username}")
    public List<NotificationVo> getNotifications(@PathVariable String username) {
        return notificationRepository.findByUsernameOrderByIdDesc(username);
    }

    @DeleteMapping("/{username}")
    @Transactional
    public void deleteNotifications(@PathVariable String username) {
        notificationRepository.deleteByUsername(username);
    }
}
