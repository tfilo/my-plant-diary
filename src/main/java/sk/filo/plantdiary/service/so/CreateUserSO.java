package sk.filo.plantdiary.service.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(name = "CreateUser")
@Getter
@Setter
@ToString(exclude = {"password"})
public class CreateUserSO {

    @NotBlank
    @Size(min = 5, max = 25)
    private String username;

    @NotBlank
    @Email
    @Size(min = 5, max = 255)
    private String email;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(min = 8, max = 255)
    @NotBlank
    private String password;
}
