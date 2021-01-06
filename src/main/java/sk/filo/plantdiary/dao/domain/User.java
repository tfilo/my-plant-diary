package sk.filo.plantdiary.dao.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString(exclude = "password")
@Entity
@Table(name = "pd_user")
public class User {

    @Id
    @Column(name = "username", nullable = false, length = 25)
    private String username;
    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;
    @Column(name = "first_name", nullable = true, length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = true, length = 50)
    private String lastName;
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    public User(String username, String email, String password, Boolean enabled) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public User() {
    }
}
