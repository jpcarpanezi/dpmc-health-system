package dpmc.health.system.models;

public class Address {
    private int ID;
    private String street;
    private String complement;
    private String city;
    private String state;
    private String zipCode;
    private int idPerson;

    public Address() {
    }

    public int getID() {
        return ID;
    }

    public String getStreet() {
        return street;
    }

    public String getComplement() {
        return complement;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public int getIdPerson() {
        return idPerson;
    }
}
