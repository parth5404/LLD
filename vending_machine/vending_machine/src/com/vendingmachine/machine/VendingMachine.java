package com.vendingmachine.machine;

import java.util.HashMap;
import java.util.Map;

import com.vendingmachine.state.DispensingState;
import com.vendingmachine.state.PaymentState;
import com.vendingmachine.state.SelectionState;
import com.vendingmachine.state.StateEnum;
import com.vendingmachine.state.VendingMachineState;

public class VendingMachine {
    private StateEnum currState;
    private Map<String, VendingMachineState> stateMap = new HashMap<>();

    public VendingMachine() {
        stateMap.put("SELECTION", new SelectionState());
        stateMap.put("PAYMENT", new PaymentState());
        stateMap.put("DISPENSE", new DispensingState());
        this.currState = StateEnum.SELECTION;
    }

    public StateEnum getCurrState() {
        return currState;
    }

    public void setCurrState(StateEnum state) {
        this.currState = state;
    }

    public VendingMachineState getState(String key) {
        return stateMap.get(key);
    }
}
