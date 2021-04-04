package base;

import base.entity.Group;
import base.entity.Student;
import base.entity.University;
import base.serviceDB.ServiceDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@SpringBootApplication
public class Main implements  ApplicationRunner{

    private static final Logger log = LoggerFactory.getLogger(Main.class);


    @Autowired
    private ServiceDatabase serviceDatabase;

    @Value("${server.port}")
    String port;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (serviceDatabase.findAllUniversity().isEmpty())
            createDemoData();

        log.info("start...."+port);

    }

    private void createDemoData() {

        serviceDatabase.clearTable();

        long id = 100L, groupId = 1000;
        University un = new University(id, "Best university");

        for (char i = 'A'; i < 'Z'; i++) {
            Group gr = new Group(groupId + i, "demo_group" +i);
            gr.setUniversity(un);

            for (int j = 1; j <= 10; j++) {
                Student s = new Student(("demo_name" + i) +j, ("demo_surname" +  i)+j, new Date());
                s.setGroup(gr);
                gr.getListStudents().add(s);
            }

            un.getListGroup().add(gr);



        }
        serviceDatabase.saveUniversity(un);
    }
}