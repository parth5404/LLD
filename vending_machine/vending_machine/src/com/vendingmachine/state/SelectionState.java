package com.vendingmachine.state;

import com.vendingmachine.machine.VendingMachine;

public class SelectionState implements VendingMachineState {

    @Override
    public boolean select() {
        System.out.println("Item selected successfully.");
        return true;
    }

    @Override
    public boolean pay(VendingMachine vendingMachine) {
        System.out.println("Cannot pay in Selection state. Please finalize selection first.");
        return false;
    }

    @Override
    public boolean dispense(VendingMachine vendingMachine) {
        System.out.println("Cannot dispense in Selection state.");
        return false;
    }

    @Override
    public void cancelDispense() {
        System.out.println("No dispense in progress to cancel.");
    }
}
