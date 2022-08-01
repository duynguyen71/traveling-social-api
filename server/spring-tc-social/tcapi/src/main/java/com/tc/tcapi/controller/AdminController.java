package com.tc.tcapi.controller;

import com.tc.tcapi.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserHelper userHelper;

    /**
     * get pending post
     * lấy ra các bài review chưa được phê duyệt
     */
    public ResponseEntity<?> getPendingReviewPost() {
        return null;
    }


    /**
     * get member users
     */
    public ResponseEntity<?> getMemberUsers() {
        return userHelper.getMemberUsers();
    }

}
