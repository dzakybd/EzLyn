package id.ac.its.ezlyn.model;

/**
 * Created by Ilham Aulia Majid on 19-Apr-17.
 */

public class Lyn {

    private String plate;
    private boolean full,status;

    public Lyn() {
    }

    public Lyn(String plate, boolean full, boolean status) {
        this.plate=plate;
        this.full=full;
        this.status=status;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPlate() {
        return plate;
    }

    public boolean isFull() {
        return full;
    }

    public boolean isStatus() {
        return status;
    }
}
