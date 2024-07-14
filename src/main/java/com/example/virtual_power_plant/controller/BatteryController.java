package com.example.virtual_power_plant.controller;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.virtual_power_plant.model.Battery;
import com.example.virtual_power_plant.service.BatteryService;

@RestController
@RequestMapping("/batteries")
public class BatteryController {

    @Autowired
    private BatteryService batteryService;

    @PostMapping
    public ResponseEntity<List<Battery>> addBatteries(@RequestBody List<Battery> batteries) {
        List<Battery> savedBatteries = batteryService.saveBatteries(batteries);
        return ResponseEntity.ok(savedBatteries);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getBatteriesByPostcodeRange(@RequestParam String postcodeRange) {
        String[] postcodes = postcodeRange.split("-");
        if (postcodes.length != 2) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid postcode range format"));
        }
        String startPostcode = postcodes[0];
        String endPostcode = postcodes[1];

        List<Battery> batteries = batteryService.getBatteriesByPostcodeRange(startPostcode, endPostcode);
        List<String> batteryNames = batteries.stream()
                                             .map(Battery::getName)
                                             .sorted()
                                             .collect(Collectors.toList());

        double totalWattCapacity = batteryService.getTotalWattCapacity(batteries);
        OptionalDouble averageWattCapacity = batteryService.getAverageWattCapacity(batteries);

        return ResponseEntity.ok(Map.of(
            "batteryNames", batteryNames,
            "statistics", Map.of(
                "totalWattCapacity", totalWattCapacity,
                "averageWattCapacity", averageWattCapacity.isPresent() ? averageWattCapacity.getAsDouble() : 0
            )
        ));
    }
}
