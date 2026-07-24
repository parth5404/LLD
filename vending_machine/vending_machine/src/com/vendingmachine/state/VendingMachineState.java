package com.vendingmachine.state;

import com.vendingmachine.machine.VendingMachine;

public interface VendingMachineState {
    boolean select(VendingMachine machine, int row, int col, int qty);

    boolean checkout(VendingMachine machine);

    boolean completePayment(VendingMachine machine);

    void insertCash(VendingMachine machine, com.vendingmachine.model.Denomination denomination);

    boolean dispense(VendingMachine vendingMachine);

    void cancelDispense(VendingMachine machine);
}
