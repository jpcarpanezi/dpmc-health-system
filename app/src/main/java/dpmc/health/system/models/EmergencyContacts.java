package dpmc.health.system.models;

public class EmergencyContacts {
    private int ID;
    private String emergencyName;
    private String phone;
    private String kinship;
    private int idPerson;

    public EmergencyContacts() {
    }

    public int getID() {
        return ID;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public String getPhone() {
        return phone;
    }

    public String getKinship() {
        return kinship;
    }

    public int getIdPerson() {
        return idPerson;
    }
}
