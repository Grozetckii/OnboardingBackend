package ru.grozetckii.SecurityApp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt.secret.access}")
    private String accessSecret;

    @Value("${jwt.secret.refresh}")
    private String refreshSecret;
    public String generateAccessToken(String username){
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(30).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("SecurityApp")
                .withExpiresAt(expirationDate) /*когда закончиться действие токена*/
                .sign(Algorithm.HMAC256(accessSecret));
    }

    public String generateRefreshToken(){
        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(30).toInstant());

        return JWT.create()
                .withSubject("refresh")
                .withIssuer("SecurityApp")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(refreshSecret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(accessSecret))
                .withSubject("User details")
                .withIssuer("SecurityApp")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(refreshSecret))
                    .withSubject("refresh")
                    .withIssuer("SecurityApp")
                    .build();

        }catch (JWTVerificationException e){
            return false;
        }
        return true;
    }
}
