package sk.filo.plantdiary.service.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(name = "CreatePlant")
@Getter
@Setter
@ToString
public class CreatePlantSO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 1000)
    private String description;

    @Valid
    private PlantTypeSO type;

    @Valid
    private LocationBasicSO location;

}
