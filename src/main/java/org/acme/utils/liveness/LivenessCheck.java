package org.acme.utils.liveness;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;


@Liveness
@ApplicationScoped
public class LivenessCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {

        //Logica di controllo(?)

        return HealthCheckResponse.up("Sembra tutto ok!");
        //return HealthCheckResponse.down("Non sembra tutto ok");
    }
    
}
