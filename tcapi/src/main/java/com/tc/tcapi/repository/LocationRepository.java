package com.tc.tcapi.repository;

import com.tc.tcapi.model.Location;
import com.tc.tcapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {


    Optional<Location> findByUser_IdAndType(Long userId,int type);

    List<Location> findAllByType(int popularPlace);
}
