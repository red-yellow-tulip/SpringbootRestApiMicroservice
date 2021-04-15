package rest;

import base.StudentMicroserviceRunner;
import base.datasource.DatabaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = StudentMicroserviceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TestActuatorService {

    @LocalServerPort
    String port;

    TestRestTemplate restTemplate = new TestRestTemplate("ADMIN", "pswd");
    @Resource
    private DatabaseService service;

    private final String endpoint = "http://localhost:%s/actuator/";
    private final String health = endpoint + "health";
    private final String info = endpoint + "info";
    private final String customServiceActuator = endpoint + "health/customServiceActuator"; // реализация своего индиктора
    private final String beans = endpoint + "beans";
    private final String env = endpoint + "env";


    @BeforeEach
    public void testBefore() {
        assertNotNull(service);
        service.createDemoData();
    }

    @AfterEach
    public void testAfter() {
        assertNotNull(service);
        service.clearTable();
    }

    @Test
    public void TestHealth() throws JSONException {

        ResponseEntity<String> healthResult = restTemplate.getForEntity(String.format(health, port), String.class);
        assertNotNull(healthResult);

          /*  {
            "components":{
            "diskSpace":{
                "details":{
                    "total":498634248192, "exists":true, "threshold":10485760, "free":124663910400
                },"status":"UP"
            },"ping":{
                "status":"UP"
            },"db":{
                "details":{
                    "database":"PostgreSQL", "validationQuery":"isValid()"
                },"status":"UP"
            },"customServiceActuator":{
                "details":{
                    "Student count = 50":0
                },"status":"UP"
            }
        },"status":"UP"
        }*/

        JSONObject root = new JSONObject(healthResult.getBody());
        assertNotNull(root);
        assertEquals(root.getString("status"), "UP");

        JSONObject components = (JSONObject) root.get("components");
        JSONArray keys = components.names();
        for (int i = 0; i < keys.length(); i++)
            assertEquals(components.getJSONObject((String) keys.get(i)).getString("status"), "UP");
    }

    @Test
    public void TestInfo() throws JSONException {

        ResponseEntity<String> infoResult = restTemplate.getForEntity(String.format(info, port), String.class);
        assertNotNull(infoResult);

          /*  {
            "build":{
            "encoding":{
                "reporting":"UTF-8",
                 "source":"UTF-8"
            },
            "version":"1.0",
            "artifact":"StudentMicroService_boot",
             "java":{
                "source":"1.8", "target":"1.8"
             },
            "name":"StudentMicroService_boot",
            "time":"2021-04-15T12:43:37.230Z",
            "group":"groupId"
        }
        }*/

        JSONObject root = new JSONObject(infoResult.getBody());
        assertNotNull(root);
        assertEquals(root.getJSONObject("build").getString("artifact"), "StudentMicroService_boot");
        assertEquals(root.getJSONObject("build").getString("group"), "groupId");
    }

    @Test
    public void TestCustomServiceActuator() throws JSONException {

        ResponseEntity<String> customServiceIndicatorResult = restTemplate.getForEntity(String.format(customServiceActuator, port), String.class);
        assertNotNull(customServiceIndicatorResult);
        /*
         {
            "status":"UP",
             "details":{
                "Student count = 50":0
            }
        }*/
        JSONObject root = new JSONObject(customServiceIndicatorResult.getBody());
        assertNotNull(root);

        assertEquals(root.get("status"), "UP");
        assertEquals(root.getJSONObject("details").get("Student count = ").toString(), "50");
    }

    @Test
    public void TestBean() {

        ResponseEntity<String> beansResult = restTemplate.getForEntity(String.format(beans, port), String.class);
        assertNotNull(beansResult); // перечисление всех бинов
        assertEquals(beansResult.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void TestEnv() {

        ResponseEntity<String> envResult = restTemplate.getForEntity(String.format(env, port), String.class);
        assertNotNull(envResult);
        assertEquals(envResult.getStatusCode(), HttpStatus.OK);
    }

}
