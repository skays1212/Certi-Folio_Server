package com.example.demo.global.auth;

import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

        private final UserRepository userRepository;

        @Override
        @Transactional
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
                OAuth2User oAuth2User = delegate.loadUser(userRequest);

                String registrationId = userRequest.getClientRegistration().getRegistrationId();
                String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                .getUserInfoEndpoint().getUserNameAttributeName();

                OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                                oAuth2User.getAttributes());

                User user = saveOrUpdate(attributes);

                // providerId를 attributes에 추가하여 OAuth2LoginSuccessHandler에서 사용 가능하게 함
                Map<String, Object> userAttributes = new HashMap<>(attributes.getAttributes());
                userAttributes.put("providerId", attributes.getProviderId());

                return new DefaultOAuth2User(
                                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                                userAttributes,
                                attributes.getNameAttributeKey());
        }

        private User saveOrUpdate(OAuthAttributes attributes) {
                User user = userRepository.findByProviderId(attributes.getProviderId())
                                .map(entity -> entity.update(attributes.getName()))
                                .orElse(attributes.toEntity());

                return userRepository.save(user);
        }
}
