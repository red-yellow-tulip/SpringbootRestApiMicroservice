package rest.helper;

import base.StudentMicroserviceRunner;
import base.datasource.DatabaseService;
import base.utils.logging.LoggerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import javax.annotation.Resource;

@SpringBootTest(classes = StudentMicroserviceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(value = BeforeAllCalllbackLiquebase.class)
public abstract class BaseTestHelper {

    @Resource
    protected LoggerService loggerService;

    @LocalServerPort
    protected String port;

    protected TestRestTemplate restTemplate = new TestRestTemplate("ADMIN", "pswd");

    protected TestRestTemplate restTemplateIncognito = new TestRestTemplate();

    @Resource
    protected DatabaseService service;

    @Autowired
    private StudentMicroserviceRunner studentMicroserviceRunner;
}


