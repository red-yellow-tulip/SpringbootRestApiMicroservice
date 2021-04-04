import base.Main;
import base.entity.Group;
import base.entity.Student;
import base.entity.University;
import base.serviceDB.ServiceDatabase;
import base.web.config.SourceParameterWrapperGroup;
import base.web.config.SourceParameterWrapperStudent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = Main.class)
@RunWith(SpringRunner.class)
public class TestGroupController {

    private static final Logger log = LogManager.getLogger();

    private RestTemplate restTemplate = new RestTemplate();

    @Resource
    private ServiceDatabase service;

    private static final String url = "http://localhost:8095/";
    private static final String getAll = url + "group/all";
    private static final String filtr = url + "group/filtr?name=group2";
    private static final String filtr1 = url + "group/filtr?name=group";
    private static final String groupId = url + "group?id=50";
    private static final String post = url + "group/add";
    private static final String del = url + "group/delete?id=55";

    @Before
    public void testBefore() {

        assertNotNull(service);
        service.clearTable();
    }


    @Test
    public void TestGetAll() {

        addGroup();

        List<Group> allGroup = restTemplate.getForObject(getAll, SourceParameterWrapperGroup.ListWrapper.class);
        assert (allGroup.size() == 5);
        System.out.println(allGroup);

    }

    @Test
    public void TestFiltr() {


        addGroup();

        List<Group> allGroup = restTemplate.getForObject(filtr, SourceParameterWrapperGroup.ListWrapper.class);
        assert (allGroup.size() == 1);
        System.out.println(allGroup);

        List<Group> allGroup1 =  restTemplate.getForObject(filtr1, SourceParameterWrapperGroup.ListWrapper.class);
        assert (allGroup1.size() == 5);
        System.out.println(allGroup1);

    }

    @Test
    public void TestGroupId() {

        addGroup();

        Group group1 = restTemplate.getForObject(groupId, Group.class);
        assert (group1 != null);
        System.out.println(group1);

    }

    @Test
    public void TestAddNewGroup() {

        addGroup();

        long id = 100L, groupIdn = 55;

        Group gr = new Group(groupIdn, "group55");
        ResponseEntity<Group> e = restTemplate.postForEntity(post, gr, Group.class);
        assert (e.getStatusCode() == HttpStatus.CREATED);
        System.out.println(e.getStatusCode());

        List<Group> allGroup1 = restTemplate.getForObject(getAll, SourceParameterWrapperGroup.ListWrapper.class);
        assert (allGroup1.size() == 6);
        System.out.println(allGroup1);

    }

    @Test
    public void TestDelete() {

        addGroup();

        long id = 100L, groupIdn = 55;

        Group gr = new Group(groupIdn, "group55");
        ResponseEntity<Group> e = restTemplate.postForEntity(post, gr, Group.class);
        assert (e.getStatusCode() == HttpStatus.CREATED);
        System.out.println(e.getStatusCode());

        restTemplate.delete(del);

        List<Group> allGroup1 =  restTemplate.getForObject(getAll, SourceParameterWrapperGroup.ListWrapper.class);
        assert (allGroup1.size() == 5);
        System.out.println(allGroup1);

    }


    private void addGroup() {

        long id = 100L, groupIdn = 10;
        University un = new University(id, "Best university");

        for (long j = 0; j < 5; j++) {
            Group gr = new Group(groupIdn + j, "group" + j);
            gr.setUniversity(un);
            gr.setGroupId(50 + j);

            for (long i = j * 10; i < j * 10 + 10; i++) {
                Student s = new Student("name" + i, "surname" + i, new Date());
                s.setGroup(gr);
                gr.getListStudents().add(s);
            }
            un.getListGroup().add(gr);
        }
        service.saveUniversity(un);

        List<Student> allStudent = restTemplate.getForObject(url + "student/all", SourceParameterWrapperStudent.ListWrapper.class);
        assert (allStudent.size() == 50);
        System.out.println(allStudent);

    }


}
