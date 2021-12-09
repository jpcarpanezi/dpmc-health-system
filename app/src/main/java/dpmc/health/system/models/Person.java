package dpmc.health.system.models;

import java.util.Date;
import java.util.List;

public class Person {
    private int id;
    private String personName;
    private String cpf;
    private String phone;
    private Date birthDate;
    private String email;
    private String birthCity;
    private List<Prescriptions> prescriptions;
    private List<MedicalConsultation> medicalConsultations;
    private List<Address> addresses;
    private List<EmergencyContacts> emergencyContacts;
    private MedicalInformations medicalInformations;

    public int getId() {
        return id;
    }

    public String getPersonName() {
        return personName;
    }

    public String getCpf() {
        return cpf;
    }

    public String getPhone() {
        return phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public List<Prescriptions> getPrescriptions() {
        return prescriptions;
    }

    public List<MedicalConsultation> getMedicalConsultations() {
        return medicalConsultations;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public List<EmergencyContacts> getEmergencyContacts() {
        return emergencyContacts;
    }

    public MedicalInformations getMedicalInformations() {
        return medicalInformations;
    }

    public Person() {

    }
}
