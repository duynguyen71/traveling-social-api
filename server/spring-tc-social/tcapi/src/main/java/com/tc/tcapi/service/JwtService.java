package com.tc.tcapi.service;

//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.TokenExpiredException;
//import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.tc.tcapi.model.MyUserDetail;
import com.tc.tcapi.model.Role;
import com.tc.tcapi.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class JwtService {

    private final String SECRET = "322qrrcccccccccccccccccccccccqawff";

    public String generateToken(MyUserDetail user, int day) {
        Role role = user.getRole();
        Date expiresAt = new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000 * day));
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        String access_token = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(expiresAt)
                .withIssuer("khanduy@gmail.com")
                .withClaim("roles", List.of(role.getName().name()))
                .sign(algorithm);
        return access_token;
    }

    public String generateToken(User user, int day) {
        Role role = user.getRole();
        Date expiresAt = new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000 * day));
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        String access_token = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(expiresAt)
                .withIssuer("khanduy@gmail.com")
                .withClaim("roles", List.of(role.getName().name()))
                .sign(algorithm);
        return access_token;
    }

    public DecodedJWT decode(String token) throws TokenExpiredException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT verify = verifier.verify(token);
        return verify;
    }

}
