package co.edu.usbcali.finalproject.domain;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Marlon.Ramirez on 16/02/2018.
 */

public interface IConnexionRest {

    @GET("directions/json")
    Call<JsonObject> getDirection(@Query("origin") String origin, @Query("destination") String destination, @Query("language") String language, @Query("mode") String mode, @Query("key") String key);
}
