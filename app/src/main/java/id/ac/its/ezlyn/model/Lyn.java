package id.ac.its.ezlyn.model;

/**
 * Created by Ilham Aulia Majid on 19-Apr-17.
 */

public class Lyn {

    LynType type;
    private String plate;
    private int eta;
    private String status;

    public Lyn(LynType type, String plate, int eta, String status) {
        this.type = type;
        this.plate = plate;
        this.eta = eta;
        this.status = status;
    }

    public String getPlate() {
        return plate;
    }

    public String getEta() {
        return String.format("%s menit",eta);
    }

    public String getStatus() {
        return status;
    }

    public LynType getType() {
        return type;
    }
}
