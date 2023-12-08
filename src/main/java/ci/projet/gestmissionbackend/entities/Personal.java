package ci.projet.gestmissionbackend.entities;

import ci.projet.gestmissionbackend.enums.Civilite;
import ci.projet.gestmissionbackend.enums.PersonalClass;
import ci.projet.gestmissionbackend.enums.Sex;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Personal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Enumerated(EnumType.STRING)
    private Civilite civilite;
    private String personalMatricule;
    private String personalName;
    private String personalFullName;
    private String fonction;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    private Date personalDayOfBirth;
    private String personalContact;
    private String email;

    private boolean director;
    @Enumerated(EnumType.STRING)
    private PersonalClass personalClass;
        @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_grade")
    private Grade grade;

    @ManyToOne
    @JoinColumn(name = "id_service", nullable = true)
    private Service service;
    @OneToMany(mappedBy = "personal")
    private List<Assignment> assignmentsList ;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "assignment_personal",
            joinColumns = {@JoinColumn(name = "PERSONAL_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ASSIGNMENT_ID")})
    private List<Assignment> assignments = new ArrayList<>();

    @OneToOne
    private BankAccount bankAccount;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name ="APP_USER_APP_PROFILE",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_PROFILE_ID")})
    private Set<PersonalProfile> personalProfiles = new HashSet<>();


    @Transient
    public void addPersonalProfil(PersonalProfile personalProfile){
        this.personalProfiles.add(personalProfile);
    }
    @Transient
    public String getFullName()
    {
        return this.personalFullName + " " + this.personalName;
    }
    public boolean isDirector(){
        return director;
    }
    public void setDirector(boolean director){this.director = director;}
    @Override
    public String toString() {
        return String.valueOf(personalId);
    }

}
