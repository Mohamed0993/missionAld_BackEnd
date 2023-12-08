package ci.projet.gestmissionbackend.enums;

public enum PersonalClass {
    A("A"),B("B"),C("C");
    private String label;

    PersonalClass(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
