package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.*;
import ci.projet.gestmissionbackend.entities.BankAccount;
import ci.projet.gestmissionbackend.entities.Grade;
import ci.projet.gestmissionbackend.entities.Personal;
import ci.projet.gestmissionbackend.entities.Service;
import ci.projet.gestmissionbackend.exceptions.PersonalNotFoundException;
import ci.projet.gestmissionbackend.exceptions.ServicesNotFoundException;

import java.util.List;

public interface PersonalService {
    PersonalDTO addNewPersonal(PersonalDTO personalDTO ) throws Exception;
    PersonalDTO findPersonalByMatricule(String matricule) throws PersonalNotFoundException;
    void deletePersonal(Long personalId) throws Exception;
    List<PersonalDTO> listPersonal();
    PersonalDTO getDg();


    void changePassword(String currentPassword, String newPassword);

    BankAccountDTO addNewBankAccount(BankAccountDTO bankAccountDTO);
    void addPersonalToService(String personalId, String serviceName) throws PersonalNotFoundException, ServicesNotFoundException;

    PersonalProfileDTO saveProfile(PersonalProfileDTO personalProfileDTO);

    List<PersonalDTO> searchPersonal(String keyword);
}
