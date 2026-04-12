package com.smartcampus.resource;

import com.smartcampus.model.SensorReading;
import com.smartcampus.service.SensorReadingService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collection;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    private static final SensorReadingService readingService =
            new SensorReadingService();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET readings for sensor
    @GET
    public Collection<SensorReading> getReadings() {

        return readingService.getReadings(sensorId);
    }

    // POST new reading
    @POST
    public Response addReading(SensorReading reading) {

        SensorReading created =
                readingService.addReading(sensorId, reading);

        return Response.status(Response.Status.CREATED)
                .entity(created)
                .build();
    }
}