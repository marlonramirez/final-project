package co.edu.usbcali.finalproject.domain;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Marlon.Ramirez on 10/03/2018.
 */

public class Route  extends AsyncTask<String, Integer, JsonObject> {
    private final String key = "AIzaSyC2T5Tt7rnoi76dvBuI20CIgiTo_huav00";

    @Override
    protected JsonObject doInBackground(String... strings) {
        try {
            Retrofit retrofitInstance = new Retrofit.Builder().baseUrl(Config.URL_BASE.getMaps())
                    .addConverterFactory(GsonConverterFactory.create()).build();
            IConnexionRest connexionRest = retrofitInstance.create(IConnexionRest.class);
            Call<JsonObject> call = connexionRest.getDirection(strings[0] + "," + strings[1], strings[2] + "," + strings[3], "es", "driving", key);
            return call.execute().body();
        } catch (Exception ex) {
            Log.e("error", "Error consultando servicio retrofit " + ex.getMessage());
        }
        return null;
    }
}
