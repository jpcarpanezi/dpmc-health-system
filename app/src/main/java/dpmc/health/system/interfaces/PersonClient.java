package dpmc.health.system.interfaces;

import dpmc.health.system.models.Person;
import dpmc.health.system.models.RegisterPerson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PersonClient {
    @GET("/person/item")
    Call<Person> searchPerson(@Query("selectBy") String searchMethod, @Query("value") String searchParameter);

    @POST("/person")
    Call<RegisterPerson> insertPerson(@Body RegisterPerson registerPerson);
}
