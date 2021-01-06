package sk.filo.plantdiary.service.so;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class CreatePhotoSO {

    @Size(max = 200)
    private String description;

    @Valid
    @NotNull
    private PlantBasicSO plant;

}
