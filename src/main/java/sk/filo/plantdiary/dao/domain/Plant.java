package sk.filo.plantdiary.dao.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString(exclude = {"events", "photos"})
@Entity
@Table(name = "pd_plant")
@SequenceGenerator(name = "plant_generator", allocationSize = 1, sequenceName = "pd_plant_seq")
public class Plant implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "plant_generator")
    private Long id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", nullable = true, length = 1000)
    private String description;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "plant_type_id", nullable = true)
    private PlantType type;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "location_id", nullable = true)
    private Location location;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "owner_username", nullable = false)
    private User owner;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    public Plant(Long id, String name, User owner, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.deleted = deleted;
    }

    public Plant() {
    }
}
