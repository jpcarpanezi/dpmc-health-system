package dpmc.health.system.interfaces;

import dpmc.health.system.models.MedicinesPaginatedView;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ManageMedicationsClient {
    @GET("/medicines")
    Call<MedicinesPaginatedView> getMedicines(
            @Query("page") int page,
            @Query("limit") int limit
    );
}
