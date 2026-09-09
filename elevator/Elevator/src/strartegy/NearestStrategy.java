package strartegy;

import java.util.List;

import enums.ElevatorDir;
import enums.HallCalldir;
import models.Elevator;
import models.HallRequest;

public class NearestStrategy implements Selection {

    @Override
    public Elevator select(List<Elevator> elevators, HallRequest hallReq) {
        Elevator c1 = null;
        Elevator c2 = null;
        Elevator c3 = null;
        int minDist = Integer.MAX_VALUE;
        for (Elevator ele : elevators) {
            if (hallReq.getDir() == HallCalldir.UP && ele.getCurrDir() == ElevatorDir.EUP) {
                int diff = hallReq.getFloor() - ele.getCurrFLoor();
                if (hallReq.getFloor() >= ele.getCurrFLoor() && diff < minDist) {
                    minDist = diff;
                    c1 = ele;
                }

            } else if (hallReq.getDir() == HallCalldir.DOWN && ele.getCurrDir() == ElevatorDir.EDOWN) {
                int diff = ele.getCurrFLoor() - hallReq.getFloor();
                if (hallReq.getFloor() <= ele.getCurrFLoor() && diff < minDist) {
                    minDist = diff;
                    c1 = ele;
                }
            }
            if (c2 == null && ele.getCurrDir() == ElevatorDir.EIDLE) {
                c2 = ele;
            }
            if (Math.abs(ele.getCurrFLoor() - hallReq.getFloor()) < minDist) {
                c3 = ele;
            }

        }

        return c1 != null ? c1 : (c2 != null ? c2 : c3);
    }

}
