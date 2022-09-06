package com.tc.tcapi.repository;

import com.tc.tcapi.model.TourComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourCommentRepository extends JpaRepository<TourComment, Long> {
}