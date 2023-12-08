package ci.projet.gestmissionbackend.printers;

import ci.projet.gestmissionbackend.Component.MSG;
import ci.projet.gestmissionbackend.dtos.*;
import ci.projet.gestmissionbackend.enums.MovingMeans;
import ci.projet.gestmissionbackend.exceptions.assignmentNotFoundException;
import ci.projet.gestmissionbackend.services.AssignmentService;
import ci.projet.gestmissionbackend.services.PersonalService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class printAssignment {

    @Autowired
    private MSG msg;

    /*@Autowired
    private AssignmentService assignmentService;*/

    final Font DEFAULT_FONT = FontFactory.getFont(FontFactory.TIMES, 14, new Color(0,0,0));
    final Font DEFAULT_FONT_BOLD = FontFactory.getFont(FontFactory.TIMES_BOLD, 13, new Color(0,0,139));
    final Font DEFAULT_FONT_BOLD_NO_COLOR = FontFactory.getFont(FontFactory.TIMES_BOLD, 13);

    public void printAssignment(AssignmentDTO assignmentDTO) throws IOException {
        //AssignmentDTO assignmentDTO = assignmentService.findAssignmentById(id);
        Document document = new Document(PageSize.A4);
        File downloadDir = new File(msg.getMessage("application.mission.downloaddir"));
        if (!downloadDir.exists()) {
            downloadDir.mkdir();
        }
        PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(downloadDir.getPath() + "/" + assignmentDTO.getUuid() + ".pdf")));
        document.open();

        // header
        Table tableHeader = new Table(5, 1);
        float[] widths = {1, 2, 2, 1, 1};
        tableHeader.setWidth(100);
        tableHeader.setWidths(widths);

        //tableHeader.setBorder(0);
        tableHeader.setPadding(2);
        //tableHeader.setSpacing(2);

        Image image = Image.getInstance(downloadDir.getPath() + "/logo.png");

        Cell cell = new Cell(image);

        //cell.setBorder(0);

        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);

        cell.setRowspan(3);
        tableHeader.addCell(cell);
        Phrase phrase = new Phrase("ENREGISTREMENT", DEFAULT_FONT_BOLD);
        cell = new Cell(phrase);
        // cell.setBorder(0);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setColspan(2);
        tableHeader.addCell(cell);
        phrase = new Phrase("SERVICE : LOGISTIQUE", DEFAULT_FONT_BOLD);
        cell = new Cell(phrase);
        //cell.setBorder(0);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setRowspan(2);
        cell.setColspan(2);
        tableHeader.addCell(cell);

        phrase = new Phrase("ORDRE DE MISSION", DEFAULT_FONT_BOLD);
        cell = new Cell(phrase);
        // cell.setBorder(0);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);

        cell.setColspan(2);
        tableHeader.addCell(cell);

        phrase = new Phrase("REF: ERG 01 - P06", DEFAULT_FONT_BOLD);
        cell = new Cell(phrase);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // cell.setBorder(0);
        tableHeader.addCell(cell);

        phrase = new Phrase("VERSION : 01", DEFAULT_FONT_BOLD);
        cell = new Cell(phrase);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // cell.setBorder(0);
        tableHeader.addCell(cell);
        //cell.setBorder(0);

        phrase = new Phrase("DATE : 06-05-22", DEFAULT_FONT_BOLD);
        cell = new Cell(phrase);
        //cell.setBorder(0);
        //cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        tableHeader.addCell(cell);
        phrase = new Phrase("PAGE : 1/1", DEFAULT_FONT_BOLD);
        cell = new Cell(phrase);
        //cell.setBorder(0);
        //cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        tableHeader.addCell(cell);


        document.add(tableHeader);

        // subHeader

        Paragraph paragraph = new Paragraph(msg.getMessage("mission.pdf.school") + "   " + assignmentDTO.getAssignmentId() + "      ", DEFAULT_FONT_BOLD_NO_COLOR);
        Phrase phrase1 = new Phrase(msg.getMessage("mission.pdf.city") + " " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), DEFAULT_FONT);
        paragraph.add(phrase1);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setSpacingBefore(10);
        paragraph.setSpacingAfter(10);
        document.add(paragraph);

        // title

        /*PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cellPdf = new PdfPCell(new Phrase(msg.getMessage("mission.pdf.title"), FontFactory.getFont(FontFactory.TIMES_BOLD, 18)));
        cellPdf.setBorderWidth(1);
        cellPdf.setPadding(10);

        cellPdf.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(cellPdf);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingAfter(15);
        document.add(table);*/

        // body


        Paragraph paragraph1 = new Paragraph("Prénom : ", FontFactory.getFont(FontFactory.TIMES_BOLD, 14));
        Phrase prenomEmp = new Phrase(assignmentDTO.getPersonalDTO().getPersonalFullName(), DEFAULT_FONT);
        Phrase nomEmp = new Phrase(assignmentDTO.getPersonalDTO().getPersonalName(), DEFAULT_FONT);
        paragraph1.add(prenomEmp);


        Table tableBody = new Table(3, 7);
        tableBody.setWidth(100);
        tableBody.setPadding(2);
        //tableBody.setSpacing();

        // Row 1
        // Col 1
        Phrase phrase2 = new Phrase("NOM ET PRENOM DE L'EMPLOYE", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
        Cell cell1 = new Cell(phrase2);
        //cell1.setHorizontalAlignment(Ho);
        tableBody.addCell(cell1);
        // Col 2-3
        cell1 = new Cell(new Phrase(assignmentDTO.getPersonalDTO().getPersonalName() + " " + assignmentDTO.getPersonalDTO().getPersonalFullName(), FontFactory.getFont(FontFactory.TIMES, 18)));
        cell1.setColspan(2);
        tableBody.addCell(cell1);

        // Row 2
        // Col 1
        Phrase phrase3 = new Phrase();
        if (assignmentDTO.getMovingMeans() != null) {
            if (assignmentDTO.getMovingMeans() == MovingMeans.VEHICLE) {
                phrase2 = new Phrase("ENGIN / VEHICULE", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
                for (VehicleDTO vehicleDTO : assignmentDTO.getVehicleDTOS()) {
                    phrase3 = new Phrase("Marque :  " + vehicleDTO.getVehicleMark() + " "+ vehicleDTO.getVehicleModel() + ",  Imma : " + vehicleDTO.getVehicleRegistration(), FontFactory.getFont(FontFactory.TIMES, 18));
                }
            } else {
                phrase2 = new Phrase("MOYEN DE DEPLACEMENT", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
                phrase3 = new Phrase(assignmentDTO.getMovingMeans().toString(), FontFactory.getFont(FontFactory.TIMES, 18));

            }
        }
        cell1 = new Cell(phrase2);
        tableBody.addCell(cell1);
        // Col 2-3
        cell1 = new Cell(phrase3);
        cell1.setColspan(2);
        tableBody.addCell(cell1);

        // Row 3
        // Col 1
        phrase2 = new Phrase("MOTIF ", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
        cell1 = new Cell(phrase2);
        tableBody.addCell(cell1);

        //Col 2-3
        phrase3 = new Phrase(assignmentDTO.getType().getLabel(), FontFactory.getFont(FontFactory.TIMES, 18));
        cell1 = new Cell(phrase3);
        cell1.setColspan(2);
        tableBody.addCell(cell1);

        // Row 4
        // Col 1
        phrase2 = new Phrase("DESTINATION ", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
        cell1 = new Cell(phrase2);
        tableBody.addCell(cell1);

        //Col 2-3
        for (WorksiteDTO worksiteDTO : assignmentDTO.getWorksiteDTOS()) {
            phrase3 = new Phrase("ABIDJAN - " + worksiteDTO.getTownDTO().getTownName(), FontFactory.getFont(FontFactory.TIMES, 18));
        }

        cell1 = new Cell(phrase3);
        cell1.setColspan(2);
        tableBody.addCell(cell1);


        // Row 5
        // Col 1
        phrase2 = new Phrase("DATE ET POINT DE DEPART  ", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
        cell1 = new Cell(phrase2);
        tableBody.addCell(cell1);

        //Col 2-3
        phrase3 = new Phrase("DU  " + assignmentDTO.getAssignmentDateOfDeparture().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " à ABIDJAN COCODY CITE DES ARTS", FontFactory.getFont(FontFactory.TIMES, 18));
        cell1 = new Cell(phrase3);
        cell1.setColspan(2);
        tableBody.addCell(cell1);

        // Row 6
        // Col 1
        phrase2 = new Phrase("DATE DE RETOUR  ", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
        cell1 = new Cell(phrase2);
        tableBody.addCell(cell1);

        //Col 2-3
        phrase3 = new Phrase(assignmentDTO.getAssigmentReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                , FontFactory.getFont(FontFactory.TIMES, 18));
        cell1 = new Cell(phrase3);
        cell1.setColspan(2);
        tableBody.addCell(cell1);


        // Row 7
        // Col 1
        phrase2 = new Phrase("NOMBRE DE JOUR(S) ", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
        cell1 = new Cell(phrase2);
        tableBody.addCell(cell1);

        //Col 2-3
        phrase3 = new Phrase(
                String.valueOf(getDays(assignmentDTO.getAssignmentDateOfDeparture(), assignmentDTO.getAssigmentReturnDate()))
                , FontFactory.getFont(FontFactory.TIMES, 18));

        cell1 = new Cell(phrase3);
        cell1.setColspan(2);
        tableBody.addCell(cell1);

        document.add(tableBody);


        if(assignmentDTO.getPersonalDTOS() != null){
            System.out.println(assignmentDTO.getPersonalDTOS());
            Table tablePersonals = new Table(3);
            tablePersonals.setWidth(100);
            tablePersonals.setPadding(3);
            phrase1 = new Phrase("Nom et prénom(s)", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
            Cell cellHeader = new Cell(phrase1);
            cellHeader.setHeader(true);
            cellHeader.setColspan(2);
            tablePersonals.addCell(cellHeader);
            phrase1 = new Phrase("Fonction", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
            tablePersonals.addCell(phrase1);
            tablePersonals.endHeaders();

            for(PersonalDTO personalDTO:assignmentDTO.getPersonalDTOS()){
                phrase1 = new Phrase(personalDTO.getPersonalName() + " " + personalDTO.getPersonalFullName(),
                        FontFactory.getFont(FontFactory.TIMES_BOLD, 14));
                Cell cell2 = new Cell(phrase1);
                cell2.setColspan(2);
                tablePersonals.addCell(cell2);
                phrase1 = new Phrase(personalDTO.getFonction(),
                        FontFactory.getFont(FontFactory.TIMES_BOLD, 14));
                tablePersonals.addCell(phrase1);
            }


            document.add(tablePersonals);
        }


        // signature

         paragraph = new Paragraph(
                 );
         phrase1 = new Phrase("Service Logistique", FontFactory.getFont(FontFactory.TIMES_BOLD, 18));
        paragraph.add(phrase1);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setSpacingBefore(20);
        paragraph.setSpacingAfter(10);
        document.add(paragraph);



        document.close();



    }

    private long getDays(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
    }
}
