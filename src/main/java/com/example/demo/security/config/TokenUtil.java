package com.example.demo.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.security.applicationuser.repository.entity.ApplicationUser;
import com.example.demo.security.applicationuser.repository.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;


public class TokenUtil {


    private static long ACCESS_TOKEN_TIME_IN_MINUTES = Long.parseLong(System.getenv("access_token_time_in_minutes"));
    private static long REFRESH_TOKEN_TIME_IN_MINUTES = Long.parseLong(System.getenv("refresh_token_time_in_minutes"));

    private static long ACCESS_TOKEN_EXPIRE_TIME_MILLIS = ACCESS_TOKEN_TIME_IN_MINUTES * 60 * 1000;
    private static long REFRESH_TOKEN_EXPIRE_TIME_MILLIS = REFRESH_TOKEN_TIME_IN_MINUTES * 60 * 1000;
    private static String algorithmSecret = "secret";

    public static Algorithm getJwtAlgorithm() {
        return Algorithm.HMAC256(algorithmSecret.getBytes());
    }

    public static String createRefreshToken(String userName, Algorithm algorithm, HttpServletRequest request) {
        return JWT.create()
                .withSubject(userName)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME_MILLIS))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }

    public static String createAccessToken(User user, Algorithm algorithm, HttpServletRequest request) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME_MILLIS))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public static String createAccessTokenWithRoles(ApplicationUser user,
                                                    Algorithm algorithm,
                                                    HttpServletRequest request,
                                                    Set<Role> rolesSet) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME_MILLIS))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", rolesSet.stream().map(r -> r.getName()).collect(Collectors.toList()))
                .sign(algorithm);
    }

}
