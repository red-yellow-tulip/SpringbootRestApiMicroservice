package base.datasource.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users"  , schema = "university_schemas")
public class UserDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    long id;

    @Getter @Setter
    @Column (name = "login")
    private String login = "";

    @Getter @Setter
    @Column (name = "psw")
    private String password = "";

    @Getter @Setter
    @Column (name = "name")
    private String name = "";

    @Getter @Setter
    @Column (name = "role")
    private String role = "";

    public UserDb() {

    }

    public UserDb(String login, String password, String role, String name) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
    }
}