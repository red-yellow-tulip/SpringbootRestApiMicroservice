package base.datasource.memDb.entity;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.UUID;

@RedisHash("person-record")
public class PersonRecord implements Serializable {

    @Id
    @Indexed
    @Setter @Getter
    private String id;

    @Setter @Getter
    private String name;

    @Setter @Getter
    private int grade;

    public PersonRecord() {
    }

    public PersonRecord(String name, int grade) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.grade = grade;
    }
}
