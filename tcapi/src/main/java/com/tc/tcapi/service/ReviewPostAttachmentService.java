package com.tc.tcapi.service;

import com.tc.tcapi.model.ReviewPost;
import com.tc.tcapi.model.ReviewPostImage;
import com.tc.tcapi.repository.ReviewPostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewPostAttachmentService {

    private final ReviewPostImageRepository repo;

    public ReviewPostImage getAttachment(Long id) {
        return repo.findById(id).orElse(null);
    }

    public boolean existById(Long id) {
        return repo.existsById(id);
    }

    public void save(ReviewPostImage attachment) {
         repo.save(attachment);
    }

    public List<ReviewPostImage> getImages(ReviewPost post,int status){
        return repo.findByReviewPostAndStatusOrderByPosAsc(post,status);
    }
}
