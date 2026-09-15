package enums;

// Direction a passenger pressed on a hallway panel (the up/down arrows outside the
// elevator). Only two values on purpose — a hall call is never "idle", so it can't
// share an enum with ElevatorDir without making an invalid state representable.
public enum HallCalldir {
    UP,
    DOWN,
}
