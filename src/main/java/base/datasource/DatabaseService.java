package base.datasource;

import base.datasource.entity.Group;
import base.datasource.entity.Student;
import base.datasource.entity.University;
import base.datasource.entity.UserDb;
import base.datasource.entity.jpa.GroupRepository;
import base.datasource.entity.jpa.StudentRepository;
import base.datasource.entity.jpa.UniversityRepository;
import base.datasource.entity.jpa.UserRepository;
import base.utils.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DatabaseService {

    @Resource  // таблица - student
    private StudentRepository studentRepository;

    @Resource  // таблица - group
    private GroupRepository groupRepository;

    @Resource  // таблица - university
    private UniversityRepository universityRepository;

    @Resource  // таблица - user
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private final long universityId = 100;

    @Transactional (propagation = Propagation.REQUIRED )
    public University saveUniversity(University un) {
        return universityRepository.save(un);
    }


    @Transactional (propagation = Propagation.REQUIRED )
    public void clearTable() {
        universityRepository.deleteAll();
        groupRepository.deleteAll();
        universityRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Transactional (readOnly = true)
    public University findUniversityById(long l) {
        return universityRepository.findByUniversityId(l).orElse(null);
    }

    @Transactional (readOnly = true)
    public List<Group> findListGroupByUniversityId(long id) {
        Optional<University> u = universityRepository.findByUniversityId(id);
        return new ArrayList<>(u.get().getListGroup());
    }

    @Transactional (readOnly = true)
    public ArrayList<Student> findStudentByGroupId(long groupId) {
        Optional<Group> g = groupRepository.findByGroupId(groupId);
        Group g0 = g.orElse(null);
        return g0 == null ? new ArrayList<>(): new ArrayList<>(g0.getListStudents());
    }
    @Transactional (propagation = Propagation.REQUIRED )
    public Student addStudentByGroupId(Student s, long groupId) {
        Group g = groupRepository.findByGroupId(groupId).orElse(null);
        s.setGroupId(g.getGroupId());
        s.setGroup(g);

        g.getListStudents().add(s);
        groupRepository.save(g);

        Optional<Student> op = studentRepository.findByNameAndSurname(s.getName(),s.getSurname());
        return  op.orElse(null);

    }

    @Transactional (readOnly = true)
    public Student getStudentByNameAndSurname(String n,String sn) {
        Optional<Student> op = studentRepository.findByNameAndSurname(n,sn);
        return  op.orElse(null);
    }

    @Transactional (readOnly = true)
    public boolean isExistsStudent(String n,String sn) {
        Optional<Student> op = studentRepository.findByNameAndSurname(n,sn);
        return  op.isPresent();
    }

    @Transactional (readOnly = true)
    public Student findStudentByNameSurName(String n, String sn) {
        Optional<Student> op = studentRepository.findByNameAndSurname(n,sn);
        return op.get();
    }

    @Transactional (readOnly = true)
    public boolean isExistsStudent(long groupId) {
        Optional<Student> op = studentRepository.findById(groupId);
        return  op.isPresent();
    }

    @Transactional (readOnly = true)
    public List<Student> findAllStudent() {
        return studentRepository.findAll();
    }

    @Transactional (readOnly = true)
    public List<Student> findAllStudentByNameLikeOrSurnameLike(String name, String sname) {

        //return studentRepository.findAllByNameLikeOrSurnameLike(name,sname);
        return studentRepository.findAllByNameLikeAndSurnameLike("%"+name+"%","%"+sname+"%");

    }
    @Transactional (readOnly = true)
    public boolean isExistsGroupById(long groupId) {
        Optional<Group> gr = groupRepository.findByGroupId(groupId);
        return gr.isPresent();
    }

    @Transactional (propagation = Propagation.REQUIRED )
    public void deleteStudent(String name, String sname) {

        Student s = studentRepository.findByNameAndSurname(name,sname).get();

        Group g = groupRepository.findById(s.getGroupId()).get();
        if (g.getListStudents().contains(s)){
            g.getListStudents().remove(s);
        }
        groupRepository.save(g);
        studentRepository.delete(s);
    }

    @Transactional (readOnly = true)
    public  List<Group>  findAllGroup() {
        return new ArrayList<>(groupRepository.findAll());
    }

    @Transactional (readOnly = true)
    public List<Group> findAllGroupByName(String groupName) {
        return groupRepository.findAllByGroupNameLike("%"+groupName+"%");
    }

    @Transactional (readOnly = true)
    public Group findGroupById(long groupId) {
        Optional<Group> g = groupRepository.findByGroupId(groupId);
        return g.orElse(null);
    }

    @Transactional (propagation = Propagation.REQUIRED )
    public Group addNewGroup(Group gr) {

        if (universityRepository.findAll().size() == 0){
            createRandomUniversity();
        }

        University un = universityRepository.findByUniversityId(universityId).get();
        gr.setUniversity(un);
        un.getListGroup().add(gr);
        universityRepository.save(un);

        Optional<Group> n = groupRepository.findByGroupName(gr.getGroupName());
        return n.orElse(null);
    }

    @Transactional (propagation = Propagation.REQUIRED )
    public void deleteGroup(long groupId) {
        Group g = groupRepository.findByGroupId(groupId).get();
        University un = universityRepository.findByUniversityId(universityId).get();

        if (un.getListGroup().contains(g))
            un.getListGroup().remove(g);
        universityRepository.save(un);
        groupRepository.delete(g);
    }

    @Transactional (readOnly = true)
    public int getCountStudentByGroupId(long groupId) {
        List<Student> l = findStudentByGroupId(groupId);
        return l.size();
    }

    @Transactional (readOnly = true)
    public Student findStudentById(long groupId) {
        return studentRepository.findById(groupId).orElse(null);
    }


    @Transactional (propagation = Propagation.REQUIRED )
    public Student updateStudentId(long studentId, Student newStudent) {

        Student sOld = studentRepository.findById(studentId).get();
        sOld.setName(newStudent.getName());
        sOld.setSurname(newStudent.getSurname());
        sOld.setDateBirth(newStudent.getDateBirth());
        studentRepository.save(sOld);

        if (sOld.getGroupId() != newStudent.getGroupId()){
            Group rg1 = groupRepository.findById(sOld.getGroupId()).get();
            Group rg2 = groupRepository.findById(newStudent.getGroupId()).get();

          /*  rg1.getListStudents().remove(sOld);
            rg2.getListStudents().add(sOld);*/
            sOld.setGroup(rg2);

         /*   groupRepository.save(rg1);
            groupRepository.save(rg2);
            studentRepository.save(sOld);*/

        }
        return studentRepository.findById(studentId).get();
    }

    @Transactional (propagation = Propagation.REQUIRED )
    void createRandomUniversity() {

        University un = new University(universityId,"Bestuniversity_random");
        universityRepository.save(un);
    }

    @Transactional (propagation = Propagation.REQUIRED )
    public List<University> findAllUniversity() {
        return universityRepository.findAll();
    }

    @Transactional (propagation = Propagation.REQUIRED )
    public UserDb AddUsers(UserDb u){
        u.setPassword(customUserDetailsService.encode(u.getPassword()));
        //u.setPassword(u.getPassword());
        return userRepository.save(u);
    }

    @Transactional (readOnly = true)
    public UserDb findUserByLogin(String userName) {
        return userRepository.findByLogin(userName).get();
    }


    public void createDemoData() {

        clearTable();

        AddUsers(new UserDb("USER1","pswd","USER", "userName"));
        AddUsers(new UserDb("USER2","pswd","USER","userName"));
        AddUsers(new UserDb("ADMIN","pswd","ADMIN","userName"));

        long id = 100L, groupIdn = 10;
        University un = new University(id, "Best university");

        for (long j = 0; j < 5; j++) {
            Group gr = new Group(groupIdn + j, "group" + j);
            gr.setUniversity(un);
            gr.setGroupId(50 + j);

            for (long i = j * 10; i < j * 10 + 10; i++) {
                Student s = new Student("name" + i, "surname" + i, new Date());
                s.setGroup(gr);
                gr.getListStudents().add(s);
            }
            un.getListGroup().add(gr);
        }
        saveUniversity(un);
    }
}