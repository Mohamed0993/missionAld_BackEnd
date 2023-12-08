package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.*;
import ci.projet.gestmissionbackend.entities.*;
import ci.projet.gestmissionbackend.enums.AssignementStateEnum;
import ci.projet.gestmissionbackend.enums.GradeType;
import ci.projet.gestmissionbackend.exceptions.PersonalNotFoundException;
import ci.projet.gestmissionbackend.exceptions.assignmentNotFoundException;
import ci.projet.gestmissionbackend.exceptions.personalHasPlannedMissionException;
import ci.projet.gestmissionbackend.exceptions.worksiteNotFoundException;
import ci.projet.gestmissionbackend.mappers.AssignmentMapperImpl;
import ci.projet.gestmissionbackend.mappers.PersonalMapperImpl;
import ci.projet.gestmissionbackend.repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j

public class AssignmentServiceImpl implements AssignmentService {
    private final MissionTypeRepository missionTypeRepository;
    AssignementRepository assignementRepository;
    AssignmentStateRepository assignmentStateRepository;
    VehicleRepository vehicleRepository;
    WorksiteRepository worksiteRepository;
    TownRepository townRepository;
    PersonalRepository personalRepository;
   // PersonalServiceImpl personalService;
    private ApplicationContext context;
    private AssignmentMapperImpl dtoMapper;

    private ci.projet.gestmissionbackend.printers.printAssignment printAssignment;

    private PersonalMapperImpl dtoPerMapper;
    private final static int pageSize = 10;


    @Override
    public AssignmentDTO addNewAssignment(AssignmentDTO assignmentDTO) throws personalHasPlannedMissionException, assignmentNotFoundException, PersonalNotFoundException, worksiteNotFoundException {
        List<VehicleDTO> vehicleDTOS = assignmentDTO.getVehicleDTOS();
        List<PersonalDTO> personalDTOS = assignmentDTO.getPersonalDTOS();
        List<WorksiteDTO> worksiteDTOS = assignmentDTO.getWorksiteDTOS();
        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        List<Vehicle> vehicleList = vehicleDTOS.stream().map(vehicleDTO -> dtoMapper.fromVehicleDTO(vehicleDTO))
                .collect(Collectors.toList());
        List<Personal> personalList = personalDTOS.stream().map(personal -> dtoPerMapper.fromPersonalDTO(personal))
                .collect(Collectors.toList());
        List<Worksite> worksiteList = worksiteDTOS.stream().map(worksiteDTO -> dtoMapper.fromWorksiteDTO(worksiteDTO))
                .collect(Collectors.toList());
        if(assignment.getCurrentState() == null){
            assignment.setCurrentState(AssignementStateEnum.CURRENT) ;
            assignment.setNextState(getNextState(dtoMapper.fromAssignment(assignment)));
            assignment.setVehicles(vehicleList);
            assignment.setPersonals(personalList);
            assignment.setWorksites(worksiteList);
            assignment = this.assignementRepository.save(assignment);
            AssignementState assignementState = context.getBean(AssignementState.class);
            assignementState.setState(AssignementStateEnum.CURRENT);
            assignementState.setStateDate(LocalDateTime.now());
            assignementState.setAssignment(assignment);

            AssignementState State = this.assignmentStateRepository.save(assignementState);

        } else {
            assignment = this.assignementRepository.save(assignment);
        }



       // addVehicleToAssignment(vehicleDTOList, assignment.getAssignmentId());
        //addPersonalToAssignment(personalDTOList, assignment.getAssignmentId());
        return dtoMapper.fromAssignment(assignment);
    }

    @Override
    public List<AssignmentDTO> getMissionsForDG() {
        List<Assignment> missionsForDG = this.assignementRepository.getMissionsForDG(AssignementStateEnum.VDG);
        List<AssignmentDTO> assignmentDTOS = missionsForDG.stream().map(assignment -> dtoMapper.fromAssignment(assignment))
                .collect(Collectors.toList());
        return assignmentDTOS;
    }

    @Override
    public List<AssignmentDTO> getMissionsForCHEF() throws PersonalNotFoundException {

        Personal personal = dtoPerMapper.fromPersonalDTO(getPrincipal());
        List<Assignment> assignments = this.assignementRepository.getMissionsForCHEF(AssignementStateEnum.VCHEF, personal.getService(), AssignementStateEnum.VDTYPE, personal.getService());
        List<AssignmentDTO> assignmentDTOS = assignments.stream().map(assignment -> dtoMapper.fromAssignment(assignment))
                .collect(Collectors.toList());
        return assignmentDTOS;
    }

