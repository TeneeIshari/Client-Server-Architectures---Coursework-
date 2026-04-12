package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.service.SensorService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collection;

/**
 *
 * @author Teneesha
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private static final SensorService sensorService = new SensorService();

    // GET sensors (with filtering)
    @GET
    public Collection<Sensor> getSensors(
            @QueryParam("type") String type) {

        if (type != null) {
            return sensorService.getSensorsByType(type);
        }

        return sensorService.getAllSensors();
    }

    // GET sensor by ID
    @GET
    @Path("{id}")
    public Response getSensor(@PathParam("id") String id) {

        Sensor sensor = sensorService.getSensor(id);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Sensor not found\"}")
                    .build();
        }

        return Response.ok(sensor).build();
    }

    // POST create sensor
    @POST
    public Response createSensor(Sensor sensor) {

        Sensor created = sensorService.createSensor(sensor);

        return Response.status(Response.Status.CREATED)
                .entity(created)
                .build();
    }

    
    @Path("{id}/readings")
    public SensorReadingResource getSensorReadingResource(
            @PathParam("id") String sensorId) {

        return new SensorReadingResource(sensorId);
    }
}