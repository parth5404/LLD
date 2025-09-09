package model;
import Enums.GateType;
public abstract class Gate {
    private int id;
    private GateType type;
    private static int nextid=0;

    Gate(GateType type){
        this.id=++nextid;
        this.type=type;
    }

    public GateType getType() {
        return type;
    }

}
