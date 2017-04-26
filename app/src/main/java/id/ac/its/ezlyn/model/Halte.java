package id.ac.its.ezlyn.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Ilham Aulia Majid on 20-Apr-17.
 */

public class Halte {

    public static final Halte KEPUTIH1 = new Halte("Halte 1",0, -7.279890,112.784973);
    public static final Halte KEPUTIH2 = new Halte("Halte 2",0, -7.279337,112.789393);
    public static final Halte KEPUTIH3 = new Halte("Halte 3",0, -7.278337,112.789393);
    public static final Halte KEPUTIH4 = new Halte("Halte 4",0, -7.277337,112.789393);

    private String name;
    private int waiting;
    private LatLng latLng;

    public Halte(String name,int waiting, double lat, double lng) {
        this.name = name;
        this.waiting = waiting;
        this.latLng = new LatLng(lat, lng);
    }

    public String getName() {
        return name;
    }

    public String getWaiting() {
        return String.format("Penunggu : %s", waiting);
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
