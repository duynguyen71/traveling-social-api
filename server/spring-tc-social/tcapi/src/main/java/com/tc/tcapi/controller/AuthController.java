package com.tc.tcapi.controller;

import com.tc.core.request.ResetPasswordRequest;
import com.tc.tcapi.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserHelper userHelper;

    @GetMapping("/verification")
    public ResponseEntity<?> verificationAccount(@RequestHeader("code") String code) {
        return userHelper.verificationAccount(code);
    }

    /**
     * get verification code
     * tao ma code moi de reset password hoac quen mat khau
     */
    @GetMapping("/verification-code/reset")
    public ResponseEntity<?> requestForVerificationCode() {
        return userHelper.requestVerificationCode();
    }

    /**
     * reset password
     */
    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return userHelper.resetCurrentUserPassword(request);
    }

    //TODO: implements refresh token


}
