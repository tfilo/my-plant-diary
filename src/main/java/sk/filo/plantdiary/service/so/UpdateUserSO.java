package sk.filo.plantdiary.service.so;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString(exclude = {"password"})
public class UpdateUserSO {

    @NotBlank
    @Email
    @Size(min = 5, max = 255)
    private String email;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(min = 8, max = 255)
    private String oldPassword;

    @Size(min = 8, max = 255)
    private String password;

    @NotNull
    private Boolean enabled;

}
