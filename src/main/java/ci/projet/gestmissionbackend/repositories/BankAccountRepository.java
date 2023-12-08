package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
