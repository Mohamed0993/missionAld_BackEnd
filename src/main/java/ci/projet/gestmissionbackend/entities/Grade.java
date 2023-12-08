package ci.projet.gestmissionbackend.entities;

import ci.projet.gestmissionbackend.enums.GradeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private GradeType type;
    private String label;

    @Override
    public boolean equals(Object other) {
        return (other instanceof Grade) && (id != null)
                ? id.equals(((Grade) other).id)
                : (other == this);
    }

    @Override
    public int hashCode() {
        return (id != null)
                ? (this.getClass().hashCode() + id.hashCode())
                : super.hashCode();
    }



}
