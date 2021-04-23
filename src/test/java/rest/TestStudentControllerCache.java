package rest;

import base.datasource.entity.Group;
import base.datasource.entity.Student;
import base.datasource.entity.University;
import base.web.config.SourceParameterWrapperStudent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import rest.helper.BaseTestHelper;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
public class TestStudentControllerCache extends BaseTestHelper {

    private final String url =      "http://localhost:%s/";
    private final String getAll =   url+"student/all";
    private final String filtr =    url+"student/filtr?name=nam&sname=surnam";
    private final String filtr1 =   url+"student/filtr?name=name51&sname=surname51";
    private final String groupId =  url+"student/group?id=54";
    private final String post =     url+"student?id=54";
    private final String del =      url+"student/delete?name=name40&sname=surname40";

    private final int countGroup = 10;
    private final int countStudent = 10;

    @BeforeEach
    public void testBefore() {
        assertNotNull(service);
        service.clearTable();
        service.createDemoData(countGroup, countStudent);
    }

    @Test
    public void TestGetAll() throws InterruptedException {


        // заполнение кеша
        List<Student> allStudent = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals( countGroup*countStudent,allStudent.size());

       // чтение хешированных данных
        Long executionTime = executeGetObjects(allStudent);

        TimeUnit.SECONDS.sleep(2);

        // чтение данных с истекшим сроком хранения
        Long executionTimeAfter =  executeGetObjects(allStudent);

        loggerService.log().info("@Cache выполнен за " + executionTime + "мс");
        loggerService.log().info("Без @Cache выполнен за " + executionTimeAfter + "мс");



    }

    private long executeGetObjects(List<Student> allStudent) {
        long start = System.currentTimeMillis();

        allStudent.stream().forEach( e-> {
            String filtrParam = String.format(url+"student/eq?name=%s&sname=%s",port,e.getName(),e.getSurname());
            Student student = restTemplate.getForObject(filtrParam, Student.class);
            assertNotNull(student);
            assertEquals( e.getId(),student.getId());
        });

        return System.currentTimeMillis() - start;

    }


}
