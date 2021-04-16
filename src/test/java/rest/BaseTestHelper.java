package rest;

import base.StudentMicroserviceRunner;
import base.datasource.DatabaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import javax.annotation.Resource;

@SpringBootTest(classes = StudentMicroserviceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTestHelper {

    protected static final Logger log = LogManager.getLogger(BaseTestHelper.class.getName());

    @LocalServerPort
    String port;

    protected TestRestTemplate restTemplate = new TestRestTemplate("ADMIN", "pswd");
    @Resource
    protected DatabaseService service;

    @Autowired
    private  StudentMicroserviceRunner studentMicroserviceRunner;

}
