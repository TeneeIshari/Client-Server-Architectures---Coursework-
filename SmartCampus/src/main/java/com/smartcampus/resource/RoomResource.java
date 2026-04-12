/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.service.RoomService;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Teneesha
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {
    
    private static final RoomService roomService = new RoomService();
    
    // Get all the rooms
    @GET
    public Collection<Room> getAllRooms(){
        return roomService.getAllRooms();
    }
    
    // Get room by ID
    @GET
    @Path("{id}")
    public Response getRoom(@PathParam("id") String id){
        Room room = roomService.getRoom(id);
        if(room == null){
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Room not found\"}").build();
        }
        return Response.ok(room).build();
    }
    
    // Create a room
    @POST
    public Response createRoom(Room room){
        Room created = roomService.createRoom(room);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    // Delete a room
    @DELETE
    @Path("{id}")
    public Response deleteRoom(@PathParam("id") String id) throws RoomNotEmptyException {
        roomService.deleteRoom(id);
        return Response.noContent().build();
    }
    
}
