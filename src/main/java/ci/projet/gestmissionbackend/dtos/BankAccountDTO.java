package ci.projet.gestmissionbackend.dtos;

import ci.projet.gestmissionbackend.enums.AccountType;
import lombok.Data;

@Data
public class BankAccountDTO {

    private Long bankAccountId;
    private String bankAccountRib;
    private AccountType accountType;
}
