package org.acme.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    
    public Response toResponse(Exception exception) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
}
