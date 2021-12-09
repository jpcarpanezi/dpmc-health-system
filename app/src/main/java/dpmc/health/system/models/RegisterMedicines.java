package dpmc.health.system.models;

public class RegisterMedicines {
    private String drugName;
    private String activeIngredient;
    private String formRoute;
    private String company;

    public RegisterMedicines(String drugName, String activeIngredient, String formRoute, String company) {
        this.drugName = drugName;
        this.activeIngredient = activeIngredient;
        this.formRoute = formRoute;
        this.company = company;
    }
}
