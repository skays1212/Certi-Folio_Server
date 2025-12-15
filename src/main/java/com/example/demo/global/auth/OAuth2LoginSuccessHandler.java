package com.example.demo.global.auth;

import com.example.demo.global.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.oauth2.authorized-redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 이메일 추출 (카카오, 네이버 각각 다른 구조)
        String email = extractEmail(oAuth2User);
        
        log.info("OAuth2 로그인 성공 - email: {}", email);

        String accessToken = jwtTokenProvider.createAccessToken(email);

        // 프론트엔드로 리다이렉트 (설정에서 URL 읽어옴)
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", accessToken)
                .build().toUriString();

        log.info("리다이렉트 URL: {}", targetUrl);
        
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String extractEmail(OAuth2User oAuth2User) {
        // 카카오의 경우
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        if (kakaoAccount != null) {
            return (String) kakaoAccount.get("email");
        }

        // 네이버의 경우
        Map<String, Object> naverResponse = oAuth2User.getAttribute("response");
        if (naverResponse != null) {
            return (String) naverResponse.get("email");
        }

        // 기본 (구글 등)
        return oAuth2User.getAttribute("email");
    }
}