package org.example.stockifyims.controller.websocket;

import org.example.stockifyims.entity.CommentVo;
import org.example.stockifyims.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{moduleType}/{moduleId}")
    public List<CommentVo> getComments(@PathVariable String moduleType, @PathVariable long moduleId) {
        return commentService.getComment(moduleType, moduleId);
    }

}
