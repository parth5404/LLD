package models;

import java.util.Collections;
import java.util.PriorityQueue;

import enums.ElevatorDir;

public class Elevator {

    private int id;
    private ElevatorDir currDir;
    private int currFLoor;
    private int targetFloor;
    private PriorityQueue<Integer> upQueue;

    public PriorityQueue<Integer> getUpQueue() {
        return upQueue;
    }

    private PriorityQueue<Integer> downQueue;

    public PriorityQueue<Integer> getDownQueue() {
        return downQueue;
    }

    public Elevator(int id) {
        this.id = id;
        this.currDir = ElevatorDir.EIDLE;
        this.upQueue = new PriorityQueue<>();
        this.downQueue = new PriorityQueue<>(Collections.reverseOrder());
    }

    public int getId() {
        return id;
    }

    public ElevatorDir getCurrDir() {
        return currDir;
    }

    public void setCurrDir(ElevatorDir currDir) {
        this.currDir = currDir;
    }

    public int getCurrFLoor() {
        return currFLoor;
    }

    public void setCurrFLoor(int currFLoor) {
        this.currFLoor = currFLoor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }

}
