package com.inveniet;

import info.ConnectionDetector;

/**
 * Created by Armin on 2015-07-28.
 */
public class Methods {

    public String source = "Promenada"; //nazwa lokalu (pobrana z wyboru mapy)
    public String[] placepic; //link do zdjęcia lokalu
    public String[] promopic; //link do zdjęcia promocji
    public String nazwa; // informacje o nazwie lokalizacji
    public String info; // informacje o lokalizacji z SQL'a
    public String infopromo; // informacje o lokalizacji z SQL'a
    public String kontakt; // kontakt do lokalizacji
    public String godziny; // informacje o godzinach pracy lokalizacji
    public double longitude; //dlugość geograficzna
    public double latitude; //szerokość geograficzna
    public String ApiConnector_url;
    public boolean jestsiec = false;
    ConnectionDetector sprawdzsiec;

    public String getApiConnector_url() {
        return ApiConnector_url;
    }

    public void setApiConnector_url(String apiConnector_url) {
        ApiConnector_url = apiConnector_url;
    }

    public String getSource() {
        source = "Promenada";
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getInfopromo() {
        return infopromo;
    }

    public void setInfopromo(String infopromo) {
        this.infopromo = infopromo;
    }

    public String getGodziny() {
        return godziny;
    }

    public void setGodziny(String godziny) {
        this.godziny = godziny;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public String[] getPromopic() {
        return promopic;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String[] getPlacepic() {
        return placepic;
    }

    public void setPlacepic(String[] placepic) {
        this.placepic = placepic;
    }

    public void setPromopic(String[] promopic) {
        this.promopic = promopic;
    }
}
