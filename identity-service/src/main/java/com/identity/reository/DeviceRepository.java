package com.identity.reository;

import com.identity.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository  extends JpaRepository<Device, Long> {


    boolean existsByDeviceId(String deviceId);

    Page<Device> findByDeviceNameContainingIgnoreCase(String deviceName, Pageable pageable);
}
