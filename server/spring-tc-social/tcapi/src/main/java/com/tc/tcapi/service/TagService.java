package com.tc.tcapi.service;

import com.tc.core.model.Tag;
import com.tc.tcapi.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository repo;

    public Tag getByName(String name) {
        return repo.findByName(name).orElse(null);
    }

    public boolean existsById(Long id) {
        return repo.existsById(id);
    }

    public boolean existsByName(String name) {
        return repo.existsByName(name);
    }

    public Tag saveFlush(Tag tag) {
        return repo.saveAndFlush(tag);
    }

    public List<Tag> getTags(String name, Pageable pageable) {
        return repo.getTagsNative(name, pageable);
    }

    public List<Tag> getTagsByName(String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }

    public Tag getById(Long id) {
        return repo
                .findById(id).orElse(null);
    }
}
