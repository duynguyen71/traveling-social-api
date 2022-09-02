package com.tc.tcapi.repository;

import com.tc.tcapi.model.DeviceMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, Long> {

    @Query(nativeQuery = true, value = "select d.* from user u join device_metadata d\n" +
            "on u.id = d.user_id where u.id = :id")
    List<DeviceMetadata> findDeviceListNative(@Param("id") Long userId);

}
