/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.service;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author indika
 */
public class SensorService {
    
    private final Map<String, Sensor> sensors = new HashMap<>();
    private final RoomService roomService = new RoomService();
    
    public Collection<Sensor> getAllSensors(){
        return sensors.values();
    }
    
    public Collection<Sensor> getSensorsByType(String type){
        return sensors.values().stream().filter(sensor -> sensor.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
    }
    
    public Sensor getSensor(String id){
        return sensors.get(id);
    }
    
    // Create a new sensor
    public Sensor createSensor(Sensor sensor){
        Room room  = roomService.getRoom(sensor.getRoomId());
        
        if(room == null){
            throw new LinkedResourceNotFoundException("Room with ID " + sensor.getRoomId() + " does not exist");
        }
        
        sensors.put(sensor.getId(), sensor);
        
        room.getSensorIds().add(sensor.getId());
        
        return sensor;
    }
    
}
