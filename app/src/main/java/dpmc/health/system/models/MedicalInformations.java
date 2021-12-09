package dpmc.health.system.models;

public class MedicalInformations {
    private int id;
    private String bloodType;
    private String medicalConditions;
    private String allergies;
    private String observations;

    public MedicalInformations() {
    }

    public int getId() {
        return id;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getObservations() {
        return observations;
    }
}
