package models;

import enums.HallCalldir;

// A request made from OUTSIDE an elevator, at a floor's hallway panel. Only the
// source floor + desired direction are known here — the passenger's actual
// destination isn't known until they board and make a CarRequest.
public class HallRequest {

    private HallCalldir dir;
    private int floor;

    public HallRequest(HallCalldir dir, int floor) {
        this.dir = dir;
        this.floor = floor;
    }

    public HallCalldir getDir() {
        return dir;
    }

    public int getFloor() {
        return floor;
    }
}
