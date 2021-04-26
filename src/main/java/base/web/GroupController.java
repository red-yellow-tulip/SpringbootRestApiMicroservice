package base.web;

import base.datasource.sqlDb.entity.Group;
import base.utils.logging.LogExecutionTime;
import base.web.config.HandlerExceptionNotFound;
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

@RestController
@RequestMapping("/group")
public class GroupController extends  BaseController{

    // RequestMethod.GET
    // http://localhost:8080/group/all
    @ApiOperation(value = "Поиск всех group", notes = "method: GroupController.getAllGroup")
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    @ExceptionHandler(value = HandlerExceptionNotFound.class)
    @LogExecutionTime
    public ResponseEntity<List<Group>> getAllGroup() {

        List<Group> listGroup = databaseService.findAllGroup();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listGroup);
    }

    // RequestMethod.GET
    // http://localhost:8080/group/filtr?name=name1
    @ApiOperation(value = "Поиск всех group c фильтрцией по имени", notes = "method: GroupController.getFilterGroup")
    @RequestMapping(value = "/filtr", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } ,  params = {"name"})
    @ResponseBody
    @CrossOrigin
    @LogExecutionTime
    public ResponseEntity<List<Group>> getFilterGroup(@ApiParam(value = "название группы") @RequestParam(value="name", required=false, defaultValue="")  String groupName) {

        List<Group> listGroup  = databaseService.findAllGroupByName(groupName);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listGroup);
    }

    // RequestMethod.GET
    // http://localhost:8080/group?id=10
    @ApiOperation(value = "Поиск всех group c фильтрцией по id", notes = "method: GroupController.getFilterGroup")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },  params = {"id"})
    @ResponseBody
    @CrossOrigin
    @ExceptionHandler(value = HandlerExceptionNotFound.class)
    @LogExecutionTime
    public ResponseEntity<Group> getFilterGroup(@ApiParam(value = "id группы") @RequestParam(value="id", required=false, defaultValue="")  long groupId) {

        Optional<Group> op  = databaseService.findGroupById(groupId);
        if (op.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(op.get());
    }

    // RequestMethod.POST
    // http://localhost:8080/group/add  + object
    @ApiOperation(value = "Добавление new group ", notes = "method: GroupController.saveGroup")
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    @LogExecutionTime
    public ResponseEntity<Group> saveGroup(@ApiParam(value = " обьект группы") @RequestBody @Valid Group gr) {

        if (gr == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        if (databaseService.isExistsGroupById(gr.getGroupId()))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        if (databaseService.addNewGroup(gr))
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // RequestMethod.DELETE
    // http://localhost:8080/group/delete?id=10
    @ApiOperation(value = "Удаление записи о group ", notes = "method: GroupController.deleteGroup")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },  params = {"id"})
    @ResponseBody
    @CrossOrigin
    @LogExecutionTime
    public ResponseEntity<Group> deleteGroup(@ApiParam(value = "id группы") @RequestParam(value="id", required=false, defaultValue="")  long groupId) {

        if (!databaseService.isExistsGroupById(groupId))
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        if ( 0 != databaseService.getCountStudentByGroupId(groupId))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (databaseService.deleteGroup(groupId))
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
