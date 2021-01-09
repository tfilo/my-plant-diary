package sk.filo.plantdiary.dao.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString(exclude = {"token"})
@Entity
@Table(name = "pd_user_activation")
@SequenceGenerator(name = "user_activation_generator", allocationSize = 1, sequenceName = "pd_user_activation_seq")
public class UserActivation {

    public UserActivation(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public UserActivation() {}

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "user_activation_generator")
    private Long id;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "activation_token", nullable = false, length = 255)
    private String token;

}
