package ci.projet.gestmissionbackend.Component;

import ci.projet.gestmissionbackend.dtos.TauxDTO;
import ci.projet.gestmissionbackend.entities.Assignment;
import ci.projet.gestmissionbackend.entities.Costs;
import ci.projet.gestmissionbackend.entities.Personal;
import ci.projet.gestmissionbackend.enums.MovingMeans;
import ci.projet.gestmissionbackend.repositories.PersonalRepository;
import ci.projet.gestmissionbackend.services.TauxService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.format.DateTimeFormatter;

@Component
public class Excel {

    @Autowired
    private TauxService taux;

    @Autowired
    private MSG msg;

    private Costs costs;
    private Assignment assignment;
    private final String nom = "C10";
    private final String matricule = "H10";
    private final String idMission = "C12";
    private final String depart = "E12";
    private final String destination = "G12";
    private final String startDate = "C14";
    private final String startHour = "E14";
    private final String endDate = "G14";
    private final String endHour = "J14";
    private final String transportType = "C16";
    private final String distance = "H16";
    private final String distanceAjouter = "I16";
    private final String tauxDejounerDiner = "C18";
    private final String tauxPetitDejouner = "H18";
    private final String tauxHebergement = "C20";
    private final String tauxKilometrique = "H20";
    private final String petitDejouner = "C25";
    private final String dejounerDiner = "C27";
    private final String hebergement = "C31";
    private final String total = "E34";
    private final String totalOnWords = "D38";
    private final String tickAuto = "F27";
    private final String nombreTickAuto = "G27";
    private final String tauxAuto = "H27";
    private final String imputation = "D40";
    private final String exercice = "D42";
    private final String totalTransportType = "J25";

    private Sheet sheet;
    @Autowired
    private PersonalRepository personalRepository;

    public void generateExcel(Costs costs){
        this.costs = costs;
        this.assignment = costs.getAssignment();
        File downloadDir = new File(msg.getMessage("application.mission.downloaddir"));
        if(!downloadDir.exists()){
            downloadDir.mkdir();
        }
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream(downloadDir.getPath() + "/modelALD.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
            //Ecriture du contenu
            this.setHeaderInformations();
            if(this.assignment.getMovingMeans() == MovingMeans.VEHICLE){
                this.fillAuto();
            } else if (assignment.getMovingMeans() == MovingMeans.TRAIN
                    || assignment.getMovingMeans() == MovingMeans.PublicTransport
                    || assignment.getMovingMeans() == MovingMeans.PLANE) {
                this.fillTaxi();

            }
            this.fillSejour();
            XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
            this.fillTotalWord();

            //Generate Output
            FileOutputStream outputStream = new FileOutputStream(downloadDir.getPath()+"/"+ assignment.getAssignmentId()+".xlsx");
            workbook.write(outputStream);
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void  setHeaderInformations(){
        Personal personal = assignment.getPersonal();
        TauxDTO taux = this.taux.getTaux();
        this.setCellValue(nom, personal.getPersonalFullName() + " " + personal.getPersonalName());
        this.setCellValue(matricule, personal.getPersonalMatricule());
        this.setCellValue(idMission, assignment.getAssignmentId());
        this.setCellValue(startDate, assignment.getAssignmentDateOfDeparture().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        this.setCellValue(startHour, assignment.getAssignmentDateOfDeparture().format(DateTimeFormatter.ofPattern("HH:mm")));
        this.setCellValue(endDate, assignment.getAssigmentReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        this.setCellValue(endHour, assignment.getAssigmentReturnDate().format(DateTimeFormatter.ofPattern("HH:mm")));
        this.setCellValue(transportType, assignment.getMovingMeans().toString());
        this.setCellValue(distanceAjouter, this.costs.getDistanceAdded());//TODO
        this.setCellValue(distance, this.costs.getDistance());
        this.setCellValue(tauxDejounerDiner, this.costs.getRateLunchAndDiner());
        this.setCellValue(tauxPetitDejouner, this.costs.getRateBreakfast());
        this.setCellValue(tauxHebergement, this.costs.getRateAccommodation());
        this.setCellValue(tauxKilometrique, this.costs.getRateKilometer());


    }

    private void fillSejour() {
        this.setCellValue(this.petitDejouner, this.costs.getNumberBreakfast());
        this.setCellValue(this.dejounerDiner, this.costs.getNumberLunchAndDinner());
        this.setCellValue(this.hebergement, this.costs.getNumberAccommodation());
    }

    private void fillAuto() {
        this.setCellValue(tickAuto, msg.getMessage("decompte.tickAuto"));
        this.setCellValue(nombreTickAuto, this.costs.getNumberTickAuto());// TODO
        this.setCellValue(tauxAuto, this.costs.getRateAuto());
    }

    private void fillTaxi() {
        this.setCellValue(tickAuto, msg.getMessage("decompte.taxi"));
        this.setCellValue(nombreTickAuto, this.costs.getNumberDays());// TODO
        this.setCellValue(tauxAuto, this.costs.getRateAuto());
    }



    private double getCellValue(String ref){
        CellReference cellReference = new CellReference(ref);
        Cell cell = sheet.getRow(cellReference.getRow()).getCell(cellReference.getCol());
        return cell.getNumericCellValue();
    }

    private void setCellValue(String ref, String value){
        CellReference cellReference = new CellReference(ref);
        Cell cell = sheet.getRow(cellReference.getRow()).getCell(cellReference.getCol());
        cell.setCellValue(value);
    }

    private void setCellValue(String ref, int value){
        CellReference cellReference = new CellReference(ref);
        Cell cell = sheet.getRow(cellReference.getRow()).getCell(cellReference.getCol());
        cell.setCellValue(value);
    }
    private void setCellValue(String ref, double value){
        CellReference cellReference = new CellReference(ref);
        Cell cell = sheet.getRow(cellReference.getRow()).getCell(cellReference.getCol());
        cell.setCellValue(value);
    }
    private void fillTotalWord(){
        double totalPrice = getCellValue(total);
        if(Math.ceil(totalPrice) < totalPrice + 0.001){
            totalPrice = Math.ceil(totalPrice);
        }
        System.out.println("Total =" + totalPrice);
        long entier = (long) Math.floor(totalPrice);
        long decimal = (long) Math.floor(totalPrice);
        String res = NumberToWord.convert(entier);
        if(decimal !=0){
            res +=" " + " Francs CFA.";
        }
        //Captlize firts letter
        res = res.substring(0,1).toUpperCase()+ res.substring(1);
        System.out.println(res);
        this.setCellValue(totalOnWords, res);

    }




}
