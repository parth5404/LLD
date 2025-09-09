package model;

import Enums.GateType;
import Enums.Vtype;

public class ExitGate extends Gate{
    public ExitGate(){
        super(GateType.EXIT);
    }

    @Override
    public GateType getType() {
        return GateType.EXIT;
    }
}
