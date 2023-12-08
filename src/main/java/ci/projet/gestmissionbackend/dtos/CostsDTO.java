package ci.projet.gestmissionbackend.dtos;

import ci.projet.gestmissionbackend.entities.Assignment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Data
public class CostsDTO {

    private Long costsId;
    private double distance;
    private double distanceAdded;
    private long numberTickAuto;
    private double rateAuto;
    private double rateBreakfast;
    private double rateLunchAndDiner;
    private double rateAccommodation;
    private double rateKilometer;
    private double rateTransport;
    private long numberDays;
    private long numberBreakfast;
    private long numberLunchAndDinner;
    private long numberAccommodation;
    private double kilometer;
    private double amount;
    private String description;
    private Date paymentDate;
    private Assignment assignment;
}