    @Override
    public List<AssignmentDTO> getMissionsForDAF() {
        List<AssignementStateEnum> nextStates =new ArrayList<>();
        nextStates.add(AssignementStateEnum.DAF);
        nextStates.add(AssignementStateEnum.VALIDATED);
        List<Assignment> assignmentList = this.assignementRepository.findByNextStateInOrderByAssignmentIdDesc(nextStates);
        List<AssignmentDTO> assignmentDTOS = assignmentList.stream().map(assignment -> dtoMapper.fromAssignment(assignment))
                .collect(Collectors.toList());
        return assignmentDTOS;
    }

    @Override
    public List<AssignmentDTO> findAll(boolean mine) throws PersonalNotFoundException {
        if (mine) return this.getMissionsForEMP();
        PersonalDTO personalDTO = getPrincipal();
        if (personalDTO.getGradeDTO() != null)
            switch (personalDTO.getGradeDTO().getType()) {
                case DG:
                case DGA:
                    return this.getMissionsForDG();
                case CHEF:
                case CHEFA:
                    return this.getMissionsForCHEF();
                case DAF: {
                    //if (personalDTO.getServiceDTO().getServiceName().equals("DAF"))
                        return this.getMissionsForDAF();
                    //return this.getMissionsForEMP();
                }
                case AUTRE:
                    return this.getMissionsForEMP();
            }
        return null;
    }

    @Override
    public List<AssignmentDTO> getMissionsForEMP() throws PersonalNotFoundException {


        List<Assignment> assignments = this.assignementRepository.findByPersonalOrderByAssignmentIdDesc(dtoPerMapper.fromPersonalDTO(getPrincipal()));
        List<AssignmentDTO> assignmentDTOS = assignments.stream().map(assignment -> dtoMapper.fromAssignment(assignment))
                .collect(Collectors.toList());

        return assignmentDTOS;
    }


    @Override
    public AssignmentDTO findAssignmentById(Long id) throws assignmentNotFoundException {
        Assignment assignment = assignementRepository.findById(id)
                .orElseThrow(()-> new assignmentNotFoundException("Assignment not found"));
        return dtoMapper.fromAssignment(assignment);
    }


    private AssignementStateEnum getNextState(AssignmentDTO assignmentDTO) {
        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        if(isPersChiefOrDg(assignmentDTO.getPersonalDTO())) return getNextStateForDgOrChief(assignmentDTO);
        ci.projet.gestmissionbackend.entities.Service service = null;
        if(assignment.getType() != null)
            service = assignment.getType().getService();

        AssignementStateEnum nextState = AssignementStateEnum.CURRENT;
        //assignment.getCurrentState();
        switch (assignment.getCurrentState()){
            case CURRENT:{
                if(service != null){
                    //System.out.println(service.getServiceId());
                    //System.out.println(assignment.getPersonal().getService().getServiceId());
                    if(Objects.equals(service.getServiceId(), assignment.getPersonal().getService().getServiceId())){
                        nextState = AssignementStateEnum.VCHEF;
                    }else {
                        nextState= AssignementStateEnum.VDTYPE;
                    }
                }else {
                    if(assignment.getPersonal().getService() != null){
                        nextState = AssignementStateEnum.VCHEF;
                    }else {
                        nextState = AssignementStateEnum.VDG;
                    }
                }
            }
            break;
            case VDTYPE: {
                if(assignment.getPersonal().getService() !=null){
                    nextState = AssignementStateEnum.VCHEF;
                }else {
                    nextState = AssignementStateEnum.VDG;
                }
            }
            break;
            case VCHEF: {
                nextState = AssignementStateEnum.VDG;
            }
            break;
            case VDG: {
                nextState = AssignementStateEnum.DAF;
            }
            break;
            case DAF:
                nextState = AssignementStateEnum.VALIDATED;
                break;
        }
        return nextState;
    }



