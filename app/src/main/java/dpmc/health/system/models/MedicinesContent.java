package dpmc.health.system.models;

public class MedicinesContent {
    private int id;
    private String drugName;
    private String activeIngredient;
    private String formRoute;
    private String company;

    public MedicinesContent() {
    }

    public int getId() {
        return id;
    }

    public String getDrugName() {
        return drugName;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public String getFormRoute() {
        return formRoute;
    }

    public String getCompany() {
        return company;
    }
}
