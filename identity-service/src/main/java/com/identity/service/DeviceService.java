package com.identity.service;

import com.identity.dto.DeviceDTO;
import com.identity.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeviceService {

    Device saveDevice(DeviceDTO dto, String token);

    Page<Device> getAllDevices(String search, Pageable pageable);

    Optional<Device> getDeviceById(Long id);

    Device updateDeviceReturnEntity(Long id, DeviceDTO dto, String token);

    Device deleteDeviceReturnEntity(Long id, String token);

    Device patchDevice(Long id, String key, Object value);
}
