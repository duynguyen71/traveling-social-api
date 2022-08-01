package com.tc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.tc.tcapi.service.FileUploadProperties;

@SpringBootApplication
@EnableEurekaClient
@EnableConfigurationProperties({FileUploadProperties.class})
@EnableFeignClients
public class TravelingCrewApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelingCrewApiApplication.class, args);
    }
}
