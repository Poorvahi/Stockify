package org.example.stockifyims.repository.comment;

import org.example.stockifyims.entity.CommentVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentVo, Long> {

    List<CommentVo> findByModuleTypeAndModuleIdOrderByIdAsc(String moduleType, long moduleId);
}