    private AssignementStateEnum getNextStateForDgOrChief(AssignmentDTO assignmentDTO) {
        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        PersonalDTO personalDTO = assignmentDTO.getPersonalDTO();
        ci.projet.gestmissionbackend.entities.Service service = null;
        if(assignment.getType() != null)
            service = assignment.getType().getService();
        AssignementStateEnum nextState = AssignementStateEnum.CURRENT;
        if(isChief(personalDTO)){
            switch (assignment.getCurrentState()){
                    case CURRENT: {
                        if (service != null){
                            nextState = AssignementStateEnum.VDTYPE;
                        }else {
                            nextState = AssignementStateEnum.VDG;
                        }
                    }
                    break;
                    case VDTYPE:{
                        nextState = AssignementStateEnum.VDG;
                    }
                    break;
                    case VDG:{
                        nextState = AssignementStateEnum.DAF;
                    }
                    break;
                    case DAF: {
                        nextState = AssignementStateEnum.VALIDATED;
                    }
                    break;
                }
            } else {
                switch (assignment.getCurrentState()){
                    case CURRENT: {
                        if(service != null){
                            nextState = AssignementStateEnum.VDTYPE;
                        }else {
                            nextState = AssignementStateEnum.DAF;
                        }
                    }
                    break;
                    case VDTYPE: {
                        nextState = AssignementStateEnum.DAF;
                    }
                    break;
                    case DAF: {
                        nextState = AssignementStateEnum.VALIDATED;

                    }
                }
            }
        return nextState;
    }

    @Override
    @Transactional
    public void canceledAssignment(AssignmentDTO assignmentDTO){
        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        assignment.setCurrentState(AssignementStateEnum.CANCELED);
        this.assignementRepository.save(assignment);
        AssignementState state =context.getBean(AssignementState.class);
        state.setState(assignment.getCurrentState());
        state.setStateDate(LocalDateTime.now());
        state.setAssignment(assignment);
        assignment.addAssignmentState(state);
        state = this.assignmentStateRepository.save(state);
        Personal personal = assignment.getPersonal();
        personal.getBankAccount().setAvoir(personal.getBankAccount().getAvoir() + assignment.getCosts().getAmount());
        this.personalRepository.save(personal);

    }
    @Override
    @Transactional
    public AssignmentDTO validateAssignment(AssignmentDTO assignmentDTO){
        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        assignment.setCurrentState(assignment.getNextState());
        assignment.setNextState(this.getNextState(dtoMapper.fromAssignment(assignment)));
        assignment.setSecret(UUID.randomUUID().toString());
        this.assignementRepository.save(assignment);
        AssignementState state = context.getBean(AssignementState.class);
        state.setState(assignment.getCurrentState());
        state.setStateDate(LocalDateTime.now());
        state.setAssignment(assignment);
        assignment.addAssignmentState(state);
        state = this.assignmentStateRepository.save(state);
        return dtoMapper.fromAssignment(assignment);

    }

    @Override
    public void printMission(AssignmentDTO assignmentDTO) throws IOException {
        this.printAssignment.printAssignment(assignmentDTO);
    }

    @Override
    @Transactional
    public boolean validateAssignmentByUuid(String uuid, String secret){
        Assignment assignment = this.assignementRepository.findByUuid(uuid);
        if(assignment == null) return false;
        if(!assignment.getSecret().equals(secret)) return false;
        this.validateAssignment(dtoMapper.fromAssignment(assignment));
        return true;
    }


    @Override
    public boolean isChiefOrDg() {
        return false;
    }

    private boolean isDg(PersonalDTO personalDTO) {
        Personal personal = dtoPerMapper.fromPersonalDTO(personalDTO);
        GradeType gradeType = personal.getGrade().getType();

        return gradeType == GradeType.DG;
    }

    private boolean isChief(PersonalDTO  personalDTO) {
        Personal personal = dtoPerMapper.fromPersonalDTO(personalDTO);
        GradeType gradeType = personal.getGrade().getType();
        return gradeType == GradeType.CHEF;
    }

    @Override
    public boolean isPersChiefOrDg(PersonalDTO personalDTO) {
        return isChief(personalDTO) || isDg(personalDTO);
    }
    @Override
    public List<AssignmentDTO> listAssignment(){
        List<Assignment> assignmentList = assignementRepository.findAll();
        List<AssignmentDTO> assignmentDTOS = assignmentList.stream().map(assignment -> dtoMapper.fromAssignment(assignment))
                .collect(Collectors.toList());
        return assignmentDTOS;
    }

