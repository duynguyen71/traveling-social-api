package com.tc.tcapi.service;

import com.tc.tcapi.model.Reaction;
import com.tc.tcapi.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepo;

    public Reaction getById(Long reactionId){
        return reactionRepo.findById(reactionId).orElse(null);
    }
}
