package base.datasource.memDb.entity;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("person-record")
public class PersonRecord implements Serializable {

    @Id
    @Setter @Getter
    private Long id;

    @Setter @Getter
    private String name;

    @Setter @Getter
    private int grade;


    public PersonRecord() {
    }

    public PersonRecord(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }
}
