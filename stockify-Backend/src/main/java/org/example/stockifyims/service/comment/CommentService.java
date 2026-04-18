package org.example.stockifyims.service.comment;

import org.example.stockifyims.entity.CommentVo;

import java.util.List;


public interface CommentService {

    CommentVo saveComment(CommentVo commentVo);

    List<CommentVo> getComment(String moduleType, long moduleId);


}

