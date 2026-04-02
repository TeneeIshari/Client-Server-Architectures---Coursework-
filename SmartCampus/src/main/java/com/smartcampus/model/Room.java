/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author indika
 */
public class Room {
    private String id;
    private String name;
    private int capacity;
    private List<String> sensorId = new ArrayList<>();
    
    public Room(String id, String name, int capacity){
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }
    
    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<String> getSensorsId() {
        return sensorId;
    }

    public void setSensorsId(List<String> sensorsId) {
        this.sensorId = sensorsId;
    }
    
    // Adding sensor
    public void addSensor(String sensorId){
        this.sensorId.add(sensorId);
    }
    
    // Removing a sensor
    public void removeSensor(String sensorId){
        this.sensorId.remove(sensorId);
        
    }
}
