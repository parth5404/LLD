package controller;

import java.util.List;

import enums.ElevatorDir;
import enums.HallCalldir;
import models.CarRequest;
import models.Elevator;
import models.HallRequest;
import strartegy.Selection;

public class ElevatorController {
    private List<Elevator> elevators;
    private Selection selectionStrategy;
    private static final int TOP_FLOOR = 9;

    public ElevatorController(List<Elevator> elevators, Selection selectionStrategy) {
        this.elevators = elevators;
        this.selectionStrategy = selectionStrategy;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    // Advances the whole simulation by one discrete step (FR#4). Order matters:
    // check-arrival-and-dequeue happens BEFORE moving, so "arrived" means the
    // elevator got to this floor on a PREVIOUS tick and is now serving the stop.
    public void tick() {
        for (Elevator ele : elevators) {
            if (!ele.getUpQueue().isEmpty() && ele.getCurrFLoor() == ele.getUpQueue().first()) {
                ele.getUpQueue().pollFirst();
            }
            if (!ele.getDownQueue().isEmpty() && ele.getCurrFLoor() == ele.getDownQueue().first()) {
                ele.getDownQueue().pollFirst();
            }

            // NOTE (known simplification): once both queues for the CURRENT direction
            // are empty we go straight to IDLE, even if the OTHER direction's queue
            // still has pending stops. A fuller LOOK algorithm would reverse direction
            // here instead of idling. Worth saying out loud in an interview.
            if (ele.getUpQueue().isEmpty() && ele.getDownQueue().isEmpty()) {
                ele.setCurrDir(ElevatorDir.EIDLE);
            }

            if (ele.getCurrDir() == ElevatorDir.EUP && ele.getCurrFLoor() < TOP_FLOOR) {
                ele.setCurrFLoor(ele.getCurrFLoor() + 1);
            } else if (ele.getCurrDir() == ElevatorDir.EDOWN && ele.getCurrFLoor() > 0) {
                ele.setCurrFLoor(ele.getCurrFLoor() - 1);
            }
        }
    }

    // Hall call (FR#2): ask the strategy WHICH elevator should go, then apply the
    // effects of that decision here — the strategy itself only decides, it never
    // mutates elevator state (keeps Selection implementations swappable and pure).
    public Elevator getBestElevator(HallRequest hallReq) {
        Elevator ele = selectionStrategy.select(elevators, hallReq);
        ElevatorDir dir = hallReq.getDir() == HallCalldir.UP ? ElevatorDir.EUP : ElevatorDir.EDOWN;

        if (ele.getCurrDir() == ElevatorDir.EIDLE) {
            ele.setCurrDir(dir);
        }

        // The elevator must physically stop at the hall-call floor to pick the
        // passenger up, even though it isn't a "destination" for anyone yet — so it
        // goes in the same queue that drives tick()'s stop-detection, same as a
        // CarRequest's target floor.
        if (dir == ElevatorDir.EUP) {
            ele.getUpQueue().add(hallReq.getFloor());
        } else {
            ele.getDownQueue().add(hallReq.getFloor());
        }

        return ele;
    }

    // Destination call (FR#3): the elevator is already fixed (elevatorId), so no
    // Selection strategy is involved at all — just enqueue the floor on it.
    // Invalid floor -> rejected by returning null (FR#6), not by throwing: an
    // out-of-range floor from a user is an expected case, not an exceptional one.
    public CarRequest carRequest(Elevator elevator, int floor) {
        if (floor > TOP_FLOOR || floor < 0) {
            return null;
        }

        if (elevator.getCurrDir() == ElevatorDir.EIDLE) {
            elevator.setCurrDir(floor >= elevator.getCurrFLoor() ? ElevatorDir.EUP : ElevatorDir.EDOWN);
        }

        if (floor >= elevator.getCurrFLoor()) {
            elevator.getUpQueue().add(floor);
        } else {
            elevator.getDownQueue().add(floor);
        }

        return new CarRequest(elevator.getId(), floor);
    }
}
