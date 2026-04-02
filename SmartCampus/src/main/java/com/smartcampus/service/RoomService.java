/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.service;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Teneesha
 */
public class RoomService {
    private final Map<String, Room> rooms = new HashMap<>();
    
    // Get all Rooms
    public Collection<Room> getAllRooms(){
        return rooms.values();
    }
    
    // Get a room by ID
    public Room getRoom(String id){
        return rooms.get(id);
    }
    
    // Delete a Room
    public void deleteRoom(String id) throws RoomNotEmptyException {
        Room room = rooms.get(id);
        if (room == null) return; 
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room " + id + " has active sensors and cannot be deleted");
        }
        rooms.remove(id);
    }
    
}
