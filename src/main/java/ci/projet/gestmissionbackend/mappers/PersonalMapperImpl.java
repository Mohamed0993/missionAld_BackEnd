package ci.projet.gestmissionbackend.mappers;

import ci.projet.gestmissionbackend.dtos.*;
import ci.projet.gestmissionbackend.entities.BankAccount;
import ci.projet.gestmissionbackend.entities.Grade;
import ci.projet.gestmissionbackend.entities.Personal;
import ci.projet.gestmissionbackend.entities.PersonalProfile;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class PersonalMapperImpl {
    public PersonalDTO fromPersonal(Personal personal){

        PersonalDTO personalDTO = new PersonalDTO();
        personalDTO.setId(personal.getPersonalId());
        BeanUtils.copyProperties(personal, personalDTO);
        personalDTO.setServiceDTO(fromService(personal.getService()));
        personalDTO.setGradeDTO(fromGrade(personal.getGrade()));
        personalDTO.setPersonalProfileDTOS(personal.getPersonalProfiles().stream().map(this::fromProfile).collect(Collectors.toSet()));
        return personalDTO;
    }
    public Personal fromPersonalDTO(PersonalDTO personalDTO){
        Personal personal = new Personal();
        personal.setPersonalId(personalDTO.getId());
        BeanUtils.copyProperties(personalDTO,personal);
        personal.setService(fromServiceDTO(personalDTO.getServiceDTO()));
        personal.setGrade(fromGradeDTO(personalDTO.getGradeDTO()));
        personal.setPersonalProfiles(personalDTO.getPersonalProfileDTOS().stream().map(this::fromProfilDTO).collect(Collectors.toSet()));
        return personal;
    }

    public ServiceDTO fromService(ci.projet.gestmissionbackend.entities.Service service){
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(service.getServiceId());
        BeanUtils.copyProperties(service,serviceDTO);
        return serviceDTO;
    }
    public ci.projet.gestmissionbackend.entities.Service fromServiceDTO(ServiceDTO serviceDTO){
        ci.projet.gestmissionbackend.entities.Service service = new ci.projet.gestmissionbackend.entities.Service();
        service.setServiceId(serviceDTO.getId());
        BeanUtils.copyProperties(serviceDTO,service);
        return service;
    }
    public BankAccountDTO fromBankAccount(BankAccount bankAccount){
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        BeanUtils.copyProperties(bankAccount,bankAccountDTO);
        return bankAccountDTO;
    }
    public BankAccount fromBankAccountDTO(BankAccountDTO bankAccountDTO){
        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(bankAccountDTO,bankAccount);
        return bankAccount;
    }
    public GradeDTO fromGrade(Grade grade){
        GradeDTO gradeDTO = new GradeDTO();
        gradeDTO.setId(grade.getId());
        BeanUtils.copyProperties(grade,gradeDTO);
        return gradeDTO;
    }
    public Grade fromGradeDTO(GradeDTO gradeDTO){
        Grade grade = new Grade();
        grade.setId(gradeDTO.getId());
        BeanUtils.copyProperties(gradeDTO,grade);
        return grade;
    }
    public PersonalProfileDTO fromProfile(PersonalProfile personalProfile){
        PersonalProfileDTO personalProfileDTO = new PersonalProfileDTO();
        personalProfileDTO.setId(personalProfile.getId());
        personalProfileDTO.setType(personalProfile.getType());
        return personalProfileDTO;
    }
    public PersonalProfile fromProfilDTO(PersonalProfileDTO personalProfileDTO){
        PersonalProfile personalProfile = new PersonalProfile();
        personalProfile.setId(personalProfileDTO.getId());
        personalProfile.setType(personalProfileDTO.getType());
        return personalProfile;
    }
}
