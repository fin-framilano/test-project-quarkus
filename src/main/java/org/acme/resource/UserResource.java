package org.acme.resource;

import javax.ws.rs.Produces;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.model.User;
import org.acme.service.UserService;
import org.acme.utils.ErrorJson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

@Path("/users")
public class UserResource {
    //Logger 
    private final static Logger logger = LoggerFactory.getLogger(UserResource.class);

    //Classe iniettata per separare le chiamate DAO dalla logica REST
    @Inject
    UserService userService;

    //Metrics accessibili dalla classe interna
    private MeterRegistry registry;
    
    //Nel costruttore dichiaro l'utilizzo delle metriche, come counter
    //Counter -> incremento e basta
    //Gauge -> Incremento e decremento
    //Timer -> misurazione di tempo
    public UserResource(MeterRegistry registry) {
        this.registry = registry;
        //Utilizzo questo counter per controllare il numero di chiamate /getall
        //Risultati disponibili su {baseUrl}/q/metrics in getAll_requests
        this.registry.counter("getAll.requests", Tags.empty());
    }

    // GET
    @GET
    @Path("/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
        // Logging
        logger.info("GET Request for " + id);

        // Logic
        User response = userService.getUser(id);
        if (response == null) 
            return Response.status(404)
            .entity(new ErrorJson("Utente non trovato"))
            .build();
        return Response.ok(userService.getUser(id)).build();
    }

    // POST
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addUser(User user) {
        // Logging
        logger.info("POST Request");

        // Logic
        String id = UUID.randomUUID().toString();
        user.setId(id);
        userService.addUser(user);
        return Response.ok(user.getId()).build();
    }

    // PUT
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User user) {
        // Logging
        logger.info(String.format("PUT Request for " + user.getId()));

        // Logic
        if (!userService.updateUser(user.getId(), user))
            return Response.status(404).build();
        else
            return Response.ok(user.getId()).build();
    }

    // DELETE
    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") String id) {
        // Logging
        logger.info(String.format("DELETE Request for " + id));

        // Logic
        if (!userService.deleteUser(id))
            return Response.status(404)
                    .entity(new ErrorJson("Impossibile eliminare l'utente"))
                    .build();
        else
            return Response.ok().build();
    }

    // GET ALL
    @GET
    @Path("/getall")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        // Logging and Metrics
        this.registry.counter("getAll.requests").increment();
        logger.info(String.format("GET ALL Requested"));

        // Logic
        return Response.ok(userService.getAllUsers()).build();
    }
}
