package dpmc.health.system.models;

public class RegisterPerson {
    private String personName;
    private String cpf;
    private String phone;
    private String birthDate;
    private String email;
    private String birthCity;

    public RegisterPerson(String personName, String cpf, String phone, String birthDate, String email, String birthCity) {
        this.personName = personName;
        this.cpf = cpf;
        this.phone = phone;
        this.birthDate = birthDate;
        this.email = email;
        this.birthCity = birthCity;
    }
}
