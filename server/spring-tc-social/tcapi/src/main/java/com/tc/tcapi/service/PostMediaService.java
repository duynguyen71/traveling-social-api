package com.tc.tcapi.service;

import com.tc.tcapi.repository.PostContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostMediaService {

    private final PostContentRepository postContentRepository;


}
