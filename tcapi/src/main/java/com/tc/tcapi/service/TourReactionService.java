package com.tc.tcapi.service;

import com.tc.tcapi.repository.TourReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TourReactionService {

    private final TourReactionRepository repo;
}
