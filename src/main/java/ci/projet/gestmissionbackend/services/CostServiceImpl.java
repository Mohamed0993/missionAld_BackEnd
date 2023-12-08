package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.Component.Excel;
import ci.projet.gestmissionbackend.dtos.AssignmentDTO;
import ci.projet.gestmissionbackend.dtos.CostsDTO;
import ci.projet.gestmissionbackend.entities.Assignment;
import ci.projet.gestmissionbackend.entities.Costs;
import ci.projet.gestmissionbackend.entities.Taux;
import ci.projet.gestmissionbackend.entities.Worksite;
import ci.projet.gestmissionbackend.enums.MovingMeans;
import ci.projet.gestmissionbackend.exceptions.assignmentNotFoundException;
import ci.projet.gestmissionbackend.mappers.AssignmentMapperImpl;
import ci.projet.gestmissionbackend.mappers.PersonalMapperImpl;
import ci.projet.gestmissionbackend.repositories.AssignementRepository;
import ci.projet.gestmissionbackend.repositories.CostsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CostServiceImpl implements CostService {
    @Autowired
    private CostsRepository costsRepository;
    private Costs costs;
    @Autowired
    private AssignementRepository assignementRepository;
    @Autowired
    Excel excel;

    @Autowired
    private ci.projet.gestmissionbackend.printers.printDecompte decom;

    @Autowired
    private TauxService tauxService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AssignmentMapperImpl dtoMapper;
    @Autowired
    private PersonalMapperImpl mapper;
   /* @Override
    public List<CostsDTO> findAll() {
        List<Costs> costsList = this.costsRepository.findAll();
        List<CostsDTO> costsDTOS = costsList.stream().map(costs-> dtoMapper.fromCost(costs))
                .collect(Collectors.toList());
        return costsDTOS;
    }*/

    @Override
    public CostsDTO save(CostsDTO costsDTO) throws IOException {
        Costs decompte = dtoMapper.fromCostDTO(costsDTO);

        costs.calculeTotal();
        decompte = this.costsRepository.save(decompte);
        this.assignementRepository.save(decompte.getAssignment());
        //this.printCost(decompte);
        //this.printDecompte(decompte);

        return dtoMapper.fromCost(decompte);
    }

    @Override
    public void delete(CostsDTO costsDTO) {
        Costs costs1 = dtoMapper.fromCostDTO(costsDTO);
        this.costsRepository.delete(costs1);

    }
    @Override
    public CostsDTO calculTotal(){
        System.out.println(this.costs);
        this.costs.calculeTotal();
        System.out.println(this.costs);

        return dtoMapper.fromCost(this.costs);
    }


    @Override
    public CostsDTO setDecompteWithMission(AssignmentDTO assignmentDTO) {

        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        if (assignment.getCosts() == null) {
            this.costs = new Costs();
            this.costs.setAssignment(assignment);
            assignment.setCosts(this.costs);
            this.fillSejour(assignment.getAssignmentDateOfDeparture(), assignment.getAssigmentReturnDate());
            this.fillTaux(dtoMapper.fromAssignment(assignment));
        } else {
            this.costs = assignment.getCosts();
        }
        return dtoMapper.fromCost(this.costs);
        //Set Taux
        //System.out.println("ok2");
    }

    @Override
    public void printDecompte(Costs costsDTO) {

    }



   /* @Override
    public void printDecompte(Costs costs1){
        System.out.println("start");
        this.excel.generateExcel(costs1);
    }*/



    @Override
    public CostsDTO getCost() {
        CostsDTO costsDTO = dtoMapper.fromCost(costs);
        return costsDTO;
    }

    @Override
    public void printCost(CostsDTO Cost) throws IOException {
        Costs costs1 = dtoMapper.fromCostDTO(Cost);
        this.decom.printLiquidation(costs1);
    }

    @Override
    public CostsDTO findCostById(Long costId) throws assignmentNotFoundException {
        Costs costs1 = costsRepository.findById(costId)
                .orElseThrow(()-> new assignmentNotFoundException("Assignment not found"));
        return dtoMapper.fromCost(costs1);
    }

    /* @Override
     public void printCost(CostsDTO costsDTO) throws IOException {
         this.decom.printDecompte(costsDTO);
     }*/
    private void fillTaux(AssignmentDTO assignmentDTO) {
        Taux taux = dtoMapper.fromTauxDTO( this.tauxService.getTaux());
        Assignment assignment = dtoMapper.fromAssigmentDTO(assignmentDTO);
        if (assignment.getPersonal().isDirector()) {
            this.costs.setRateLunchAndDiner(taux.getRateDejounerDinerDirec());
            this.costs.setRateBreakfast(taux.getRatePetitDejounerDirec());
            this.costs.setRateAccommodation(taux.getRateHebergementDirec());
            this.costs.setRateKilometer(taux.getRateKilometriqueDirec());
        } else {
            this.costs.setRateLunchAndDiner(taux.getRateDejounerDiner());
            this.costs.setRateBreakfast(taux.getRatePetitDejouner());
            this.costs.setRateAccommodation(taux.getRateHebergement());
            if (assignment.getMovingMeans() == MovingMeans.VEHICLE) {
                this.costs.setRateKilometer(taux.getRateKilometrique());
            }
            if (assignment.getWorksites() !=  null) {
                double distance = 0;
                double tauxV = 0;
                double tauxM = 0;
                for(Worksite destination : assignment.getWorksites()) {
                    distance += destination.getTown().getTownDistance();
                    tauxV += destination.getTown().getTauxAuto();

                }



                if (assignment.getMovingMeans() == MovingMeans.VEHICLE) {

                    this.costs.setDistance(distance);
                    this.costs.setRateAuto(tauxV);
                    //this.costs.setNombreTickAuto(destination.getTown().getNombreTickAuto());
                } else if (assignment.getMovingMeans() == MovingMeans.TRAIN
                        || assignment.getMovingMeans() == MovingMeans.PublicTransport
                        || assignment.getMovingMeans() == MovingMeans.PLANE) {
                    double tauxKilometrique = 0d;
                    switch (assignment.getMovingMeans()) {
                        case TRAIN:
                            for(Worksite destination : assignment.getWorksites()) {
                                tauxM += destination.getTown().getTauxTRAIN();
                            }

                            tauxKilometrique = tauxM;
                            break;
                        case PublicTransport:
                            for(Worksite destination : assignment.getWorksites()) {
                                tauxM += destination.getTown().getTauxCTM();
                            }
                            tauxKilometrique = tauxM;
                            break;
                        case PLANE:
                            for(Worksite destination : assignment.getWorksites()) {
                                tauxM += destination.getTown().getTauxAvion();
                            }
                            tauxKilometrique = tauxM;
                            break;
                    }
                    this.costs.setRateKilometer(tauxKilometrique);
                    this.costs.setDistance(1.0d);
                    this.costs.setNumberDays(this.getDays(assignment.getAssignmentDateOfDeparture(), assignment.getAssigmentReturnDate()));
                    this.costs.setRateTransport(taux.getRateTaxi());
                } /*else if (mission.getTransportType() == TransportType.Service) {
                    this.decompte.setTauxKilometrique(0.0);
                    this.decompte.setDistance(destination.getDistance());
                }*/
            }
        }
    }

    private void fillSejour(LocalDateTime startDate, LocalDateTime endDate) {
        int petitDejouner = 0;
        int dejounerDiner = 0;

        int startHour = startDate.getHour();
        int endHour = endDate.getHour();
//        System.out.println("startHour = " + startHour + " endHour = " + endHour);
        //Not the same day
        if (!startDate.toLocalDate().equals(endDate.toLocalDate())) {
            if (startHour <= 8) {
                petitDejouner++;
                dejounerDiner++;
                dejounerDiner++;
            } else if (startHour <= 14) {
                dejounerDiner++;
                dejounerDiner++;
            } else if (startHour <= 22) {
                dejounerDiner++;
            }
            if (endHour > 20) {
                petitDejouner++;
                dejounerDiner++;
                dejounerDiner++;
            } else if (endHour > 12) {
                petitDejouner++;
                dejounerDiner++;
            } else if (endHour > 06) {
                petitDejouner++;
            }
        } else {
            //same day
            if (startHour <= 8) {
                petitDejouner++;
                if (endHour > 20) {
                    dejounerDiner++;
                    dejounerDiner++;
                } else if (endHour > 12) {
                    dejounerDiner++;
                }
            }
        }

        long days = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        //days between start and end date
        if (days >= 1) days--;
        petitDejouner += days;
        dejounerDiner += days * 2;

        this.costs.setNumberBreakfast(petitDejouner);
        this.costs.setNumberLunchAndDinner(dejounerDiner);
        if (!startDate.toLocalDate().equals(endDate.toLocalDate())) {
            days++;
        }
        this.costs.setNumberAccommodation(days);
//        System.out.println("Petit Dejouner = " + petitDejouner + "  Dejouner diner = " + dejounerDiner + " Days = " + (days+1));
    }

    private long getDays(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
    }
}
