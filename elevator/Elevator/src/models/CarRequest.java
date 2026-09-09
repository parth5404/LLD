package models;

public class CarRequest {
    private int elevatorId;
    private int targetFloor;

    public CarRequest(int elevatorId, int targetFloor) {
        this.elevatorId = elevatorId;
        this.targetFloor = targetFloor;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

}
