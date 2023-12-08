package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.AssignmentDTO;
import ci.projet.gestmissionbackend.dtos.CostsDTO;
import ci.projet.gestmissionbackend.entities.Costs;
import ci.projet.gestmissionbackend.exceptions.assignmentNotFoundException;

import java.io.IOException;
import java.util.List;

public interface CostService {
    /*List<CostsDTO> findAll();*/
    CostsDTO save(CostsDTO costsDTO) throws IOException;
    void delete(CostsDTO costsDTO);



    CostsDTO calculTotal();

    CostsDTO setDecompteWithMission(AssignmentDTO assignmentDTO);

    void printDecompte(Costs costsDTO);
    //void printLiquidation(CostsDTO costsDTO) throws IOException;

    CostsDTO getCost();

    void printCost(CostsDTO costs) throws IOException;

    CostsDTO findCostById(Long costId) throws assignmentNotFoundException;
}
