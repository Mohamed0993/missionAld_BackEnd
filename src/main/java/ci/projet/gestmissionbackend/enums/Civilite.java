package ci.projet.gestmissionbackend.enums;

public enum Civilite {
    M("Monsieur"),MME("Madame");

    private String label;

    Civilite(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
