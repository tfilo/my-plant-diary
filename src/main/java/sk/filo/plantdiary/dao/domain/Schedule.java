package sk.filo.plantdiary.dao.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString(exclude = {"plant"})
@Entity
@Table(name = "pd_schedule")
@SequenceGenerator(name = "schedule_generator", allocationSize = 1, sequenceName = "pd_schedule_seq")
public class Schedule implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "schedule_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType type;

    @Column(name = "repeat_every", nullable = true)
    private Integer repeatEvery;

    @Column(name = "auto_update", nullable = false)
    private Boolean autoUpdate;

    @Column(name = "next", nullable = false)
    private LocalDate next;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "owner_username", nullable = false)
    private User owner;
}