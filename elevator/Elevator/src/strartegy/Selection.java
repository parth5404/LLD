package strartegy;

import java.util.List;

import models.Elevator;
import models.HallRequest;

// Strategy pattern (satisfies the Extensibility NFR): a new dispatch algorithm can
// be dropped in by implementing this interface, without touching ElevatorController.
// Implementations should be read-only decisions — pick a winner, don't mutate any
// elevator's state here; let the caller (Controller) apply the effects of dispatch.
public interface Selection {
    Elevator select(List<Elevator> elevators, HallRequest hallReq);
}
