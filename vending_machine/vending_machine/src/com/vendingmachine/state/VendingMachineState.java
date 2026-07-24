package com.vendingmachine.state;

import com.vendingmachine.machine.VendingMachine;

public interface VendingMachineState {
    boolean select();

    boolean pay(VendingMachine vendingMachine);

    boolean dispense(VendingMachine vendingMachine);

    void cancelDispense();
}
