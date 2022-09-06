package com.tc.tcapi.service;

import com.tc.tcapi.repository.TourCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TourCommentService {

    private final TourCommentRepository repo;
}