    private AssignementStateEnum getRStateDependsOnConnectedPrincipal(Assignment assignment){

        switch (assignment.getNextState()){
            case VCHEF:
                return AssignementStateEnum.RCHEF;
            case VDG:
                return AssignementStateEnum.RDG;
            case VDTYPE:
                return AssignementStateEnum.RDTYPE;
        }
        return null;
    }

    @Override
    @Transactional
    public void rejectMission(AssignmentDTO assignmentDTO) {
        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        AssignementStateEnum rejectState = getRStateDependsOnConnectedPrincipal(assignment);
        assignment.setCurrentState(rejectState);
        assignment.setNextState(AssignementStateEnum.CURRENT);
        assignment.setSecret(UUID.randomUUID().toString());
        this.assignementRepository.save(assignment);
        AssignementState state = context.getBean(AssignementState.class);
        state.setState(assignment.getCurrentState());
        state.setStateDate(LocalDateTime.now());
//        state.addMission(mission);
        state.setAssignment(assignment);
        assignment.addAssignmentState(state);
        state = this.assignmentStateRepository.save(state);
    }

    @Override
    @Transactional
    public boolean rejectAssignmentByUuid(String Uuid, String secret){
        Assignment assignment =this.assignementRepository.findByUuid(Uuid);
        if(assignment== null) return false;
        if(!assignment.getSecret().equals(secret)) return false;

        this.rejectMission(dtoMapper.fromAssignment(assignment));
        return true;
    }

    @Override
    public void resendAssignment(AssignmentDTO assignmentDTO){
        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        assignment.setCurrentState(AssignementStateEnum.CURRENT);
        assignment.setNextState(this.getNextState(assignmentDTO));
        assignment.setComment("");
        assignment = this.assignementRepository.save(assignment);
        AssignementState state = context.getBean((AssignementState.class));
        state.setState(assignment.getCurrentState());
        state.setStateDate(LocalDateTime.now());
        state.setAssignment(assignment);
        state = this.assignmentStateRepository.save(state);
    }

    @Override
    public boolean isRejected(AssignmentDTO assignmentDTO){
        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        AssignementStateEnum currentState = assignment.getCurrentState();
        if(currentState != null)
            switch (currentState) {
                case RCHEF:
                case RDG:
                case RDTYPE:
                    return true;
            }
        return false;
    }

    @Override
    public PersonalDTO getPrincipal() throws PersonalNotFoundException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Personal personal = personalRepository.findByPersonalMatricule(principal.getUsername());
        return dtoPerMapper.fromPersonal(personal);

    }


    // MissionType Service



   // End MissionType Service


    @Override
    public void addPersonalToAssignment(List<PersonalDTO> personalDTOS, Long assignmentId) throws PersonalNotFoundException, assignmentNotFoundException {
        AssignmentDTO assignmentDTO = findAssignmentById(assignmentId);
        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        List<Personal> personals = personalDTOS.stream().map(personalDTO -> dtoMapper.getPersonalMapper().fromPersonalDTO(personalDTO))
                .collect(Collectors.toList());
        //Vehicle vehicle = vehicleList.get(1);
        //
        if(assignment.getPersonals()!= null){
            for (Personal personal: personals) {
                personal.getAssignments().add(assignment);
                assignment.getPersonals().add(personal);
            }
        }
    }

    @Override
    public void addVehicleToAssignment(List<VehicleDTO> vehicleDTOS, Long assignmentId) throws assignmentNotFoundException {
        AssignmentDTO assignmentDTO = findAssignmentById(assignmentId);
        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        List<Vehicle> vehicleList = vehicleDTOS.stream().map(vehicleDTO -> dtoMapper.fromVehicleDTO(vehicleDTO))
                .collect(Collectors.toList());
        //Vehicle vehicle = vehicleList.get(1);
        //
            if(assignment.getVehicles()!= null){
                for (Vehicle vehicle: vehicleList) {
                    vehicle.getAssignments().add(assignment);
                    assignment.getVehicles().add(vehicle);
                }


            }
        //}


    }


    public void addWorksiteToAssignment(Long worksiteId, Long assignmentId) throws assignmentNotFoundException, worksiteNotFoundException {


    }



}
