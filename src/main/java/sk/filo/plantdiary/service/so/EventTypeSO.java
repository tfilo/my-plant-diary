package sk.filo.plantdiary.service.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(name = "EventType")
@Getter
@Setter
@ToString
public class EventTypeSO {

    @NotNull
    private Long id;

    private String code;

    private Boolean schedulable;
}
