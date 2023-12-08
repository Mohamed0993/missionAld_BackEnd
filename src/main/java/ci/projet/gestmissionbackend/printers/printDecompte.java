package ci.projet.gestmissionbackend.printers;

import ci.projet.gestmissionbackend.Component.MSG;
import ci.projet.gestmissionbackend.dtos.CostsDTO;
import ci.projet.gestmissionbackend.entities.Costs;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Component
public class printDecompte {
    @Autowired
    private MSG msg;

    final Font DEFAULT_FONT = FontFactory.getFont(FontFactory.TIMES, 12, new Color(0,0,0));
    final Font DEFAULT_FONT_BOLD = FontFactory.getFont(FontFactory.TIMES_BOLD, 13, new Color(0,0,139));
    final Font DEFAULT_FONT_BOLD_NO_COLOR = FontFactory.getFont(FontFactory.TIMES_BOLD, 13);

     public void printLiquidation (Costs costs) throws IOException {
        Document document = new Document(PageSize.A4);
        File downloadDir = new File(msg.getMessage("application.mission.downloaddir"));
        if (!downloadDir.exists()) {
            downloadDir.mkdir();
        }
        PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(downloadDir.getPath() + "/" + costs.getCostsId() + ".pdf")));
        document.open();

        // header
        Table tableHeader = new Table(5, 1);
        float[] widths = {1, 2, 2, 1, 1};
        tableHeader.setWidth(100);
        tableHeader.setWidths(widths);

        //tableHeader.setBorder(0);
        tableHeader.setPadding(2);
        //tableHeader.setSpacing(2);

        com.lowagie.text.Image image = Image.getInstance(downloadDir.getPath() + "/logo.png");

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
        phrase = new Phrase("SERVICE : FINANCIER", DEFAULT_FONT_BOLD);
        cell = new Cell(phrase);
        //cell.setBorder(0);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setRowspan(2);
        cell.setColspan(2);
        tableHeader.addCell(cell);

        phrase = new Phrase(msg.getMessage("decompte.title"), DEFAULT_FONT_BOLD);
        cell = new Cell(phrase);
        // cell.setBorder(0);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);

        cell.setColspan(2);
        tableHeader.addCell(cell);

        phrase = new Phrase("REF: ERG 01 - P07", DEFAULT_FONT_BOLD);
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

        Paragraph paragraph = new Paragraph("Monsieur, Mdme, Mlle : ", DEFAULT_FONT);
        Phrase phrase1 = new Phrase ( costs.getAssignment().getPersonal().getPersonalName() + " "
                + costs.getAssignment().getPersonal().getPersonalFullName(), DEFAULT_FONT );
        paragraph.add(phrase1);
        Phrase phrase2 = new Phrase( costs.getAssignment().getPersonal().getPersonalMatricule());
        paragraph.add(phrase2);


        paragraph.setSpacingBefore(5);
        paragraph.setSpacingAfter(5);
        document.add(paragraph);

        Paragraph paragraph2 = new Paragraph("Ordre de mission N°: ", DEFAULT_FONT);
        Phrase phrase3 = new Phrase ( costs.getCostsId().toString() + "                   du : " + costs.getAssignment().getAssignmentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                "              Destination : ", DEFAULT_FONT );
        paragraph2.add(phrase3);
        paragraph2.setSpacingBefore(5);
        paragraph2.setSpacingAfter(5);
        document.add(paragraph2);

        Paragraph paragraph3 = new Paragraph();
        Phrase phrase4 = new Phrase ( msg.getMessage("mission.pdf.startdate")+" : " + costs.getAssignment().getAssignmentDateOfDeparture().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +"                       "+
                                                                         " Date de retour : " + costs.getAssignment().getAssigmentReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ,DEFAULT_FONT );
        paragraph3.add(phrase4);
        paragraph3.setSpacingBefore(5);
        paragraph3.setSpacingAfter(5);
        document.add(paragraph3);

        /*Paragraph paragraph4 = new Paragraph();
        Phrase phrase5 = new Phrase ( msg.getMessage("mission.pdf.transport")+" utilisé : " + costs.getAssignment().getMovingMeans() +"      "+ msg.getMessage("decompte.distance")+" : "
                + costs.getDistance()*2 + " Kilomètre(s)" ,DEFAULT_FONT );
        paragraph4.add(phrase5);
        paragraph4.setSpacingBefore(5);
        paragraph4.setSpacingAfter(5);
        document.add(paragraph4);

        Paragraph paragraph6 = new Paragraph();
        Phrase phrase7 = new Phrase ( msg.getMessage("decompte.tauxDejounerDiner")+" : " + costs.getRateLunchAndDiner() +" fcfa                "+ msg.getMessage("decompte.tauxPetitDejouner")+" : "
                + costs.getRateBreakfast() + " fcfa" ,DEFAULT_FONT );
        paragraph6.add(phrase7);
        paragraph6.setSpacingBefore(5);
        paragraph6.setSpacingAfter(5);
        document.add(paragraph6);

        Paragraph paragraph7 = new Paragraph();
        Phrase phrase8 = new Phrase ( msg.getMessage("decompte.tauxHebergement")+" :  " + costs.getRateAccommodation() +" fcfa                "+ msg.getMessage("decompte.tauxKilometrique")+"  :  "
                + costs.getRateKilometer() + " fcfa " ,DEFAULT_FONT );
        paragraph7.add(phrase8);
        paragraph7.setSpacingBefore(5);
        paragraph7.setSpacingAfter(5);
        document.add(paragraph7);*/

        // Table
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);

        //PdfPCell cell1 = new PdfPCell(new Phrase(msg.getMessage("decompte.subHeader1"), FontFactory.getFont(FontFactory.HELVETICA, 18)));

        //line 1
         PdfPCell cell1 = new PdfPCell(new Paragraph(msg.getMessage("decompte.subHeader1"), DEFAULT_FONT ));
         cell1.setColspan(4);
         cell1.setPaddingTop(5);
         cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
         table.addCell(cell1);
         cell1 = new PdfPCell(new Paragraph(msg.getMessage("decompte.subHeader2"), DEFAULT_FONT ));
         cell1.setColspan(4);
         cell1.setPaddingTop(5);
         cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
         table.addCell(cell1);

         //line 2
        cell1 = new PdfPCell(new Paragraph("Unité", DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph("Nombre", DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph("Taux", DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph("Total 1", DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph("Moyen", DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph("Distance", DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph("Taux", DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph("Total 2", DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);

        // line 3

        cell1 = new PdfPCell(new Paragraph(msg.getMessage("decompte.petitDejouner"), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(String.valueOf(costs.getNumberBreakfast()), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(String.valueOf(costs.getRateBreakfast()), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(String.valueOf(costs.getNumberBreakfast() * costs.getRateBreakfast()), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(" "+ costs.getAssignment().getMovingMeans(), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph( "" + costs.getDistance()*2, DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(" " + costs.getRateKilometer(), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(" "+ (costs.getDistance()*2)*costs.getRateKilometer(), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);

        // line 4

        cell1 = new PdfPCell(new Paragraph(msg.getMessage("decompte.dejounerDiner"), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(String.valueOf(costs.getNumberLunchAndDinner()), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(String.valueOf(costs.getRateLunchAndDiner()), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(String.valueOf(costs.getNumberLunchAndDinner() * costs.getRateLunchAndDiner()), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(" TICK.AUTO" , DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph( "" + costs.getNumberTickAuto(), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(" " + costs.getRateAuto(), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(" "+ costs.getNumberTickAuto()*costs.getRateAuto(), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);

        //line 5
        cell1 = new PdfPCell(new Paragraph(msg.getMessage("decompte.hebergement"), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(String.valueOf(costs.getNumberAccommodation()), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(String.valueOf(costs.getRateAccommodation()), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(String.valueOf(costs.getNumberAccommodation() * costs.getRateAccommodation()), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);

        //line 6

        cell1 = new PdfPCell(new Paragraph("TOTAL SEJOUR", DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        cell.setColspan(3);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(String.valueOf(costs.getNumberAccommodation()), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph("TRANSPORT", DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);
        cell1 = new PdfPCell(new Paragraph(String.valueOf(costs.getNumberAccommodation() * costs.getRateAccommodation()), DEFAULT_FONT ));
        cell1.setPaddingTop(5);
        table.addCell(cell1);


        document.add(table);



        document.close();

    }
}
