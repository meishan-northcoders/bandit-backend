package com.northcoders.bandit.filter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.northcoders.bandit.model.FireBaseUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
public class FirebaseAuthFilter extends OncePerRequestFilter {

    //can be removed depending on further changes
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.startsWith("/api/v1/")) {
            filterChain.doFilter(request, response);
            return;
        }
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
        FireBaseUser fireBaseUser = firebaseTokenToUserDto(decodedToken);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(fireBaseUser,
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

    private FireBaseUser firebaseTokenToUserDto(FirebaseToken decodedToken) {
        FireBaseUser fireBaseUser = new FireBaseUser();
        if (decodedToken != null) {
            fireBaseUser.setUserId(decodedToken.getUid());
            fireBaseUser.setUserName(decodedToken.getName());
            fireBaseUser.setEmail(decodedToken.getEmail());
            fireBaseUser.setGooglePictureLink(decodedToken.getPicture());
            fireBaseUser.setEmailVerified(decodedToken.isEmailVerified());
        }
        return fireBaseUser;
    }

}