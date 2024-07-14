package com.example.virtual_power_plant.service;

import java.util.List;
import java.util.OptionalDouble;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.virtual_power_plant.model.Battery;
import com.example.virtual_power_plant.repository.BatteryRepository;

@Service
public class BatteryService {

    @Autowired
    private BatteryRepository batteryRepository;

    public Battery saveBattery(Battery battery) {
        return batteryRepository.save(battery);
    }

    public List<Battery> saveBatteries(List<Battery> batteries) {
        return batteryRepository.saveAll(batteries);
    }

    public List<Battery> getBatteriesByPostcodeRange(String startPostcode, String endPostcode) {
        return batteryRepository.findByPostcodeBetween(startPostcode, endPostcode);
    }

    public double getTotalWattCapacity(List<Battery> batteries) {
        return batteries.stream().mapToInt(Battery::getWattCapacity).sum();
    }

    public OptionalDouble getAverageWattCapacity(List<Battery> batteries) {
        return batteries.stream().mapToInt(Battery::getWattCapacity).average();
    }
}