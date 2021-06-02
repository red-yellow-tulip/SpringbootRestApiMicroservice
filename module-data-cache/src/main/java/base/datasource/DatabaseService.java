package base.datasource;

import base.datasource.entity.Group;
import base.datasource.entity.Student;
import base.datasource.entity.University;
import base.datasource.entity.jpa.GroupRepository;
import base.datasource.entity.jpa.StudentRepository;
import base.datasource.entity.jpa.UniversityRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    private final long universityId = 100;// гвозди


    //------------------------------------------------------------------------------------------------------------------
    //University
    @Transactional (propagation = Propagation.REQUIRED )
    public University saveUniversity(University un) {  return universityRepository.save(un);  }

    @Transactional (propagation = Propagation.REQUIRED )
    public List<University> findAllUniversity() {
        return universityRepository.findAll();
    }

    @Transactional (readOnly = true)
    public Optional<University> findUniversityById(long l) { return universityRepository.findByUniversityId(l);  }

    @Transactional (readOnly = true)
    public List<Group> findListGroupByUniversityId(long id) {
        Optional<University> op = universityRepository.findByUniversityId(id);
        if (op.isEmpty())
            return  new ArrayList<Group>();
        return new ArrayList<>(op.get().getListGroup());
    }

    //------------------------------------------------------------------------------------------------------------------
    //Group
    @Transactional (readOnly = true)
    public ArrayList<Student> findStudentByGroupId(long groupId) {
        Optional<Group> op = groupRepository.findByGroupId(groupId);
        if (op.isEmpty())
            return new ArrayList<Student>();
        return new ArrayList<>(op.get().getListStudents());
    }

    @Transactional (propagation = Propagation.REQUIRED )
    public boolean addStudentToGroupByGroupId(Student s, long groupId) {
        Optional<Group> opg = groupRepository.findByGroupId(groupId);
        if (opg.isEmpty())
            return  false;

        Group g = opg.get();
        s.setGroupByGroupObject(g);
        g.getListStudents().add(s);
        groupRepository.save(g);
        return true;
    }

    @Transactional (readOnly = true)
    public boolean isExistsGroupById(long groupId) {
        return groupRepository.findByGroupId(groupId).isPresent();
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
    public Optional<Group> findGroupById(long groupId) {
        return groupRepository.findByGroupId(groupId);
    }

    @Transactional (propagation = Propagation.REQUIRED )
    public boolean addNewGroup(Group gr) {

        Optional<University> op = universityRepository.findByUniversityId(universityId);
        if (op.isEmpty())
            return  false;

        University un = op.get();
        gr.setUniversity(un);
        un.getListGroup().add(gr);
        universityRepository.save(un);
        return true;
    }

    @Transactional (propagation = Propagation.REQUIRED )
    public boolean deleteGroup(long groupId) {
        Optional<Group> op = groupRepository.findByGroupId(groupId);
        if (op.isEmpty())
            return true;

        Group g = op.get();
        Optional<University> opu = universityRepository.findByUniversityId(universityId);
        if (opu.isEmpty())
            return false;

        University un = opu.get();
        un.getListGroup().remove(g);
        universityRepository.save(un);
        groupRepository.delete(g);
        return true;
    }

    @Transactional (readOnly = true)
    public int getCountStudentByGroupId(long groupId) {
        return findStudentByGroupId(groupId).size();
    }

    //------------------------------------------------------------------------------------------------------------------
    //Student
    @Transactional (readOnly = true)
    @Cacheable(value = "Student.id", key = "#id")
    public Optional<Student> findStudentById(long id) {
        return studentRepository.findById(id);
    }

    @Cacheable(value = "Student.flag", key = "#name+#surname")
    @Transactional (readOnly = true)
    public boolean isExistsStudent(String name,String surname) {
        return  studentRepository.findByNameAndSurname(name,surname).isPresent();
    }
    @Cacheable(value = "Student.fio", key = "#name+#surname")
    @Transactional (readOnly = true)
    public Optional<Student> findStudentByNameSurName(String name, String surname) {
        return studentRepository.findByNameAndSurname(name,surname);
    }

    @Transactional (readOnly = true)
    public List<Student> findAllStudent() {
        return studentRepository.findAll();
    }

    @Transactional (readOnly = true)
    public List<Student> findAllStudentByNameLikeOrSurnameLike(String name, String sname) {
        return studentRepository.findAllByNameLikeAndSurnameLike("%"+name+"%","%"+sname+"%");
    }

    @Transactional (propagation = Propagation.REQUIRED )
    @CachePut(value = "Student.fio", key = "#newStudent.name+#newStudent.surname")// обновить данные в кеше согласно return
    public Optional<Student> updateStudentId(long studentId, Student newStudent) {

        Optional<Student> ops = studentRepository.findById(studentId);
        if (ops.isEmpty())
            return  Optional.empty();
        Student sOld = ops.get().updateParam(newStudent);

        if (!sOld.getGroupId().equals(newStudent.getGroupId())){
            Optional<Group> opg = groupRepository.findById(newStudent.getGroupId());
            if (opg.isEmpty())
                return  Optional.empty();
            sOld.setGroup(opg.get());
        }
        studentRepository.save(sOld);
        return Optional.of(studentRepository.save(sOld));
    }

    @Transactional (propagation = Propagation.REQUIRED )
    @CacheEvict(value = "Student.fio", key = "#name+#surname")
    public boolean deleteStudent(String name, String surname) {

        Optional<Student> ops =  studentRepository.findByNameAndSurname(name,surname);
        if (ops.isEmpty())
            return  false;
        Student s = ops.get();

        Optional<Group> opg = groupRepository.findById(s.getGroupId());
        if (opg.isEmpty())
            return  false;

        Group g = opg.get();
        g.getListStudents().remove(s);

        groupRepository.save(g);
        studentRepository.delete(s);
        return true;
    }

    //DemoData
    public void createDemoData(int groupCount,int studentCount) {

        clearTable();

        long id = 100L;
        University un = new University(id, "Best university");

        for (long j = 0; j < groupCount; j++) {
            Group gr = new Group(groupCount + j, "group" + j);
            gr.setUniversity(un);
            gr.setGroupId(50 + j);

            for (long i = j * studentCount; i < j * studentCount + studentCount; i++) {
                Student s = new Student("name" + i, "surname" + i, new Date());
                s.setGroup(gr);
                gr.getListStudents().add(s);
            }
            un.getListGroup().add(gr);
        }
        saveUniversity(un);
    }

    @Transactional (propagation = Propagation.REQUIRED )
    public void clearTable() {
        universityRepository.deleteAll();
        groupRepository.deleteAll();
        universityRepository.deleteAll();
    }

}