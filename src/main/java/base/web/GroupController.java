package base.web;

import base.datasource.entity.Group;
import base.utils.logging.LogExecutionTime;
import base.web.config.HandlerExceptionNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import base.datasource.DatabaseService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController extends  BaseController{

    // RequestMethod.GET
    // http://localhost:8080/group/all
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    @ExceptionHandler(value = HandlerExceptionNotFound.class)
    @LogExecutionTime
    public ResponseEntity<List<Group>> getAllProduct() {

        List<Group> listGroup = databaseService.findAllGroup();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listGroup);
    }

    // RequestMethod.GET
    // http://localhost:8080/group/filtr?name=name1
    @RequestMapping(value = "/filtr", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    @LogExecutionTime
    public ResponseEntity<List<Group>> getFilterProduct(@RequestParam(value="name", required=false, defaultValue="")  String groupName) {

        List<Group> listGroup  = databaseService.findAllGroupByName(groupName);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listGroup);
    }

    // RequestMethod.GET
    // http://localhost:8080/group?id=10
    @RequestMapping(value = "", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    @ExceptionHandler(value = HandlerExceptionNotFound.class)
    @LogExecutionTime
    public ResponseEntity<Group> getFilterProduct(@RequestParam(value="id", required=false, defaultValue="")  long groupId) {

        Group g  = databaseService.findGroupById(groupId);
        if (g == null) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(null);
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(g);
    }


    // RequestMethod.POST
    // http://localhost:8080/group/add  + object
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    @LogExecutionTime
    public ResponseEntity<Group> saveProduct(@RequestBody @Valid Group gr) {

        if (gr == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        if (databaseService.isExistsGroupById(gr.getGroupId()))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Group temp = databaseService.addNewGroup(gr);

        if (temp != null)
            return new ResponseEntity<>(temp, new HttpHeaders(), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(temp, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }



    // RequestMethod.DELETE
    // http://localhost:8080/group/delete?id=10
    @RequestMapping(value = "delete", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    @LogExecutionTime
    public ResponseEntity<Group> deleteProduct(       @RequestParam(value="id", required=false, defaultValue="")  long groupId) {

        if (!databaseService.isExistsGroupById(groupId))
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        if ( 0 != databaseService.getCountStudentByGroupId(groupId))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        databaseService.deleteGroup(groupId);
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
    }
}
