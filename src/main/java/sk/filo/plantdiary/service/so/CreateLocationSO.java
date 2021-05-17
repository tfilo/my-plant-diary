package sk.filo.plantdiary.service.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(name = "CreateLocation")
@Getter
@Setter
@ToString
public class CreateLocationSO {

    @NotNull
    @Size(max = 80)
    private String name;

}
