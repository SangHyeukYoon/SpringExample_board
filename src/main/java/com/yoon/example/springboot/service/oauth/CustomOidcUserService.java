package com.yoon.example.springboot.service.oauth;

import com.yoon.example.springboot.domain.user.User;
import com.yoon.example.springboot.domain.user.UserRepository;
import com.yoon.example.springboot.service.oauth.dto.OAuthAttributes;
import com.yoon.example.springboot.service.oauth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OidcUserRequest, OidcUser> delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes
                .of(registrationId, userNameAttributeName, oidcUser.getAttributes());

        User user;

        if (userRepository.findByEmail(attributes.getEmail()).isEmpty()) {
            user = attributes.toEntity();
        } else {
            user = saveOrUpdate(attributes);
            httpSession.setAttribute("user", new SessionUser(user));
        }

        return new DefaultOidcUser(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                userRequest.getIdToken());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }

}
