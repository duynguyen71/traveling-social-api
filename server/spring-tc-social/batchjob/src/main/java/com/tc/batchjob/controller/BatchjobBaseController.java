package com.tc.batchjob.controller;

import com.tc.batchjob.feign.AuthFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
@Slf4j
public class BatchjobBaseController {

    private final AuthFeignClient authFeignClient;

    @GetMapping("/hihi")
    public ResponseEntity<?> getHiHI() {
        Object users = authFeignClient.getUsers();
        return ResponseEntity.ok(users);
    }
}
