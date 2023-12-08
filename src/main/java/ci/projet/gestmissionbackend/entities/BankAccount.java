package ci.projet.gestmissionbackend.entities;

import ci.projet.gestmissionbackend.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class BankAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankAccountId;
    private String bankAccountRib;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private double avoir;
    @OneToOne
    private Personal personal;
}
