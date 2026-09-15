package models;

import java.util.Comparator;
import java.util.TreeSet;

import enums.ElevatorDir;

public class Elevator {

    private int id;
    private ElevatorDir currDir;
    private int currFLoor;
    private int targetFloor;

    // Pending stops while heading up, ordered so the NEXT stop (lowest floor above
    // current) is always first — classic LOOK-algorithm per-elevator set.
    // TreeSet, not PriorityQueue: a floor pressed twice is still ONE stop. A PQ
    // would keep both copies and the second one, left behind after the first is
    // served, would pin the elevator at the end of the shaft forever.
    private TreeSet<Integer> upQueue;

    // Pending stops while heading down — reverse order, so the next stop (highest
    // floor below current) is first.
    private TreeSet<Integer> downQueue;

    public Elevator(int id) {
        this.id = id;
        this.currDir = ElevatorDir.EIDLE;
        this.upQueue = new TreeSet<>();
        this.downQueue = new TreeSet<>(Comparator.reverseOrder());
    }

    public TreeSet<Integer> getUpQueue() {
        return upQueue;
    }

    public TreeSet<Integer> getDownQueue() {
        return downQueue;
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
