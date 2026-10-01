package com.simao.perfumehub.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.simao.perfumehub.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
    private final byte[] secret;
    private final long expirationMinutes;

    public JwtService(@Value("${spring.jwt.secret}") String secret, @Value("${spring.jwt.expiration-minutes}") long expirationMinutes) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(User user) {
        try {
            Instant now = Instant.now();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .claim("role", user.getRole().name())
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                    .build();

            SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
            jwt.sign(new MACSigner(secret));
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new IllegalArgumentException("Could not generate JWT", e);
        }
    }

    public SignedJWT parseAndValidate(String token) {
            try {
                SignedJWT jwt = SignedJWT.parse(token);

                if (!jwt.verify(new MACVerifier(secret))) {
                    throw new BadCredentialsException("Invalid JWT token");
                }

                Date expiration = jwt.getJWTClaimsSet().getExpirationTime();
                if (expiration == null || expiration.before(new Date())) {
                    throw new BadCredentialsException("JWT token expired");
                }

                return jwt;
            } catch (ParseException | JOSEException e) {
                throw new BadCredentialsException("Invalid JWT token");
            }
    }

    public String extractEmail(SignedJWT jwt) {
        try {
            return jwt.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new BadCredentialsException("Invalid JWT token");
        }
    }

    public long getExpirationSeconds() {
        return expirationMinutes * 60;
    }
}