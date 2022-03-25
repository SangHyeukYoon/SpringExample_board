package com.yoon.example.springboot.service.user;

import com.yoon.example.springboot.domain.user.User;
import com.yoon.example.springboot.domain.user.UserRepository;
import com.yoon.example.springboot.web.dto.users.UserSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));
    }

    @Transactional
    public Long registerUser(UserSaveRequestDto requestDto) throws ResponseStatusException {
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 존재하는 사용자입니다.");
        }

        User newUser = User.builder()
                .username(requestDto.getUsername())
                .password(requestDto.getPassword())
                .build();

        return userRepository.save(newUser).getId();
    }
}
