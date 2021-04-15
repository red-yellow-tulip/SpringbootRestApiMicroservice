package base.utils.actuator;

import base.datasource.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomServiceActuator implements HealthIndicator {

    @Autowired
    protected DatabaseService databaseService;

    @Override
    public Health health() {

        if (databaseService != null ) {
            return Health.up().withDetail("Student count = ", databaseService.findAllStudent().size()).build();
        }
        return Health.down().build();
    }

}
