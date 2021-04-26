package base.web;

import base.datasource.memDb.entity.PersonRecord;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
public class RecordController extends  BaseController{

    // RequestMethod.GET
    // http://localhost:8080/person/all
    @ApiOperation(value = "Поиск всех person-record", notes = "method: RecordController.findAllPerson")
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<PersonRecord>> findAllPerson() {

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(redisService.findAllPersonRecord());
    }

    // RequestMethod.POST
    // http://localhost:8080/person/add  + object
    @ApiOperation(value = "Добавление new person-record ", notes = "method: RecordController.savePerson")
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<PersonRecord> savePerson(@ApiParam(value = "обьект person") @RequestBody @Valid PersonRecord personRecord) {

        if (personRecord == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        PersonRecord newRecordOp = redisService.savePersonRecord(personRecord);
        if (newRecordOp == null)
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(newRecordOp, new HttpHeaders(), HttpStatus.CREATED);

    }



}
