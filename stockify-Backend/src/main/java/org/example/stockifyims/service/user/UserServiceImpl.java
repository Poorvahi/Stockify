package org.example.stockifyims.service.user;

import org.example.stockifyims.entity.UserVo;
import org.example.stockifyims.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserVo user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String role = user.getRole();
        if (role != null && !role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(role)
                .build();

    }

    public void saveUser(UserVo userVo) {
        userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));
        userRepository.save(userVo);
    }

}
