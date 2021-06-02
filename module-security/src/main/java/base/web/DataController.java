package base.web;

import base.datasource.DatabaseService;
import base.datasource.entity.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    protected DatabaseService databaseService;


    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<Data>> getAllData() {

        Data d = Data.builder().d1("d1").d2("d2").build();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(List.of(d));
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Data> getDataById(  @RequestParam(value="id", required=false, defaultValue="")  String id) {

        Data d = Data.builder().d1("d3").d2("d4").build();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(d);
    }

}
