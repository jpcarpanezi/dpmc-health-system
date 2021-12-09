package dpmc.health.system.models;

import java.util.List;

public class MedicinesPaginatedView {
    private int totalItems;
    private Medicines medicines;
    private int totalPages;
    private int currentPage;

    public MedicinesPaginatedView() { }

    public int getTotalItems() {
        return totalItems;
    }

    public Medicines getMedicines() {
        return medicines;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
