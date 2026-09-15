import java.util.ArrayList;
import java.util.List;

import controller.ElevatorController;
import enums.HallCalldir;
import models.CarRequest;
import models.Elevator;
import models.HallRequest;
import strartegy.NearestStrategy;

// Small manual driver to sanity-check the simulation end to end. Not a test suite —
// just prints state after every tick() so a run can be eyeballed.
public class App {
    public static void main(String[] args) throws Exception {
        List<Elevator> elevators = new ArrayList<>();
        elevators.add(new Elevator(0));
        elevators.add(new Elevator(1));
        elevators.add(new Elevator(2));

        ElevatorController controller = new ElevatorController(elevators, new NearestStrategy());

        System.out.println("-- initial state --");
        printState(controller);

        // Someone on floor 5 wants to go UP.
        HallRequest hallReq = new HallRequest(HallCalldir.UP, 5);
        Elevator dispatched = controller.getBestElevator(hallReq);
        System.out.println("\ndispatched elevator " + dispatched.getId() + " for hall call (floor 5, UP)");

        boolean carRequestMade = false;
        for (int i = 1; i <= 10; i++) {
            controller.tick();
            System.out.println("\n-- after tick " + i + " --");
            printState(controller);

            // Once the dispatched elevator reaches the hall-call floor, simulate the
            // passenger boarding and pressing floor 8 inside the cabin.
            if (!carRequestMade && dispatched.getCurrFLoor() == 5) {
                CarRequest carReq = controller.carRequest(dispatched, 8);
                carRequestMade = true;
                System.out.println("passenger boarded elevator " + dispatched.getId()
                        + ", requested floor " + carReq.getTargetFloor());
            }
        }
    }

    private static void printState(ElevatorController controller) {
        for (Elevator ele : controller.getElevators()) {
            System.out.println("elevator " + ele.getId()
                    + " | floor=" + ele.getCurrFLoor()
                    + " | dir=" + ele.getCurrDir()
                    + " | upQueue=" + ele.getUpQueue()
                    + " | downQueue=" + ele.getDownQueue());
        }
    }
}
