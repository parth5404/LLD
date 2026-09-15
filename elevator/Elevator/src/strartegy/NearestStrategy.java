package strartegy;

import java.util.List;

import enums.ElevatorDir;
import enums.HallCalldir;
import models.Elevator;
import models.HallRequest;

public class NearestStrategy implements Selection {

    private static final int TOP_FLOOR = 9;

    @Override
    public Elevator select(List<Elevator> elevators, HallRequest hallReq) {
        // c1: an elevator already moving the SAME direction as the call, and not
        // yet past the calling floor. Best case — no detour needed.
        Elevator c1 = null;
        int minDist = Integer.MAX_VALUE;

        // c2: nearest IDLE elevator. Second-best — has to start moving, but no
        // detour either.
        Elevator c2 = null;
        int c2_dist = Integer.MAX_VALUE;

        // c3: nearest elevator moving the OPPOSITE direction. Worst case — it must
        // finish its current sweep to the far end, then reverse, before it can
        // serve this call (LOOK-algorithm detour), so its cost is estimated as a
        // round trip via the top/bottom floor instead of hard-excluding it.
        Elevator c3 = null;
        int c3_dist = Integer.MAX_VALUE;

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

            if (ele.getCurrDir() == ElevatorDir.EIDLE && Math.abs(ele.getCurrFLoor() - hallReq.getFloor()) < c2_dist) {
                c2_dist = Math.abs(ele.getCurrFLoor() - hallReq.getFloor());
                c2 = ele;
            }

            if (hallReq.getDir() == HallCalldir.UP && ele.getCurrDir() == ElevatorDir.EDOWN) {
                // must finish going down to floor 0, then come back up to the call floor
                int val = Math.abs(ele.getCurrFLoor() + hallReq.getFloor());
                if (val < c3_dist) {
                    c3_dist = val;
                    c3 = ele;
                }
            }
            if (hallReq.getDir() == HallCalldir.DOWN && ele.getCurrDir() == ElevatorDir.EUP) {
                // must finish going up to the top floor, then come back down to the call floor
                int val = Math.abs(2 * TOP_FLOOR - ele.getCurrFLoor() - hallReq.getFloor());
                if (val < c3_dist) {
                    c3_dist = val;
                    c3 = ele;
                }
            }
        }

        // c1 > c2 > c3 > absolute last resort (never return null — a broken dispatch
        // decision is still better than crashing the caller).
        return c1 != null ? c1 : (c2 != null ? c2 : c3 != null ? c3 : elevators.get(0));
    }

}
