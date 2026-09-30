package it.unimib.socialmesh.model;
import java.io.Serializable;

public class LatLong implements Serializable {
    private double latitude;
    private double longitude;
    public LatLong(LatLong latlong) {
        this.latitude = latlong.getLatitude();
        this.longitude = latlong.getLongitude();
    }


    public LatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}