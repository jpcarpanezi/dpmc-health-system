package dpmc.health.system.models;

public class Prescriptions {
    private int ID;
    private int idPerson;
    private int idConsultation;
    private int idMedicine;
    private String dosage;

    public Prescriptions() {
    }

    public int getID() {
        return ID;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public int getIdConsultation() {
        return idConsultation;
    }

    public int getIdMedicine() {
        return idMedicine;
    }

    public String getDosage() {
        return dosage;
    }
}
