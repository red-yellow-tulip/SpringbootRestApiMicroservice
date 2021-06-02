package base.datasource.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users"  , schema = "university_schemas")
public class RemoteUser {

    @Getter @Setter
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="user_seq", sequenceName="university_schemas.user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

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

    public RemoteUser() {

    }

    public RemoteUser(String login, String password, String role, String name) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
    }
}