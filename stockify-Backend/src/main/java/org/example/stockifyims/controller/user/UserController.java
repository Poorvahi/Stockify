package org.example.stockifyims.controller.user;

import org.example.stockifyims.entity.UserVo;
import org.example.stockifyims.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<String> getAllUsernames() {
        return userRepository.findAll().stream()
                .map(UserVo::getUsername)
                .collect(Collectors.toList());
    }
}
