package com.tc.socket.controller;

import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor
@Configuration
public class FeignRequestInterceptor implements feign.RequestInterceptor {

    private final AuthToken authToken;

    @Override
    public void apply(RequestTemplate requestTemplate) {
            requestTemplate.header(HttpHeaders.AUTHORIZATION, authToken.getBearerToken());
    }
}
