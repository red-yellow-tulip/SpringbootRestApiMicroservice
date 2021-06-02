package base;

import base.datasource.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataMicroserviceRunner implements  ApplicationRunner{
    private static final Logger log = LoggerFactory.getLogger(DataMicroserviceRunner.class);

    @Autowired
    private DatabaseService databaseService;

    @Value("${server.port}")
    private String port;

    @Value("${spring.application.name}")
    private String name;

    public static void main(String[] args) {
        SpringApplication.run(DataMicroserviceRunner.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {

        log.info(String.format("start app: %s, port: %s",name,port));
    }
}