package ci.projet.gestmissionbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;

@Entity
@Scope("prototype")
@Data @NoArgsConstructor @AllArgsConstructor
public class MissionType {
    @Id @GeneratedValue
    private Long id;
    private String label;
    @ManyToOne(fetch = FetchType.EAGER)
    private Service service;
}
