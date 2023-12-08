package ci.projet.gestmissionbackend.repositories;

import ci.projet.gestmissionbackend.entities.*;
import ci.projet.gestmissionbackend.enums.AssignementStateEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AssignementRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByNextStateInAndAssignmentDateOfDepartureBetween(List<AssignementStateEnum> nextState, LocalDateTime start, LocalDateTime end);
    Assignment findByUuid(String uuid);

    List<Assignment>findAssignmentByPersonal(Personal personal);

    @Query("select m from Assignment m where m.nextState = ?1 order by m.assignmentId desc")
    List<Assignment> getMissionsForDG(AssignementStateEnum nextState);

    @Query("select m from Assignment m where (m.nextState = ?1 and m.personal.service = ?2) or (m.nextState = ?3 and m.type.service= ?4) order by m.assignmentId desc")
    List<Assignment> getMissionsForCHEF(AssignementStateEnum nextStateCHEF, Service personalServ, AssignementStateEnum nextStateVDTYPE, Service typeService);

//    Page<Mission> findByNextStateOrderByIdMissionDesc(MissionStateEnum nextState, Pageable pageable);

    List<Assignment> findByNextStateOrderByAssignmentIdDesc(AssignementStateEnum nextState);

    @Query("select a from Assignment a where a.nextState in ?1 order by a.assignmentId DESC")
    List<Assignment> findByNextStateInOrderByAssignmentIdDesc(List<AssignementStateEnum> nextState);

    List<Assignment> findByPersonalOrderByAssignmentIdDesc(Personal personal);

    Page<Assignment> findByNextStateInAndAssignmentDateOfDepartureBetween(List<AssignementStateEnum> nextState,LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Assignment> findByNextStateInAndTypeAndAssignmentDateOfDepartureBetween(List<AssignementStateEnum> nextState, MissionType type, LocalDateTime start, LocalDateTime end, Pageable page);
    Page<Assignment> findByNextStateInAndWorksitesAndAssignmentDateOfDepartureBetween(List<AssignementStateEnum> nextState, Worksite worksite, LocalDateTime start, LocalDateTime end, Pageable page);
    /*@Query("select m from Assignment m where m.nextState in ?1 and ?2 member m.worksites and m.startDate between ?3 and ?4")
    Page<Mission> findByNextStateInAndVilleAndStartDateBetween(List<MissionStateEnum> nextState,Ville ville, LocalDateTime start, LocalDateTime end, Pageable page);*/
    @Query("select m from Assignment m where m.nextState in ?1 and m.personal.service = ?2 and m.assignmentDateOfDeparture between ?3 and ?4")
    Page<Assignment> findByNextStateInAndDeptAndStartDateBetween(List<AssignementStateEnum> nextState,Service service, LocalDateTime start, LocalDateTime end, Pageable page);


    //List<Assignment> findByNextStateInAndAssignmentDateOfDepartureBetween(List<AssignementStateEnum> nextState,LocalDateTime start, LocalDateTime end);
    List<Assignment> findByNextStateInAndTypeAndAssignmentDateOfDepartureBetween(List<AssignementStateEnum> nextState,MissionType type, LocalDateTime start, LocalDateTime end);
    //List<Assignment> findByNextStateInAndEntrepriseAndStartDateBetween(List<MissionStateEnum> nextState,Entreprise entreprise, LocalDateTime start, LocalDateTime end);
    @Query("select m from Assignment m where m.nextState in ?1 and ?2 member m.worksites and m.assignmentDateOfDeparture between ?3 and ?4")
    List<Assignment> findByNextStateInAndVilleAndStartDateBetween(List<AssignementStateEnum> nextState,Worksite worksite, LocalDateTime start, LocalDateTime end);
    @Query("select m from Assignment m where m.nextState in ?1 and m.personal.service = ?2 and m.assignmentDateOfDeparture between ?3 and ?4")
    List<Assignment> findByNextStateInAndDeptAndStartDateBetween(List<AssignementStateEnum> nextState,Service service, LocalDateTime start, LocalDateTime end);





    Page<Assignment> findByPersonalAndAssignmentDateOfDepartureBetween(Personal personal,LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Assignment> findByPersonalAndTypeAndAssignmentDateOfDepartureBetween(Personal personal,MissionType type, LocalDateTime start, LocalDateTime end, Pageable page);

    Page<Assignment> findByPersonalAndWorksitesAndAssignmentDateOfDepartureBetween(Personal personal,Worksite worksite, LocalDateTime start, LocalDateTime end, Pageable page);

  /*  @Query("select m from Assignment m where m.personal = ?1 and ?2 member m.villes and m.startDate between ?3 and ?4")
    Page<Mission> findByEmployeAndVilleAndStartDateBetween(Employe employe,Ville ville, LocalDateTime start, LocalDateTime end, Pageable page);*/

    @Query("select m from Assignment m where m.personal = ?1 and m.personal.service = ?2 and m.assignmentDateOfDeparture between ?3 and ?4")
    Page<Assignment> findByPersonalAndServiceAndAssignmentDateOfDepartureBetween(Personal personal,Service service, LocalDateTime start, LocalDateTime end, Pageable page);
}
