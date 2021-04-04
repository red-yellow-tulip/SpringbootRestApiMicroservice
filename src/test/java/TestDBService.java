
import base.Main;
import base.entity.Group;
import base.entity.Student;
import base.entity.University;
import base.serviceDB.ServiceDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = Main.class)
@RunWith(SpringRunner.class)
public class TestDBService {

    private static final Logger log = LogManager.getLogger();

    @Resource
    private ServiceDatabase service;

    @Before
    public void testBefore() {

        assertNotNull(service);

        service.clearTable();

    }

    @Test
    public void Test0() {



        assertNotNull(service);

        service.clearTable();

    }

    @Test
    public void Test(){

        long id = 100L;

        University un = new University(id,"Best university");
        University un2 = service.saveUniversity(un);
        assert (un2 != null);
        assert (un2.getId() != null);
        log.info(un2);

        un = service.findUniversityById(id);
        assert (un != null);
        assert (un.getId() != null);
        log.info(un);

    }

    @Test
    public void Test2(){

        long id = 100L;
        University un = new University(id,"Best university");
        un= service.saveUniversity(un);

        Group gr = new Group(10L,"group1");
        gr.setUniversity(un);
        un.getListGroup().add(gr);
        service.saveUniversity(un);

        List<Group> lg =  service.findListGroupByUniversityId(id);
        assert (lg.size() == 1);

    }

    @Test
    public void Test3(){

        long id = 100L, groupId = 10;
        University un = new University(id,"Best university");

        Group gr = new Group(groupId,"demo_group1");
        gr.setUniversity(un);

        for(int i = 0; i< 10; i++){
            Student s = new Student("demo_name"+i,"demo_surname"+i,new Date());
            s.setGroup(gr);
            gr.getListStudents().add(s);
        }

        un.getListGroup().add(gr);
        service.saveUniversity(un);
        List<Student> l = service.findStudentByGroupId(groupId);

        log.info(l);
        assert (l.size() == 10);
        assert (l.get(5) != null);

    }


    @Test
    public void Test4(){

        long id = 100L, groupId = 10;
        University un = new University(id,"Best university");

        Group gr = new Group(groupId,"group1");
        gr.setUniversity(un);

        for(long i = 0; i< 10; i++){
            Student s = new Student("name"+i,"surname"+i,new Date());
            s.setGroup(gr);
            gr.getListStudents().add(s);
        }

        un.getListGroup().add(gr);
        service.saveUniversity(un);
        List<Student> l = service.findStudentByGroupId(groupId);


        Student s0 = new Student("name11","surname11",new Date());
        Student s00 = service.addStudentByGroupId(s0,groupId);
        assert (s00 != null);


        Student s001 = service.getStudentByNameAndSurname("name11","surname11");
        assert (s001 != null);
        assert (s001.getGroupId() != 11L);


        l = service.findStudentByGroupId(groupId);
        assert (l.size() == 11);


        service.deleteStudent("name11","surname11");
        l = service.findStudentByGroupId(groupId);

        log.info(l);
        assert (l.size() == 10);
    }



    @Test
    public void Test5() {

        service.clearTable();

        long id = 100L, groupId = 1000;
        University un = new University(id, "Best university");

        for (char i = 'A'; i < 'Z'; i++) {
            Group gr = new Group(groupId + i, "group" +i);
            gr.setUniversity(un);

            for (int j = 1; j <= 10; j++) {
                log.info(i+":"+j);
                Student s = new Student(("name" + i) +j, ("surname" +  i)+j, new Date());
                s.setGroup(gr);
                gr.getListStudents().add(s);
            }

            un.getListGroup().add(gr);



        }
        service.saveUniversity(un);

    }
}
