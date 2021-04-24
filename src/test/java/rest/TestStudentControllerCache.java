package rest;

import base.datasource.entity.Student;
import base.web.config.SourceParameterWrapperStudent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import rest.helper.BaseTestHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
public class TestStudentControllerCache extends BaseTestHelper {

    private final String url =      "http://localhost:%s/";
    private final String getAll =   url+"student/all";

    private final int countGroup = 10;
    private final int countStudent = 100;

    @BeforeEach
    public void testBefore() {

        assertNotNull(service);
        service.clearTable();
        service.createDemoData(countGroup, countStudent);
    }

    @Test
    public void TestGetAll() throws InterruptedException {
        //port = "11095";

        // заполнение кеша
        List<Student> allStudent = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals( countGroup*countStudent,allStudent.size());

       // чтение данных - заполнение хеша   - то есть обращение к СУБД
        loggerService.log().info("executionTimeBefore starts...");
        long executionTimeBefore = executeGetObjects(allStudent);

        // чтение данных - чтение из хеша   - то есть БЕЗ обращение к СУБД
        loggerService.log().info("executionTimeReadCache starts...");
        long executionTimeReadCache = executeGetObjects(allStudent);

        TimeUnit.SECONDS.sleep(1); // соответствует преиоду хранения кеша

        //  чтение данных - чтение из хеша   - то есть БЕЗ обращение к СУБД
        loggerService.log().info("executionTimeAfter starts...");
        long executionTimeAfter =  executeGetObjects(allStudent);

        printInfo();
        loggerService.log().info("Заполнение кеша - " + executionTimeBefore + "мс");
        loggerService.log().info("@Cache чтение с кешем №1- " + executionTimeReadCache + "мс");
        loggerService.log().info("@Cache чтение с кешем №2- " + executionTimeAfter + "мс");

        /*
        @EnableCaching                              //@EnableCaching - отключено кеширование
        на примере 1000 записей  .stream().parallel() - запись и чтение через restApi
        intel i5-3210 mobility
        БД H2
        Заполнение кеша - 17736мс                   Заполнение кеша - 17097мс
        @Cache чтение с кешем №1- 10761мс           @Cache чтение с кешем №1- 13614мс
        @Cache чтение с кешем №2- 10499мс           @Cache чтение с кешем №2- 14255мс

        БД  Postgres
        Заполнение кеша - 16345мс                   Заполнение кеша - 17748мс
        @Cache чтение с кешем №1- 11548мс           @Cache чтение с кешем №1- 19457мс
        @Cache чтение с кешем №2- 11510мс           @Cache чтение с кешем №2- 15331мс
        */
    }

    private long executeGetObjects(List<Student> allStudent) {
        long start = System.currentTimeMillis();

        allStudent.stream().parallel().forEach( e-> {
            String filtrParam = String.format(url+"student/eq?name=%s&sname=%s",port,e.getName(),e.getSurname());
            Student student = restTemplate.getForObject(filtrParam, Student.class);
            assertNotNull(student);
            assertEquals( e.getId(),student.getId());
        });

        return System.currentTimeMillis() - start;

    }
}
