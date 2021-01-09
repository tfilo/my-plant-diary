package sk.filo.plantdiary.dao.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"password", "plants", "locations"})
@Entity
@Table(name = "pd_user")
public class User {

    public User(String username, String email, String password, Boolean enabled) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public User() {
    }

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
    @OneToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "user_activation_id", nullable = true)
    private UserActivation userActivation;

    // To ensure cascade delete
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = false
    )
    @JoinColumn(
            name = "owner_username",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private List<Plant> plants;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = false
    )
    @JoinColumn(
            name = "owner_username",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private List<Location> locations;
}
