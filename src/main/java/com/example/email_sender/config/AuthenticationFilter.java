package com.example.email_sender.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.email_sender.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String SERVICE_HEADER     = "Authorization-key";
    private static final String SERVICE_TOKEN      = "authenticationMicroserviceForSendingRecoveryCodes";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 1) Если есть Bearer‑токен, пытаемся его обработать
        if (isValid(authHeader) && authHeader.startsWith("Bearer ")) {
            handleJwt(authHeader, request, response, filterChain);
        }else {
            // 2) Иначе пытаемся сервисную аутентификацию по кастомному заголовку
            String serviceHeader = request.getHeader(SERVICE_HEADER);
            if (isValid(serviceHeader) && SERVICE_TOKEN.equals(serviceHeader)) {
                UserDetails svcUser = new User(
                        "authenticationMicroservice",
                        "",
                        Collections.emptyList()
                );
                setAuthentication(svcUser);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleJwt(String authHeader,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           FilterChain filterChain)
            throws IOException, ServletException {

        try {
            String jwt = authHeader.substring(7);
            if (jwt.isBlank()) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Empty JWT token");
                return;
            }

            long telegramId = jwtUtil.validateTokenAndRetrieveClaim(jwt);
            UserDetails userDetails = new User(
                    String.valueOf(telegramId),
                    "",
                    Collections.emptyList()
            );
            setAuthentication(userDetails);
        } catch (JWTVerificationException ex) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
        }
    }

    private boolean isValid(String header) {
        return header != null && !header.isBlank();
    }

    private void setAuthentication(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        "",
                        userDetails.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authToken);
        System.out.println("Authentication: " + SecurityContextHolder.getContext().getAuthentication());
    }

    private void sendError(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.getWriter().write(message);
        response.getWriter().flush();
    }
}
