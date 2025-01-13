package com.northcoders.bandit.filter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.northcoders.bandit.model.TestUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FirebaseAuthFilter extends OncePerRequestFilter {

    //can be removed depending on further changes
    @Autowired
    SecurityProperties restSecProps;


    @Autowired
    SecurityProperties securityProps;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        verifyToken(request);
        filterChain.doFilter(request, response);
    }

    private void verifyToken(HttpServletRequest request) {
        String session = null;
        FirebaseToken decodedToken = null;
        String token = getBearerToken(request);
        logger.info(token);
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            System.out.println("Firebase Exception:: "+ e.getLocalizedMessage());
        }
        TestUser userProfile = firebaseTokenToUserDto(decodedToken);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userProfile,
                null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getBearerToken(HttpServletRequest request) {
        String authorization = ((HttpServletRequest) request).getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return authorization;
    }

    private TestUser firebaseTokenToUserDto(FirebaseToken decodedToken) {
        TestUser profile = null;
        if (decodedToken != null) {
            decodedToken.getUid();
            decodedToken.getName();
        }
        return new TestUser(decodedToken.getName());
    }

}