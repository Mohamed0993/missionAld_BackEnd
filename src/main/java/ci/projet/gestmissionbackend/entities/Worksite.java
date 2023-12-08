package ci.projet.gestmissionbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Worksite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long worksiteId;
    private String worksiteObject;
    private int worksiteDuration;
    @ManyToMany(/*mappedBy = "assignments", */fetch = FetchType.EAGER)
    @JoinTable(name = "assignment_worksite",
            joinColumns = {@JoinColumn(name = "WORKSITE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ASSIGNMENT_ID")})
    private List<Assignment>assignments = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    private Town town;
}
