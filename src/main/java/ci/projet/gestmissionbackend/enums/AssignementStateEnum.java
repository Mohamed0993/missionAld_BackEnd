package ci.projet.gestmissionbackend.enums;

public enum AssignementStateEnum {
    CURRENT("En cours"),
    VCHEF("Valider par le chef de service"),
    VDG("Valider par le directeur Générale"),
    VDTYPE("Valider par "),
    RCHEF("Refuser par le chef de service"),
    RDG("Refuser par le directeur generale"),
    RDTYPE("Refuser par "),
    DAF("Valider par DAF"),
    VALIDATED("VALIDATED"),
    CANCELED("Annuler");

    private String label;

    AssignementStateEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
