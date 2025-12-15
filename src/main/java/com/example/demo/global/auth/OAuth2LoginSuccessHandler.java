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
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // providerId 추출 (CustomOAuth2UserService에서 설정한 값 사용)
        String providerId = extractProviderId(oAuth2User);

        log.info("OAuth2 로그인 성공 - providerId: {}", providerId);

        String accessToken = jwtTokenProvider.createAccessToken(providerId);

        // 프론트엔드로 리다이렉트 (설정에서 URL 읽어옴)
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", accessToken)
                .build().toUriString();

        log.info("리다이렉트 URL: {}", targetUrl);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String extractProviderId(OAuth2User oAuth2User) {
        // CustomOAuth2UserService에서 설정한 providerId를 먼저 확인
        String providerId = oAuth2User.getAttribute("providerId");
        if (providerId != null) {
            return providerId;
        }

        // Fallback: 직접 추출 (혹시 모를 경우를 대비)
        @SuppressWarnings("unchecked")
        Map<String, Object> naverResponse = oAuth2User.getAttribute("response");
        if (naverResponse != null && naverResponse.get("id") != null) {
            return "naver_" + naverResponse.get("id");
        }

        Object kakaoId = oAuth2User.getAttribute("id");
        if (kakaoId != null) {
            return "kakao_" + kakaoId;
        }

        String sub = oAuth2User.getAttribute("sub");
        if (sub != null) {
            return sub;
        }

        throw new IllegalArgumentException("Cannot extract provider ID from OAuth2User");
    }
}
