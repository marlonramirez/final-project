package co.edu.usbcali.finalproject.domain;

/**
 * Created by Marlon.Ramirez on 16/02/2018.
 */

public enum Config {
    URL_BASE("https://maps.googleapis.com/");

    public String urlBase;

    Config(String url) {
        this.urlBase = url;
    }

    public String getMaps() {
        return urlBase + "maps/api/";
    }
}
