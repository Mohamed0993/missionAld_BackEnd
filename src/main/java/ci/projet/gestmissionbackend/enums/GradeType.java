package ci.projet.gestmissionbackend.enums;

public enum GradeType {
    DG("Directeur General"), DA("Directeur Adjoint"), CHEF("CHEF DE SERVICE"), ASSISTANT("Assistant(e)"), AUTRE("AUTRE"), DGA("DGA"), CHEFA("CHEFA"),DAF("Directeur Administratif et Financier");

    private String label;

    GradeType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
