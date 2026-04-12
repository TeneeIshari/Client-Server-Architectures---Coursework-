/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.service;

/**
 *
 * @author indika
 */
import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorReadingService {
    
    private final Map<String, List<SensorReading>> readingsMap = new HashMap<>();
    private final SensorService sensorService = new SensorService();
    
    public Collection<SensorReading> getReadings(String sensorId) {
        return readingsMap.getOrDefault(sensorId, new ArrayList<>());
    }
    
    public SensorReading addReading(String sensorId, SensorReading reading) {
        Sensor sensor = sensorService.getSensor(sensorId);
        if (sensor != null && "MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor " + sensorId + " is currently under maintenance.");
        }
        
        readingsMap.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
        
        if (sensor != null) {
            sensor.setCurrentValue(reading.getValue());
        }
        
        return reading;
    }

}
