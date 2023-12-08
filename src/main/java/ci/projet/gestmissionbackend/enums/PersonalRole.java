package ci.projet.gestmissionbackend.enums;

public enum PersonalRole {
        ADMIN("ADMIN"), USER("USER");

        private String label;

    PersonalRole(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

}
