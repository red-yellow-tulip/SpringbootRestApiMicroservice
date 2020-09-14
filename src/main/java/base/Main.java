package base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class Main implements  ApplicationRunner{

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Value("${server.port}")
    String port;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("start...."+port);

    }
}