package com.tc.tcapi.repository;

import com.tc.tcapi.model.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    List<Tag> findByNameContainingIgnoreCase(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM tag t\n" +
            "WHERE (:name IS NULL OR t.name LIKE :name)")
    List<Tag> getTagsNative(@Param("name") String name, Pageable pageable);

    boolean existsByName(String name);

}
