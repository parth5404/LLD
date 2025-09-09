package model;

import Enums.GateType;

public class EntryGate extends Gate{
    public EntryGate(){
        super(GateType.ENTRY);
    }

    @Override
    public GateType getType() {
        return GateType.ENTRY;
    }
    public void parkVehicle(){

    }
}
