package dpmc.health.system.interfaces;

import dpmc.health.system.models.MedicinesPaginatedView;
import dpmc.health.system.models.RegisterMedicines;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MedicationsClient {
    @GET("/medicines")
    Call<MedicinesPaginatedView> getMedicines(@Query("page") int page, @Query("limit") int limit);

    @POST("/medicines")
    Call<RegisterMedicines> registerMedicines(@Body RegisterMedicines registerMedicines);
}
