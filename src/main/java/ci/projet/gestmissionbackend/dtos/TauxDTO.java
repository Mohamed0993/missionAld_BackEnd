package ci.projet.gestmissionbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TauxDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double rateDejounerDiner;
    private double ratePetitDejouner;
    private double rateHebergement;
    private double rateKilometrique;

    private double rateDejounerDinerDirec;
    private double ratePetitDejounerDirec;
    private double rateHebergementDirec;
    private double rateKilometriqueDirec;

    private double rateTaxi;
}
