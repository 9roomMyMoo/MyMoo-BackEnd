package com.example.mymoo.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String authorizationHeader = request.getHeader("Authorization");
        log.info("Request URI: {}, Authorization Header: {}", uri, authorizationHeader);

        // jwt 헤더가 없는 경우 다음 필터로
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(7); // 토큰 부분만 추출
        try {
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwt);
            // Authentication 객체를 등록
            SecurityContextHolder.getContext().setAuthentication(
                jwtTokenProvider.getAuthenticationFromClaims(claims)
            );
        } catch (JwtException e) {
            log.error("JWT 토큰 오류: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT 토큰 오류: " + e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }
}