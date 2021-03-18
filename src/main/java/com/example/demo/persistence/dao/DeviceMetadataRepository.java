package com.example.demo.persistence.dao;

import com.example.demo.persistence.model.DeviceMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, Long> {
}
