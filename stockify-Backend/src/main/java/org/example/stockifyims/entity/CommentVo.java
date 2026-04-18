package org.example.stockifyims.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name="comments")
@Entity
public class CommentVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String  content;

    @Column(name = "module_id", nullable = false)
    private  long moduleId;

    @Column(name = "module_type", nullable = false)
    private String moduleType ;

    @Column(name = "username")
    private String username;

    @Column(name = "created_at")
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
