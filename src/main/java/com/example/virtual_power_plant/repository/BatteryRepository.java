package com.example.virtual_power_plant.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.virtual_power_plant.model.Battery;


public interface BatteryRepository extends JpaRepository<Battery, Long> {
    List<Battery> findByPostcodeBetween(String startPostcode, String endPostcode);
}