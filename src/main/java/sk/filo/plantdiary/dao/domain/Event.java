package sk.filo.plantdiary.dao.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"plant"})
@Entity
@Table(name = "pd_event")
@SequenceGenerator(name = "event_generator", allocationSize = 1, sequenceName = "pd_event_seq")
public class Event implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "event_generator")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType type;
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;
    @Column(name = "description", nullable = true, length = 1000)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    public Event(Long id, EventType type, LocalDateTime dateTime, Plant plant) {
        this.id = id;
        this.type = type;
        this.dateTime = dateTime;
        this.plant = plant;
    }

    public Event() {
    }
}


