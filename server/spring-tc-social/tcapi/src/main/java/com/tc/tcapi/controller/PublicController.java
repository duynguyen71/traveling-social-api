package com.tc.tcapi.controller;

import com.tc.tcapi.helper.ReviewPostHelper;
import com.tc.tcapi.utilities.Constance;
import com.tc.tcapi.helper.UserHelper;
import com.tc.core.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PublicController {

    private final UserHelper userHelper;
    private final ReviewPostHelper reviewPostHelper;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return null;
    }

    @GetMapping("validation-input")
    public ResponseEntity<?> validationInput(@RequestParam("input") String input,
                                             @RequestParam("value") String value) {
        return userHelper.validationInput(input, value);
    }

    @PostMapping("/users/registration")
    public ResponseEntity<?> registrationAccount(@RequestBody @Valid RegistrationRequest registrationRequest) {
        return userHelper.registrationAccount(registrationRequest);
    }

    @GetMapping("/images/{name}")
    public ResponseEntity<?> getImage(@PathVariable("name") String name) {
        return userHelper.getImage(name);
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> getReviewPosts(@RequestParam Map<String, String> param) {
        return reviewPostHelper.getReviewPosts(param);
    }
}
