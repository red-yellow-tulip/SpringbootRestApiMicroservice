package app.controller;

import app.config.RedisService;
import app.config.entity.Record;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class RecordController {

    @Autowired
    protected RedisService redisService;

    // RequestMethod.GET
    // http://localhost:8080/person/all
    @ApiOperation(value = "Поиск всех person-record", notes = "method: RecordController.findAllPerson")
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<Record>> findAllPerson() {

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(redisService.findAllPersonRecord());
    }

    // RequestMethod.POST
    // http://localhost:8080/person/add  + object
    @ApiOperation(value = "Добавление new person-record ", notes = "method: RecordController.savePerson")
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Record> savePerson(@ApiParam(value = "обьект person") @RequestBody @Valid Record record) {

        if (record == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        Record newRecordOp = redisService.savePersonRecord(record);
        if (newRecordOp == null)
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(newRecordOp, new HttpHeaders(), HttpStatus.CREATED);

    }

    // RequestMethod.GET
    // http://localhost:8080/person?id=10
    @ApiOperation(value = "Поиск person по id", notes = "method: RecordController.getPersonById")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },  params = {"id"})
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Record> getPersonById(@ApiParam(value = "id person") @RequestParam(value="id", required=false, defaultValue="")  String personId) {


        Optional<Record> op = redisService.findPersonRecord(personId);
        if (op.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(op.get());
    }



    // RequestMethod.DELETE
    // http://localhost:8080/person/delete?id=10
    @ApiOperation(value = "Удаление записи person ", notes = "method: RecordController.deletePerson")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },  params = {"id"})
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Record> deletePerson(@ApiParam(value = "id группы") @RequestParam(value="id", required=false, defaultValue="")  String id) {


        Optional<Record> op = redisService.findPersonRecord(id);
        if (op.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        redisService.deletePersonRecord(op.get());
        return new ResponseEntity<>(null,new HttpHeaders(), HttpStatus.OK);
    }

    // RequestMethod.DELETE
    // http://localhost:8080/person/delete?id=10
    @ApiOperation(value = "Удаление записи person ", notes = "method: RecordController.deleteAllPerson")
    @RequestMapping(value = "delete/all", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Record> deleteAllPerson() {

        redisService.deleteAllPersonRecord();
        return new ResponseEntity<>(null,new HttpHeaders(), HttpStatus.OK);
    }




}
