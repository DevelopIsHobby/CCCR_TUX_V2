package com.chattest.service;

import com.chattest.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.chattest.entity.User;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
//@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("UserDetailService loadUserByUsername {}", email);

        User user = userRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("Check Email"));

        log.info("===============================");
        log.info(user);
        log.info("User roles : {}", user.getAuthorities());

        return user; // User 엔티티가 UserDetails를 구현하고 있어야 합니다.

    }

}
