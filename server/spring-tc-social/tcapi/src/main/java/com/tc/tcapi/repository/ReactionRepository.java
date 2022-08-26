package com.tc.tcapi.repository;

import com.tc.tcapi.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository  extends JpaRepository<Reaction,Long> {
}
