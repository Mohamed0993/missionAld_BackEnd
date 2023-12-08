package ci.projet.gestmissionbackend.services;

import ci.projet.gestmissionbackend.dtos.TauxDTO;
import ci.projet.gestmissionbackend.entities.Taux;

public interface TauxService {
    TauxDTO save(TauxDTO tauxDTO);
    TauxDTO getTaux();
}
