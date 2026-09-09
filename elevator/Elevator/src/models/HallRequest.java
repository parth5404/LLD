package models;

import enums.HallCalldir;

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
