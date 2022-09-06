package com.tc.tcapi.repository;

import com.tc.tcapi.model.TourReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TourReactionRepository extends JpaRepository<TourReaction, Long>  {
}