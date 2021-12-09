package dpmc.health.system.models;

import java.security.Timestamp;

public class MedicalConsultation {
    private int ID;
    private String consultationDate;
    private String reason;
    private String diagnose;
    private String observations;
    private int idPerson;

    public MedicalConsultation() {
    }

    public int getID() {
        return ID;
    }

    public String getConsultationDate() {
        return consultationDate;
    }

    public String getReason() {
        return reason;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public String getObservations() {
        return observations;
    }

    public int getIdPerson() {
        return idPerson;
    }
}
