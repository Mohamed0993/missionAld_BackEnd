package ci.projet.gestmissionbackend.entities;

import ci.projet.gestmissionbackend.enums.AssignementStateEnum;
import ci.projet.gestmissionbackend.enums.AssignmentStatus;
import ci.projet.gestmissionbackend.enums.MovingMeans;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
 //@NoArgsConstructor @AllArgsConstructor
public class Assignment {
    public Assignment(){
        this.uuid = UUID.randomUUID().toString();
        this.secret = UUID.randomUUID().toString();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;
    private String assignmentPattern;
    @Enumerated(EnumType.STRING)
    private MovingMeans movingMeans;
    private LocalDateTime assignmentDate;
    private LocalDateTime assignmentDateOfDeparture;
    private LocalDateTime assigmentReturnDate;
    @Enumerated(EnumType.STRING)
    private AssignmentStatus assignmentStatus;
    @OneToMany(mappedBy = "assignment", fetch = FetchType.EAGER)
    private Set<AssignementState> assignementStates = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private AssignementStateEnum currentState;
    @Enumerated(EnumType.STRING)
    private AssignementStateEnum nextState;
    @ManyToOne
    @JoinColumn(name = "personal_id")
    private Personal personal;
    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(name = "assignment_vehicle",
            joinColumns = {@JoinColumn(name = "ASSIGNMENT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "VEHICLE_ID")})
    private List<Vehicle> vehicles = new ArrayList<>();

    @ManyToMany(/*mappedBy = "assignments", */fetch = FetchType.EAGER)
    @JoinTable(name = "assignment_personal",
            joinColumns = {@JoinColumn(name = "ASSIGNMENT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "PERSONAL_ID")})
    private List<Personal>personals = new ArrayList<>();

    @ManyToMany(/*mappedBy = "assignments", */fetch = FetchType.LAZY)
    @JoinTable(name = "assignment_worksite",
            joinColumns = {@JoinColumn(name = "ASSIGNMENT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "WORKSITE_ID")})
    private List<Worksite>worksites = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    private Costs costs;
    @OneToOne
    @JoinColumn(name = "id_type")
    private MissionType type;
    private String comment;
    private String uuid;
    private String secret;

    /*@Transient
    public AssignementState getLastState() {
        return Collections.max(this.assignementStates, Comparator.comparing(AssignementState::getStateDate));
    }*/

    public void addAssignmentState(AssignementState assignementState){
        this.assignementStates.add(assignementState);
    }
}
