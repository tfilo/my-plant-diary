package sk.filo.plantdiary.dao.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
@Table(name = "pd_event_type")
@SequenceGenerator(name = "event_type_generator", allocationSize = 1, sequenceName = "pd_event_type_seq")
public class EventType implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "event_type_generator")
    private Long id;

    @Column(name = "code", nullable = false, length = 80, unique = true)
    private String code;

    @Column(name = "schedulable", nullable = false)
    private Boolean schedulable;
}
