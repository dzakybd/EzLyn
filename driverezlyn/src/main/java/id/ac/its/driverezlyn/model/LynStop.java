package id.ac.its.driverezlyn.model;

/**
 * Created by Ilham Aulia Majid on 19-Apr-17.
 */

public class LynStop {

    private String name;
    private int waiting;

    public LynStop(String name,int waiting) {
        this.name = name;
        this.waiting = waiting;
    }

    public String getName() {
        return name;
    }

    public String getWaiting() {
        return String.format("Penunggu : %s", waiting);
    }
}
