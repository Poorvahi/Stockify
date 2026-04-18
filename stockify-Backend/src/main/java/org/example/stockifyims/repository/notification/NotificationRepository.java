package org.example.stockifyims.repository.notification;

import org.example.stockifyims.entity.NotificationVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationVo, Long> {
    List<NotificationVo> findByUsernameOrderByIdDesc(String username);
    void deleteByUsername(String username);
}
