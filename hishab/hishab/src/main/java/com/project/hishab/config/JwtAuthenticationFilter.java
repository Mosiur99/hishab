package com.project.hishab.config;

import com.project.hishab.Enum.UserRole;
import com.project.hishab.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractEmail(token);
                    Long userId = jwtUtil.extractUserId(token);
                    UserRole role = jwtUtil.extractRole(token);
                    
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()))
                    );
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token is invalid, continue without authentication
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
