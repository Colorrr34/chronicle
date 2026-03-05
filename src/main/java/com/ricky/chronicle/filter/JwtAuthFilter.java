package com.ricky.chronicle.filter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.service.JwtService;
import com.ricky.chronicle.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
    private final UserService userService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal (
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain)throws IOException,ServletException{
            String authHeader = request.getHeader("Authorization");

            try {if (authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            UUID userId = jwtService.extractUserId(token);
            List<GrantedAuthority> authorities = jwtService.getAuthoritiesFromToken(token);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication()== null){
                User user = userService.getAuthuser(userId);
                if (jwtService.validateToken(token, user)){
                    UsernamePasswordAuthenticationToken authToken = new 
                        UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            authorities
                        );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }}catch(Exception e){
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
    }
}
