package com.tc.tcapi.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tc.tcapi.service.JwtService;
import com.tc.tcapi.service.MyUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    public static final String AUTH_URL_PATTERN = "\\.*/auth/\\.*";
    public static final String PUBLIC_URL_PATTERN = "\\.*/public\\.*";


    private final MyUserDetailService userDetailService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUrl = request.getRequestURI();

        Pattern publicUrlPattern = Pattern.compile(PUBLIC_URL_PATTERN);
        Pattern authUrlPattern = Pattern.compile(AUTH_URL_PATTERN);

        if (publicUrlPattern.matcher(requestUrl).find() || authUrlPattern.matcher(requestUrl).find()) {
            SecurityContextHolder.getContext().setAuthentication(null);
        } else {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (header != null && header.startsWith("Bearer ")) {
                try {
                    String token = header.substring("Bearer ".length());
                    DecodedJWT decodeToken = jwtService.decode(token);
                    String email = decodeToken.getSubject();
                    List<SimpleGrantedAuthority> roles =
                            Arrays
                                    .stream(decodeToken.getClaim("roles")
                                            .asArray(String.class))
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
                    UserDetails userDetails =
                            userDetailService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(userDetails, null, roles);
                    SecurityContextHolder.getContext().setAuthentication(authenticate);
                } catch (TokenExpiredException e) {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    new ObjectMapper().writeValue(response.getOutputStream(), Map.of("message", "token expired"));
                }catch (Exception e){
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    new ObjectMapper().writeValue(response.getOutputStream(), Map.of("message", "token is not valid"));
                }
            } else {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                new ObjectMapper().writeValue(response.getOutputStream(), Map.of("message", "token is not valid"));
            }
        }
        filterChain.doFilter(request, response);
    }
}
