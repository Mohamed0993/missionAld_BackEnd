package ci.projet.gestmissionbackend.mappers;

import ci.projet.gestmissionbackend.dtos.*;
import ci.projet.gestmissionbackend.entities.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor
public class AssignmentMapperImpl {
    private PersonalMapperImpl personalMapper;
    public AssignmentDTO fromAssignment(Assignment assignment){
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setAssignmentId(assignment.getAssignmentId());
        BeanUtils.copyProperties(assignment, assignmentDTO);
        assignmentDTO.setPersonalDTO(personalMapper.fromPersonal(assignment.getPersonal()));
        assignmentDTO.setPersonalDTOS(assignment.getPersonals().stream().map(personal -> personalMapper.fromPersonal(personal))
                .collect(Collectors.toList()));
        assignmentDTO.setVehicleDTOS(assignment.getVehicles().stream().map(this::fromVehicle).collect(Collectors.toList()));
        assignmentDTO.setType(fromMissionType(assignment.getType()));
        assignmentDTO.setWorksiteDTOS(assignment.getWorksites().stream().map(this::fromWorksite).collect(Collectors.toList()));
        //assignmentDTO.setCostsDTO(fromCost(assignment.getCosts()));
        return assignmentDTO;
    }
    public Assignment fromAssigmentDTO(AssignmentDTO assignmentDTO){
        Assignment assignment= new Assignment();
        assignment.setAssignmentId(assignmentDTO.getAssignmentId());
        //assignment.setPersonal(personalMapper.fromPersonalDTO(assignmentDTO.getPersonalDTO()));
        assignment.setPersonals(assignmentDTO.getPersonalDTOS().stream().map(personalDTO -> personalMapper.fromPersonalDTO(personalDTO))
        .collect(Collectors.toList()));
        //assignment.setAssignementStates();
        BeanUtils.copyProperties(assignmentDTO,assignment);
        //assignment.setAssignmentDate(assignmentDTO.getAssignmentDate());
        assignment.setVehicles(assignmentDTO.getVehicleDTOS().stream().map(this::fromVehicleDTO).collect(Collectors.toList()));
        assignment.setPersonal(personalMapper.fromPersonalDTO(assignmentDTO.getPersonalDTO()));
        assignment.setType(fromMissionTypeDTO(assignmentDTO.getType()));
        assignment.setWorksites(assignmentDTO.getWorksiteDTOS().stream().map(this::fromWorksiteDTO).collect(Collectors.toList()));
        //assignment.setCosts(fromCostDTO(assignmentDTO.getCostsDTO()));
        return assignment;
    }

    public VehicleDTO fromVehicle(Vehicle vehicle){
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setVehicleId(vehicle.getVehicleId());
        BeanUtils.copyProperties(vehicle,vehicleDTO);
        return vehicleDTO;
    }
    public Vehicle fromVehicleDTO(VehicleDTO vehicleDTO){
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleDTO,vehicle);
        return vehicle;
    }
    public WorksiteDTO fromWorksite(Worksite worksite){
        WorksiteDTO worksiteDTO = new WorksiteDTO();
        worksiteDTO.setWorksiteId(worksite.getWorksiteId());
        BeanUtils.copyProperties(worksite,worksiteDTO);
       // worksiteDTO.setAssignmentDTOS(worksiteDTO.getAssignmentDTOS());
       worksiteDTO.setTownDTO(fromTown(worksite.getTown()));
        return worksiteDTO;
    }
    public Worksite fromWorksiteDTO(WorksiteDTO worksiteDTO){
        Worksite worksite = new Worksite();
        worksite.setWorksiteId(worksiteDTO.getWorksiteId());
        BeanUtils.copyProperties(worksiteDTO,worksite);
        //worksite.setAssignmentList(worksiteDTO.getAssignmentDTOS());
        worksite.setTown(fromTownDTO(worksiteDTO.getTownDTO()));

        return worksite;
    }
    public TownDTO fromTown(Town town){
        TownDTO townDTO = new TownDTO();
        townDTO.setTownId(town.getTownId());
        BeanUtils.copyProperties(town, townDTO);
        return townDTO;
    }
    public Town fromTownDTO(TownDTO townDTO){
        Town town = new Town();
        town.setTownId(townDTO.getTownId());
        BeanUtils.copyProperties(townDTO,town);
        return town;
    }

    public MissionType fromMissionTypeDTO(MissionTypeDTO missionTypeDTO){
        MissionType missionType = new MissionType();
        missionType.setId(missionTypeDTO.getId());
        BeanUtils.copyProperties(missionTypeDTO, missionType);
        missionType.setService(personalMapper.fromServiceDTO(missionTypeDTO.getServiceDTO()));
        return missionType;
    }

    public MissionTypeDTO fromMissionType(MissionType missionType){
        MissionTypeDTO missionTypeDTO = new MissionTypeDTO();
        missionTypeDTO.setId(missionType.getId());
        BeanUtils.copyProperties(missionType,missionTypeDTO);
        missionTypeDTO.setServiceDTO(personalMapper.fromService(missionType.getService()));
        return missionTypeDTO;
    }
    public TauxDTO fromTaux(Taux taux){
        TauxDTO tauxDTO = new TauxDTO();
        tauxDTO.setId(taux.getId());
        BeanUtils.copyProperties(taux,tauxDTO);
        return tauxDTO;
    }

    public Taux fromTauxDTO(TauxDTO tauxDTO){
        Taux taux = new Taux();
        taux.setId(tauxDTO.getId());
        BeanUtils.copyProperties(tauxDTO,taux);
        return taux;
    }

    public CostsDTO fromCost(Costs costs){
        CostsDTO costsDTO = new CostsDTO();
        costsDTO.setCostsId(costs.getCostsId());
        BeanUtils.copyProperties(costs,costsDTO);
        return costsDTO;

    }
    public Costs fromCostDTO(CostsDTO costsDTO){
        Costs costs = new Costs();
        if(costsDTO != null){
            costs.setCostsId(costsDTO.getCostsId());
            BeanUtils.copyProperties(costsDTO,costs);
            return costs;
        }
        return costs;
    }
}
