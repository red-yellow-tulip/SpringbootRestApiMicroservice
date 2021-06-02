package app.utils;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomServiceActuator implements HealthIndicator {

    private long counter = 0L;

    @Override
    public Health health() {

        if (true) {
            return Health.up().withDetail("counter = ", counter++).build();
        }
        return Health.down().build();
    }

}
