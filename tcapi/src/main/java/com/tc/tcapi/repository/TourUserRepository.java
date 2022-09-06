package com.tc.tcapi.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourUserRepository extends JpaRepository<TourUser, Long> {
}