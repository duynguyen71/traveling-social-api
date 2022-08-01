package com.tc.tcapi.service;

import com.tc.core.model.ReviewPost;
import com.tc.tcapi.repository.ReviewPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewPostService {

    @Autowired
    private ReviewPostRepository reviewPostRepository;
    public ReviewPost save(ReviewPost reviewPost){
        return reviewPostRepository.save(reviewPost);
    }
}
