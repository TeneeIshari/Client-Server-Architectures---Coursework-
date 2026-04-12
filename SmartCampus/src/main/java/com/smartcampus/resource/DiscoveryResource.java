/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Teneesha
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {
    
    @GET
    public Map<String, Object> getApiInfo(){
        Map<String, Object> response = new HashMap<>();
        
        // API Metadatada
        response.put("api", "Smart Campus API");
        response.put("version", "v1");
        response.put("api", "Smart Campus API");
        
        // Resource links
        Map<String, String> resources = new HashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        
        response.put("resources", resources);
        
        return response;
        
        
    }
    
}
