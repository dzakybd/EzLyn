package id.ac.its.ezlyn.model;

/**
 * Created by Ilham Aulia Majid on 19-Apr-17.
 */

public class Lyn {

    LynType type;
    private String plate;
    private String eta;
    private String status;

    public Lyn(LynType type, String plate, String eta, String status) {
        this.type = type;
        this.plate = plate;
        this.eta = eta;
        this.status = status;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LynType getType() {
        return type;
    }

    public void setType(LynType type) {
        this.type = type;
    }
}
