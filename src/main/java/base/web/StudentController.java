package base.web;

import base.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import base.serviceDB.ServiceDatabase;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private ServiceDatabase serviceDatabase;

    // RequestMethod.GET
    // http://localhost:8080/student/all
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<Student>> getAllProduct() {

        List<Student> listStudent = new ArrayList<>(serviceDatabase.findAllStudent());
        //HttpHeaders headers = new HttpHeaders();
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //ResponseEntity<List<Student>> resp = new ResponseEntity<>(listStudent,headers, HttpStatus.OK);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listStudent);
    }

    // RequestMethod.GET
    // http://localhost:8080/student/filtr?name=name1&sname=surname1
    @RequestMapping(value = "/filtr", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<Student>> getFilterProduct(@RequestParam(value="name", required=false, defaultValue="")  String name,
                                                          @RequestParam(value="sname", required=false, defaultValue="")  String sname) {

        List<Student> listStudent  = serviceDatabase.findAllStudentByNameLikeOrSurnameLike(name,sname);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listStudent);
    }

    // RequestMethod.GET
    // http://localhost:8080/student?id=1000
    @RequestMapping(value = "", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Student> getProductById(@RequestParam(value="id", required=false, defaultValue="")  long groupId) {


        if (!serviceDatabase.isExistsStudent(groupId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Student stud  = serviceDatabase.findStudentById(groupId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(stud);
    }



    // RequestMethod.GET
    // http://localhost:8080/student/group?id=10
    @RequestMapping(value = "/group", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<List<Student>> getFilterProduct(@RequestParam(value="id", required=false, defaultValue="")  long groupId) {

        List<Student> listStudent  = serviceDatabase.findStudentByGroupId(groupId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listStudent);
    }

    // RequestMethod.POST
    // http://localhost:8080/student?id=11   + object
    @RequestMapping(value = "", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<Student> saveProduct(@RequestBody @Valid Student student,
                                               @RequestParam(value="id", required=false, defaultValue="")  long groupId) {

        if (!serviceDatabase.isExistsGroupById(groupId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (serviceDatabase.isExistsStudent(student.getName(),student.getSurname()))
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        if (student == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        Student temp = serviceDatabase.addStudentByGroupId(student, groupId);

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

    public ResponseEntity<Student> updateProduct(@RequestBody @Valid Student student,
                                                 @RequestParam(value="id", required=false, defaultValue="")  long studentIt) {

        if (student == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        if (serviceDatabase.isExistsStudent(student.getName(),student.getSurname())) {

            Student oldSt = serviceDatabase.findStudentByNameSurName(student.getName(),student.getSurname());
            if (oldSt.equals(student))
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

        if (studentIt != student.getId())
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);

        Student temp = serviceDatabase.updateStudentId(studentIt, student);

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

    public ResponseEntity<Student> deleteProduct(       @RequestParam(value="name", required=false, defaultValue="")  String name,
                                                        @RequestParam(value="sname", required=false, defaultValue="") String sname)  {

        if (!serviceDatabase.isExistsStudent(name,sname))
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        serviceDatabase.deleteStudent(name,sname);

        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
    }
}
