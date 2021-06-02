package base.web;

import base.datasource.entity.Student;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController  extends  BaseController{

    // RequestMethod.GET
    // http://localhost:8080/student/all
    @ApiOperation(value = "Поиск всех student", notes = "method: StudentController.getAllStudent")
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<Student>> getAllStudent() {

        List<Student> listStudent = new ArrayList<>(databaseService.findAllStudent());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listStudent);
    }

    @ApiOperation(value = "Поиск всех student с фильтрацией по имени и фамилии", notes = "method: StudentController.getFilterStudent" )
    // RequestMethod.GET
    // http://localhost:8080/student/filtr?name=name1&sname=surname1
    @RequestMapping(value = "/filtr", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },  params = {"name","sname"})
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<Student>> getFilterStudent( @ApiParam (value = "имя")     @RequestParam(value="name", required=false, defaultValue="")  String name,
                                                           @ApiParam (value = "фамилия") @RequestParam(value="sname", required=false, defaultValue="")  String sname) {

        List<Student> listStudent  = databaseService.findAllStudentByNameLikeOrSurnameLike(name,sname);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listStudent);
    }

    @ApiOperation(value = "Поиск всех student по имени и фамилии", notes = "method: StudentController.getFilterStudentEq" )
    // RequestMethod.GET
    // http://localhost:8080/student/eq?name=name1&sname=surname1
    @RequestMapping(value = "/eq", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },  params = {"name","sname"})
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Student> getFilterStudentEq( @ApiParam (value = "имя")     @RequestParam(value="name", required=false, defaultValue="")  String name,
                                                           @ApiParam (value = "фамилия") @RequestParam(value="sname", required=false, defaultValue="")  String sname) {

        Optional<Student> op  = databaseService.findStudentByNameSurName(name,sname);
        if (op.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(op.get());
    }

    // RequestMethod.GET
    // http://localhost:8080/student?id=1000
    @ApiOperation(value = "Поиск student по id", notes = "method: StudentController.getStudentById")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },  params = {"id"})
    @ResponseBody
    @CrossOrigin
        public ResponseEntity<Student> getStudentById(@ApiParam (value = "id student")   @RequestParam(value="id", required=false, defaultValue="")  long id) {

        Optional<Student> op  = databaseService.findStudentById(id);
        if (op.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(op.get());
    }

    // RequestMethod.GET
    // http://localhost:8080/student/group?id=10
    @ApiOperation(value = "Поиск student по group id", notes = "method: StudentController.getFilterStudent")
    @RequestMapping(value = "/group", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },  params = {"id"})
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<Student>> getFilterStudent(@ApiParam (value = "id student") @RequestParam(value="id", required=false, defaultValue="")  long groupId) {

        List<Student> listStudent  = databaseService.findStudentByGroupId(groupId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listStudent);
    }

    // RequestMethod.POST
    // http://localhost:8080/student?id=11   + object
    @ApiOperation(value = "Добавить new student", notes = "method: StudentController.saveStudent")
    @RequestMapping(value = "", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Student> saveStudent(@ApiParam (value = "student object") @RequestBody @Valid Student student,
                                               @ApiParam (value = "id student")  @RequestParam(value="id", required=false, defaultValue="")  long groupId) {

        if (!databaseService.isExistsGroupById(groupId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (databaseService.isExistsStudent(student.getName(),student.getSurname()))
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        boolean isAdded = databaseService.addStudentToGroupByGroupId(student, groupId);

        if (isAdded)
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // RequestMethod.PUT
    // http://localhost:8080/student?id=11   + object
    @ApiOperation(value = "Обновить состояние  student", notes = "method: StudentController.updateStudent")
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, params = {"student","id"})
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Student> updateStudent(@ApiParam (value = "student object") @RequestBody @Valid Student student,
                                                 @ApiParam (value = "id student")  @RequestParam(value="id", required=false, defaultValue="")  long studentIt) {

        if (student == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        Optional<Student> op = databaseService.findStudentByNameSurName(student.getName(),student.getSurname());
        if (op.isPresent() && op.get().equals(student))
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        if (studentIt != student.getId())
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);

        if (databaseService.updateStudentId(studentIt, student).isPresent())
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // RequestMethod.DELETE
    // http://localhost:8080/student/delete?name=name1&sname=surname1
    @ApiOperation(value = "Удалить запись о student", notes = "method: StudentController.deleteStudent")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, params = {"name","sname"})
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Student> deleteStudent( @ApiParam (value = "имя") @RequestParam(value="name", required=false, defaultValue="")  String name,
                                                  @ApiParam (value = "фамилия")  @RequestParam(value="sname", required=false, defaultValue="") String sname)  {

        if (!databaseService.isExistsStudent(name,sname))
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        databaseService.deleteStudent(name,sname);

        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
    }
}
