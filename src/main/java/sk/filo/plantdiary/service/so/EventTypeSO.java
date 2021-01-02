package sk.filo.plantdiary.service.so;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class EventTypeSO {

    @NotNull
    private Long id;

    private String code;

    private Boolean schedulable;
}
