package com.tc.socket.feign;

import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.context.annotation.Bean;


public class FeignClientSimpleEncodeConfig {
    @Bean
    public Encoder encoder() {
        return new FormEncoder();
    }
}
