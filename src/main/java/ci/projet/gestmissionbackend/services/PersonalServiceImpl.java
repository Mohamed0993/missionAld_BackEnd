package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.*;
import ci.projet.gestmissionbackend.entities.*;
import ci.projet.gestmissionbackend.enums.GradeType;
import ci.projet.gestmissionbackend.enums.PersonalRole;
import ci.projet.gestmissionbackend.exceptions.PersonalExists;
import ci.projet.gestmissionbackend.exceptions.PersonalNotFoundException;
import ci.projet.gestmissionbackend.exceptions.ServiceExistsException;
import ci.projet.gestmissionbackend.exceptions.ServicesNotFoundException;
import ci.projet.gestmissionbackend.mappers.PersonalMapperImpl;
import ci.projet.gestmissionbackend.repositories.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PersonalServiceImpl implements PersonalService {
    PersonalRepository personalRepository;
    AssignmentService assignmentService;
    GradeRepository gradeRepository;
    BankAccountRepository bankAccountRepository;
    ServiceRepository serviceRepository;
    PersonalProfilRepository personalProfilRepository;
    private GradeService gradeService;
    private PersonalMapperImpl dtoMapper;
    private PasswordEncoder passwordEncoder;

    @Override
    public PersonalDTO addNewPersonal(PersonalDTO personalDTO) throws PersonalExists, ServiceExistsException {


        Personal pers = this.personalRepository.findByPersonalMatricule(dtoMapper.fromPersonalDTO(personalDTO).getPersonalMatricule());
        Personal personal = dtoMapper.fromPersonalDTO(personalDTO);
        //personal.setPersonalMatricule("ALD"+);
        if(pers !=null){
            throw new PersonalExists("Personal already exists");
        }
        if(personal.getGrade().getType().equals(GradeType.AUTRE) ){
            Grade grade = dtoMapper.fromPersonalDTO(personalDTO).getGrade();
            grade.setId(null);
            this.gradeService.saveGrade(dtoMapper.fromGrade(grade));
        }
        personal.setPassword(passwordEncoder.encode(personal.getPersonalMatricule()));
        if(personal.getGrade().getType().equals(GradeType.ASSISTANT)){
            PersonalProfile personalProfile = this.personalProfilRepository.findByType(PersonalRole.ADMIN);
            personal.addPersonalProfil(personalProfile);
        }
        PersonalProfile personalProfile = this.personalProfilRepository.findByType(PersonalRole.USER);
        personal.addPersonalProfil(personalProfile);
        if(isDg(dtoMapper.fromPersonal(personal))){
            personal.setDirector(true);
        }
        personal = this.personalRepository.save(personal);
        if(personal.getGrade().getType() == GradeType.CHEF ) {
            personal.getService().setChef(personal);
            this.serviceRepository.save(personal.getService());
        }


        return dtoMapper.fromPersonal(personal);
    }

    public boolean isDg(PersonalDTO personalDTO) {
        Personal personal = dtoMapper.fromPersonalDTO(personalDTO);
        GradeType gradeType = personal.getGrade().getType();

        return gradeType == GradeType.DG;
    }



    @Override
    public PersonalDTO findPersonalByMatricule(String matricule) throws PersonalNotFoundException {
        Personal personal = this.personalRepository.findByPersonalMatricule(matricule);
        if(personal == null){
            throw new PersonalNotFoundException("Personal not found");
        }
        return dtoMapper.fromPersonal(personal);
    }
    @Override
    public void deletePersonal(Long personalId) throws Exception {
        Personal personal = this.personalRepository.findById(personalId).orElse(null);
        if(personal.getGrade().getType() == GradeType.CHEF){
            throw new Exception("Personal has chef");
        }
        this.personalRepository.delete(personal);
    }

    public List<PersonalDTO> getAccountable(Assignment assignment){
        List<GradeType> gradeTypes = new ArrayList<>();
        switch (assignment.getNextState()){
            case VCHEF: {
                gradeTypes.add(GradeType.CHEF);
                gradeTypes.add(GradeType.CHEFA);
                List<Personal> personals = this.personalRepository.getChefAndChefA(assignment.getPersonal().getService(), gradeTypes);
                List<PersonalDTO> personalDTOS = personals.stream().map(personal -> dtoMapper.fromPersonal(personal))
                        .collect(Collectors.toList());
                return personalDTOS;
            }
            case VDTYPE:
                gradeTypes.add(GradeType.CHEF);
                gradeTypes.add(GradeType.CHEFA);

                List<Personal> personals = this.personalRepository.getChefAndChefA(
                        assignment.getType().getService(), gradeTypes);
                List<PersonalDTO> personalDTOS = personals.stream().map(personal -> dtoMapper.fromPersonal(personal))
                        .collect(Collectors.toList());
                return personalDTOS;

            case VDG:
                gradeTypes.add(GradeType.DG);
                gradeTypes.add(GradeType.DGA);
                List<Personal> personalList=  this.personalRepository.getDgAndDGA(gradeTypes);
                List<PersonalDTO> personalDTOS1 = personalList.stream().map(personal -> dtoMapper.fromPersonal(personal))
                        .collect(Collectors.toList());
                return personalDTOS1;
        }
        return null;
    }

    @Override
    public List<PersonalDTO> listPersonal() {
        List<Personal> personalList =  this.personalRepository.findAll();
        List<PersonalDTO> personalDTOS = personalList.stream().map(personal -> dtoMapper.fromPersonal(personal))
                .collect(Collectors.toList());
        return personalDTOS;
    }

    @Override
    public PersonalDTO getDg() {
        return null;
    }

    @Override
    public void changePassword(String currentPassword, String newPassword) {

    }



    private Grade getDgGrade(){return this.gradeRepository.findByType(GradeType.DG);}
    private Grade getChefGrade() {
        return this.gradeRepository.findByType(GradeType.CHEF);
    }



    @Override
    public BankAccountDTO addNewBankAccount(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = dtoMapper.fromBankAccountDTO(bankAccountDTO);
        BankAccount savedBankAccount = this.bankAccountRepository.save(bankAccount);
        return dtoMapper.fromBankAccount(savedBankAccount);
    }

    @Override
    public void addPersonalToService(String personalId, String serviceName) throws PersonalNotFoundException, ServicesNotFoundException {

    }


    public PersonalProfileDTO saveProfile(PersonalProfileDTO personalProfileDTO ) {
        PersonalProfile personalProfile = dtoMapper.fromProfilDTO(personalProfileDTO);
        PersonalProfile savedProfile = this.personalProfilRepository.save(personalProfile);

        return dtoMapper.fromProfile(savedProfile);
    }

    @Override
    public List<PersonalDTO> searchPersonal(String keyword) {
        List<Personal> personalList = personalRepository.findByPersonalNameContains(keyword);
        List<PersonalDTO> personalDTOS = personalList.stream().map(personal -> dtoMapper.fromPersonal(personal)).collect(Collectors.toList());
        return personalDTOS;

    }

}
