package ci.projet.gestmissionbackend.entities;

import ci.projet.gestmissionbackend.enums.MovingMeans;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Scope("prototype")
@Getter
@Setter


public class Costs {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToOne
    @JsonIgnore
    private Assignment assignment;

    @Transient
    public void calculeTotal() {
        double pd = numberBreakfast * rateBreakfast;
        double dd = numberLunchAndDinner * rateLunchAndDiner;
        double h = numberAccommodation * rateAccommodation;
        double k = rateKilometer * distance * 2.0;
        double a = 0d; //Auto
        double t = 0d; //Taxi
        if (assignment.getMovingMeans() == MovingMeans.VEHICLE) {
            a = rateAuto * numberTickAuto;
        } else if (assignment.getMovingMeans() == MovingMeans.TRAIN
                || assignment.getMovingMeans() == MovingMeans.PublicTransport
                || assignment.getMovingMeans() == MovingMeans.PLANE) {
            t = rateTransport * numberDays;
        }
        System.out.println(pd);
        System.out.println(dd);
        System.out.println(h);
        System.out.println(k);
        System.out.println(a);
        System.out.println(t);
        this.amount = pd + dd + h + k + a + t;
        System.out.println(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Costs costs = (Costs) o;
        return costsId != null && Objects.equals(costsId, costs.costsId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
