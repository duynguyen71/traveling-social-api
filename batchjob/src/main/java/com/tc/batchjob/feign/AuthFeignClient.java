package com.tc.batchjob.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "tcapi")
public interface AuthFeignClient {

    @GetMapping("/api/v1/public/users")
    Object getUsers();
}
