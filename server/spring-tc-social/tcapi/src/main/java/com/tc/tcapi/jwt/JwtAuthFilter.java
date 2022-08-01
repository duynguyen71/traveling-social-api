package com.tc.tcapi.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.maxmind.geoip2.model.CityResponse;
import com.tc.tcapi.model.MyUserDetail;
import com.tc.core.request.LoginRequest;
import com.tc.tcapi.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        BufferedReader reader = request.getReader();
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        String parseReq = stringBuffer.toString();
        if (parseReq != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequest loginRequest = objectMapper.readValue(parseReq, LoginRequest.class);

            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authenticate);

            return authenticate;
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new RuntimeException("Bad credentials");
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, String> map = new HashMap<>();
        map.put("message", failed.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream(), map);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("Success authentication ");
        MyUserDetail principal = (MyUserDetail) authResult.getPrincipal();
        String accessToken = jwtService.generateToken(principal,15);
        String refreshToken = jwtService.generateToken(principal,30);
        Map<String, String> data = Map.of("accessToken", accessToken,"refreshToken",refreshToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        //
        new ObjectMapper().writeValue(response.getOutputStream(), data);
    }


}
