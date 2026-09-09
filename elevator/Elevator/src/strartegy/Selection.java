package strartegy;

import java.util.List;

import models.Elevator;
import models.HallRequest;

public interface Selection {
    Elevator select(List<Elevator> elevators, HallRequest hallReq);
}
