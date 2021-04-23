package rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import rest.helper.BaseTestHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ActiveProfiles("test")
public class TestActuatorService extends BaseTestHelper {

    private final String endpoint = "http://localhost:%s/actuator/";
    private final String health = endpoint + "health";
    private final String info = endpoint + "info";
    private final String customServiceActuator = endpoint + "health/customServiceActuator"; // реализация своего индиктора
    private final String beans = endpoint + "beans";
    private final String env = endpoint + "env";

    @BeforeEach
    public void testBefore() {
        assertNotNull(service);
        service.clearTable();
        service.createDemoData();
    }


    @Test
    public void TestHealth() throws JSONException {

        ResponseEntity<String> healthResult = restTemplateIncognito.getForEntity(String.format(health, port), String.class);
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

        ResponseEntity<String> infoResult = restTemplateIncognito.getForEntity(String.format(info, port), String.class);
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

        ResponseEntity<String> customServiceIndicatorResult = restTemplateIncognito.getForEntity(String.format(customServiceActuator, port), String.class);
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

        ResponseEntity<String> beansResult = restTemplateIncognito.getForEntity(String.format(beans, port), String.class);
        assertNotNull(beansResult); // перечисление всех бинов
        assertEquals(beansResult.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void TestEnv() {

        ResponseEntity<String> envResult = restTemplateIncognito.getForEntity(String.format(env, port), String.class);
        assertNotNull(envResult);
        assertEquals(envResult.getStatusCode(), HttpStatus.OK);
    }

}
