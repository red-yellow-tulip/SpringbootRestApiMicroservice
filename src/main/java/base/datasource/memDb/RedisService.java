package base.datasource.memDb;

import base.datasource.memDb.entity.PersonRecord;
import base.datasource.memDb.entity.PersonRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class RedisService {

    @Autowired
    private PersonRecordRepository personRecordRepository;

    public Optional<PersonRecord> findPersonRecord(long id){
        return  personRecordRepository.findById(String.valueOf(id));
    }

    public PersonRecord savePersonRecord(PersonRecord p){
        return personRecordRepository.save(p);
    }

    public List<PersonRecord> findAllPersonRecord(){
        List<PersonRecord> list = new ArrayList<>();
        Iterator<PersonRecord> it = personRecordRepository.findAll().iterator();
        while (it.hasNext())
            list.add(it.next());
        return list;
    }

    public void deletePersonRecord(PersonRecord p){
        personRecordRepository.delete(p);

    }

}
