package com.tc.tcapi.repository;

import com.tc.core.model.DeviceMetadata;
import com.tc.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, Long> {

    List<DeviceMetadata> findAllByUser_Id(Long userId);

    Optional<DeviceMetadata> findByUser_IdAndDeviceDetailAndLocation(Long userId,String detail,String location);
}
