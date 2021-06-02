import base.CacheApplicationRunner;
import base.datasource.DatabaseService;
import base.datasource.entity.Student;
import base.datasource.utils.SourceParameterWrapperStudent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@ActiveProfiles("test.h2")
@SpringBootTest(classes = CacheApplicationRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestStudentControllerCache{

    @Resource
    private DatabaseService service;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private static final Logger log = LogManager.getLogger(TestStudentControllerCache.class.getName());

    private final String url =      "http://localhost:%s/";
    private final String getAll =   url+"student/all";

    @LocalServerPort
    protected String port;

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
        log.info("executionTimeBefore starts...");
        long executionTimeBefore = executeGetObjects(allStudent);

        // чтение данных - чтение из хеша   - то есть БЕЗ обращение к СУБД
        log.info("executionTimeReadCache starts...");
        long executionTimeReadCache = executeGetObjects(allStudent);

        TimeUnit.SECONDS.sleep(1); // соответствует преиоду хранения кеша

        //  чтение данных - чтение из хеша   - то есть БЕЗ обращение к СУБД
        log.info("executionTimeAfter starts...");
        long executionTimeAfter =  executeGetObjects(allStudent);

        printInfo();
        log.info("Заполнение кеша - " + executionTimeBefore + "мс");
        log.info("@Cache чтение с кешем №1- " + executionTimeReadCache + "мс");
        log.info("@Cache чтение с кешем №2- " + executionTimeAfter + "мс");

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

    public void printInfo() {
        log.info("Start test for db:" + url);
    }
}
