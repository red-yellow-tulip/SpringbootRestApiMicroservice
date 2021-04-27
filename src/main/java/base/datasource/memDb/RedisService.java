package base.datasource.memDb;

import base.datasource.memDb.entity.PersonRecord;
import base.datasource.memDb.entity.PersonRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RedisService {

    @Autowired
    private PersonRecordRepository personRecordRepository;


    public Optional<PersonRecord> findPersonRecord(String id){
        return  personRecordRepository.findById(id);
    }

    public PersonRecord savePersonRecord(PersonRecord p){
        return personRecordRepository.save(p);
    }

    public List<PersonRecord> findAllPersonRecord(){
        List<PersonRecord> list = new ArrayList<>();
        personRecordRepository.findAll().forEach(list::add);
        return list;
    }

    public void deletePersonRecord(PersonRecord p){
        personRecordRepository.delete(p);
    }

    public void deleteAllPersonRecord(){
        personRecordRepository.deleteAll();
    }


}
