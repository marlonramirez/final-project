package co.edu.usbcali.finalproject.domain;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;

import java.util.List;

/**
 * Created by Marlon.Ramirez on 10/03/2018.
 */

public class RouteLogic {
    private static RouteLogic instance;

    public static RouteLogic getInstance() {
        if (instance == null) {
            instance = new RouteLogic();
        }
        return instance;
    }

    public List<LatLng> getDirection(LatLng origin, LatLng destination) {
        List<LatLng> routes = null;
        try {
            JsonObject jsonObject = new Route().execute(
                    String.valueOf(origin.latitude),
                    String.valueOf(origin.longitude),
                    String.valueOf(destination.latitude),
                    String.valueOf(destination.longitude)
            ).get();
            JsonElement route = jsonObject.get("routes").getAsJsonArray().get(0);
            JsonElement polyline = route.getAsJsonObject().get("overview_polyline");
            routes = PolyUtil.decode(polyline.getAsJsonObject().get("points").getAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    public PolylineOptions getRoute(LatLng origin, LatLng destination) {
        List<LatLng> routes = this.getDirection(origin, destination);
        PolylineOptions polylineOptions = null;
        int size = routes.size();
        if(size > 0) {
            polylineOptions = new PolylineOptions();
            polylineOptions.width(5);
            polylineOptions.color(Color.MAGENTA);
            for (int i = 0; i < size; i++) {
                polylineOptions.add(routes.get(i));
            }
        }
        return polylineOptions;
    }
}
