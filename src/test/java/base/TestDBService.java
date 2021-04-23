package base;

import base.datasource.DatabaseService;
import base.datasource.entity.Group;
import base.datasource.entity.Student;
import base.datasource.entity.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import rest.helper.BaseTestHelper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class TestDBService extends BaseTestHelper {

    @Resource
    private DatabaseService service;

    @BeforeEach
    public void testBefore() {
        assertNotNull(service);
        service.clearTable();
    }

    @Test
    public void findUniversityTest(){

        long id = 100L;

        University un = new University(id,"Best university");
        University un2 = service.saveUniversity(un);
        assertNotNull (un2 );
        assertNotNull (un2.getId());
        //log.info(un2);

        Optional<University> op = service.findUniversityById(id);
        assertTrue (op.isPresent());
        assertNotNull (op.get().getId() );
        //log.info(un);
    }

    @Test
    public void findGroupIdTest2(){

        long id = 100L;
        University un = new University(id,"Best university");
        un= service.saveUniversity(un);

        Group gr = new Group(10L,"group1");
        gr.setUniversity(un);
        un.getListGroup().add(gr);
        service.saveUniversity(un);

        List<Group> lg =  service.findListGroupByUniversityId(id);
        assertEquals (lg.size() , 1);
    }

    @Test
    public void findGroupTest(){

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

        //log.info(l);
        assertEquals (l.size() ,  10);
        assertNotNull (l.get(5) );
    }

    @Test
    public void findStudentByGroupIdTest4(){

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
        assertTrue(service.addStudentToGroupByGroupId(s0,groupId));

        Optional<Student> op = service.findStudentByNameSurName("name11","surname11");
        assertTrue (op.isPresent());
        assertNotEquals (op.get().getGroupId() , 11L);

        l = service.findStudentByGroupId(groupId);
        assert (l.size() == 11);

        service.deleteStudent("name11","surname11");
        l = service.findStudentByGroupId(groupId);

        //log.info(l);
        assert (l.size() == 10);
    }

    @Test
    public void createTest() {

        service.clearTable();
        long id = 100L, groupId = 1000;
        University un = new University(id, "Best university");

        for (char i = 'A'; i < 'Z'; i++) {
            Group gr = new Group(groupId + i, "group" +i);
            gr.setUniversity(un);

            for (int j = 1; j <= 10; j++) {
                //log.info(i+":"+j);
                Student s = new Student(("name" + i) +j, ("surname" +  i)+j, new Date());
                s.setGroup(gr);
                gr.getListStudents().add(s);
            }
            un.getListGroup().add(gr);
        }
        service.saveUniversity(un);
    }
}
