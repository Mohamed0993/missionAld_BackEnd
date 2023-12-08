package ci.projet.gestmissionbackend.entities;

import ci.projet.gestmissionbackend.enums.AssignementStateEnum;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Entity
@Scope("prototype")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor //@NoArgsConstructor
public class AssignementState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAssignementState;
    private LocalDateTime stateDate;
    @Enumerated(EnumType.STRING)
    private AssignementStateEnum state;
    @ManyToOne
    private Assignment assignment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AssignementState that = (AssignementState) o;
        return idAssignementState != null && Objects.equals(idAssignementState, that.idAssignementState);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
