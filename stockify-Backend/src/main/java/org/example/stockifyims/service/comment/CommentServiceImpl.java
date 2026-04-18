package org.example.stockifyims.service.comment;

import org.example.stockifyims.entity.CommentVo;
import org.example.stockifyims.repository.comment.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public CommentVo saveComment(CommentVo commentVo) {
        return commentRepository.save(commentVo);
    }

    @Override
    public List<CommentVo> getComment(String moduleType, long moduleId) {
        return commentRepository.findByModuleTypeAndModuleIdOrderByIdAsc(moduleType, moduleId);
    }
}