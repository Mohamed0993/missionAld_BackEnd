package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.*;
import ci.projet.gestmissionbackend.exceptions.PersonalNotFoundException;
import ci.projet.gestmissionbackend.exceptions.assignmentNotFoundException;
import ci.projet.gestmissionbackend.exceptions.personalHasPlannedMissionException;
import ci.projet.gestmissionbackend.exceptions.worksiteNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

public interface AssignmentService {
    //AssignmentDTO addNewAssignment (AssignmentDTO assignmentDTO, List<VehicleDTO> vehicleDTOS, List<PersonalDTO> personalDTOList) throws personalHasPlannedMissionException, assignmentNotFoundException, PersonalNotFoundException, worksiteNotFoundException;

    AssignmentDTO addNewAssignment(AssignmentDTO assignmentDTO) throws personalHasPlannedMissionException, assignmentNotFoundException, PersonalNotFoundException, worksiteNotFoundException;

    List<AssignmentDTO> getMissionsForDG();

    List<AssignmentDTO> getMissionsForCHEF() throws PersonalNotFoundException;

    List<AssignmentDTO> getMissionsForDAF();

    List<AssignmentDTO> findAll(boolean mine) throws PersonalNotFoundException;

    List<AssignmentDTO> getMissionsForEMP() throws PersonalNotFoundException;

    @Transactional
    void canceledAssignment(AssignmentDTO assignmentDTO);

   // void printMission(Long id) throws IOException, assignmentNotFoundException;

    @Transactional
    boolean validateAssignmentByUuid(String uuid, String secret);

    boolean isChiefOrDg();

    boolean isPersChiefOrDg(PersonalDTO personalDTO);
    List<AssignmentDTO> listAssignment();

    @Transactional
    void rejectMission(AssignmentDTO assignmentDTO);

    @Transactional
    boolean rejectAssignmentByUuid(String Uuid, String secret);

    void resendAssignment(AssignmentDTO assignmentDTO);

    boolean isRejected(AssignmentDTO assignmentDTO);

    PersonalDTO getPrincipal() throws PersonalNotFoundException;

   /* VehicleDTO addNewVehicle(VehicleDTO vehicleDTO);
    VehicleDTO findVehicleByVehicleRegistration (String vehicleRegistration);
    List<VehicleDTO> listVehicle();*/



    AssignmentDTO findAssignmentById(Long id) throws assignmentNotFoundException;
    void addPersonalToAssignment(List<PersonalDTO> personalDTOS, Long assignmentId) throws PersonalNotFoundException, assignmentNotFoundException;
    void addVehicleToAssignment(List<VehicleDTO> vehicleDTOS, Long assignmentId) throws assignmentNotFoundException;
    //void addPersonalToAssignment(List<PersonalDTO> personalDTOS, Long assignmentId ) throws assignmentNotFoundException;
    void addWorksiteToAssignment(Long worksiteId, Long assignmentId) throws assignmentNotFoundException, worksiteNotFoundException;

    @Transactional
    AssignmentDTO validateAssignment(AssignmentDTO assignmentDTO);

    void printMission(AssignmentDTO assignmentDTO) throws IOException;
}
