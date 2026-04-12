/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.service.SensorService;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Teneesha
 */

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    private static final SensorService sensorService = new SensorService();
    
    @GET
    public Collection<Sensor> getSensors(@QueryParam("type") String type){
        if(type != null){
             return sensorService.getSensorsByType(type);
        }
        return sensorService.getAllSensors();
    
    }
    
    // Get sensor by ID
    @GET
    @Path("{id}")
    public Response getSensor(@PathParam("id") String id){
        Sensor sensor = sensorService.getSeonsor(id);
        
        if(sensor == null){
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Sensor not found\"}").build();
        }
        return Response.ok(sensor).build();
    }
    
    // POST create sensor
    @POST
    public Response createSensor(Sensor sensor){
        Sensor created = sensorService.createSensor(sensor);
        
        return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Sensor not found\"}").build();
    }
    
    @Path("{id}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("id") String sensorId){
        
        return new SensorReadingResource(sensorId);
    }
    
}
