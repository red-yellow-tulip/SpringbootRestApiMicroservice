package base.web;

import base.datasource.entity.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController  extends  BaseController{

    // RequestMethod.GET
    // http://localhost:8080/student/all
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    /*@Operation(summary = "Gets all student", tags = "student")
    @ApiResponses(value = { @ApiResponse(responseCode = "200",description = "Found the student",
                            content = {@Content(mediaType = "application/json",
                                       array = @ArraySchema(schema = @Schema(implementation = Student.class)))    })  })*/
    public ResponseEntity<List<Student>> getAllStudent() {

        List<Student> listStudent = new ArrayList<>(databaseService.findAllStudent());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listStudent);
    }

    // RequestMethod.GET
    // http://localhost:8080/student/filtr?name=name1&sname=surname1
    @RequestMapping(value = "/filtr", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<Student>> getFilterStudent(@RequestParam(value="name", required=false, defaultValue="")  String name,
                                                          @RequestParam(value="sname", required=false, defaultValue="")  String sname) {

        List<Student> listStudent  = databaseService.findAllStudentByNameLikeOrSurnameLike(name,sname);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listStudent);
    }

    // RequestMethod.GET
    // http://localhost:8080/student?id=1000
    @RequestMapping(value = "", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Student> getStudentById(@RequestParam(value="id", required=false, defaultValue="")  long groupId) {


        if (!databaseService.isExistsStudent(groupId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Student stud  = databaseService.findStudentById(groupId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(stud);
    }

    // RequestMethod.GET
    // http://localhost:8080/student/group?id=10
    @RequestMapping(value = "/group", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<Student>> getFilterStudent(@RequestParam(value="id", required=false, defaultValue="")  long groupId) {

        List<Student> listStudent  = databaseService.findStudentByGroupId(groupId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listStudent);
    }

    // RequestMethod.POST
    // http://localhost:8080/student?id=11   + object
    @RequestMapping(value = "", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Student> saveStudent(@RequestBody @Valid Student student,
                                               @RequestParam(value="id", required=false, defaultValue="")  long groupId) {

        if (!databaseService.isExistsGroupById(groupId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (databaseService.isExistsStudent(student.getName(),student.getSurname()))
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        Student temp = databaseService.addStudentByGroupId(student, groupId);

        if (temp != null)
            return new ResponseEntity<>(temp, new HttpHeaders(), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(temp, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // RequestMethod.PUT
    // http://localhost:8080/student?id=11   + object
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Student> updateStudent(@RequestBody @Valid Student student,
                                                 @RequestParam(value="id", required=false, defaultValue="")  long studentIt) {

        if (student == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        if (databaseService.isExistsStudent(student.getName(),student.getSurname())) {

            Student oldSt = databaseService.findStudentByNameSurName(student.getName(),student.getSurname());
            if (oldSt.equals(student))
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

        if (studentIt != student.getId())
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);

        Student temp = databaseService.updateStudentId(studentIt, student);

        if (temp != null)
            return new ResponseEntity<>(temp, new HttpHeaders(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // RequestMethod.DELETE
    // http://localhost:8080/student/delete?name=name1&sname=surname1
    @RequestMapping(value = "delete", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Student> deleteStudent(       @RequestParam(value="name", required=false, defaultValue="")  String name,
                                                        @RequestParam(value="sname", required=false, defaultValue="") String sname)  {

        if (!databaseService.isExistsStudent(name,sname))
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        databaseService.deleteStudent(name,sname);

        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
    }
}
