package enums;

// State of an Elevator itself: which way it's currently moving, or IDLE if it has
// no pending stops. Kept separate from HallCalldir (below) because a hall call can
// never be IDLE — only an elevator can.
public enum ElevatorDir {
    EUP,
    EDOWN,
    EIDLE
}
