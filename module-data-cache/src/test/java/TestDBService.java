import base.CacheApplicationRunner;
import base.datasource.DatabaseService;
import base.datasource.entity.Group;
import base.datasource.entity.Student;
import base.datasource.entity.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


//@ActiveProfiles("test.h2")
@SpringBootTest(classes = CacheApplicationRunner.class)
public class TestDBService {

    @Resource
    protected DatabaseService service;

    @BeforeEach
    public void testBefore() {
        assertNotNull(service);
        service.clearTable();
       // service.createDemoData(5,10);
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

        Student op = service.findStudentByNameSurName("name11","surname11").get();
        assertNotEquals (op.getGroupId() , 11L);

        Student op2 = service.findStudentByNameSurName("name11","surname11").get();
        assertNotEquals (op2.getGroupId() , 11L);

        long studentId = op2.getId();
        Student opId = service.findStudentById(studentId).get();
        assertEquals (opId.getId() , studentId);

        Student opId2 = service.findStudentById(studentId).get();
        assertEquals (opId2.getId() , studentId);

        l = service.findStudentByGroupId(groupId);
        assert (l.size() == 11);

        service.deleteStudent("name11","surname11");
        l = service.findStudentByGroupId(groupId);

        //log.info(l);
        assert (l.size() == 10);
    }
}
