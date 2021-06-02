package app.config;

import app.config.entity.Record;
import app.config.entity.PersonRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RedisService {

    @Autowired
    private PersonRecordRepository personRecordRepository;


    public Optional<Record> findPersonRecord(String id){
        return  personRecordRepository.findById(id);
    }

    public Record savePersonRecord(Record p){
        return personRecordRepository.save(p);
    }

    public List<Record> findAllPersonRecord(){
        List<Record> list = new ArrayList<>();
        personRecordRepository.findAll().forEach(list::add);
        return list;
    }

    public void deletePersonRecord(Record p){
        personRecordRepository.delete(p);
    }

    public void deleteAllPersonRecord(){
        personRecordRepository.deleteAll();
    }


}
