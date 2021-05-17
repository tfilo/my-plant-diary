package sk.filo.plantdiary.service.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(name = "PlantBasic")
@Getter
@Setter
@ToString
public class PlantBasicSO {

    @NotNull
    private Long id;

    @Size(max = 100)
    private String name;

}
