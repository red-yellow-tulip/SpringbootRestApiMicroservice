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
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class StudentMicroserviceRunner implements  ApplicationRunner{

    private static final Logger log = LoggerFactory.getLogger(StudentMicroserviceRunner.class);

    @Autowired
    private DatabaseService databaseService;

    @Value("${server.port}")
    String port;

    @Value("${spring.application.name}")
    String name;

    public static void main(String[] args) {
        SpringApplication.run(StudentMicroserviceRunner.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (databaseService.findAllUniversity().isEmpty())
            databaseService.createDemoData(10,10);
        log.info(String.format("start app: %s, port: %s",name,port));
    }
}