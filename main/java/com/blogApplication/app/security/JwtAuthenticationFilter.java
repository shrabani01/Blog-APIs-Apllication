package com.blogApplication.app.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException; // Added for new exception
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private ApplicationContext context;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get token from Authorization header
        String requestToken = request.getHeader("Authorization");

        String username = null;
        String token = null;

        // Check if token is provided and starts with "Bearer "
        if (requestToken != null && requestToken.startsWith("Bearer ")) {
            token = requestToken.substring(7); // Extract the token
            try {
                // Extract username from token
                username = this.jwtTokenHelper.getUserNameFromToken(token);

            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT token has expired");
            } catch (MalformedJwtException e) {
                System.out.println("Invalid JWT token");
            } catch (SignatureException e) { // Handle invalid signature
                System.out.println("JWT signature validation failed");
            }
        } else {
            System.out.println("JWT token doesn't begin with Bearer");
        }

        // Validate token and set authentication in SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(UserDetailsService.class).loadUserByUsername(username);

            if (this.jwtTokenHelper.validateToken(token, userDetails)) {
                // Create an authentication token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication to security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("Invalid JWT token");
            }
        } else {
            System.out.println("Username is null or context already set");
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
