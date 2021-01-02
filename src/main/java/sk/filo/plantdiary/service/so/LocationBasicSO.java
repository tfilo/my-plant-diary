package sk.filo.plantdiary.service.so;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class LocationBasicSO {

    private Long id;

    @Size(max=80)
    private String name;

}
