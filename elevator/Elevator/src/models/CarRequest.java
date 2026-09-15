package models;

// A request made from INSIDE a specific elevator (cabin panel), once a passenger
// has boarded. Unlike HallRequest, the elevator is already fixed (elevatorId) —
// there's nothing to "select", it just needs to be queued on that elevator.
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
