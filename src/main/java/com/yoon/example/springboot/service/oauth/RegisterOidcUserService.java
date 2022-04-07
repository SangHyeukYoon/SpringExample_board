package com.yoon.example.springboot.service.oauth;

import com.yoon.example.springboot.domain.user.Role;
import com.yoon.example.springboot.domain.user.User;
import com.yoon.example.springboot.domain.user.UserRepository;
import com.yoon.example.springboot.service.oauth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class RegisterOidcUserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Transactional
    public Long registerUser(String nickname, OidcUser principal) {
        User user = User.builder()
                .name(principal.getName())
                .email(principal.getEmail())
                .picture(principal.getPicture())
                .nickname(nickname)
                .role(Role.USER)
                .build();

        Authentication oldAuth = SecurityContextHolder.getContext().getAuthentication();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                oldAuth.getName(), oldAuth.getCredentials(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        httpSession.setAttribute("user", new SessionUser(user));

        return userRepository.save(user).getId();
    }

}
