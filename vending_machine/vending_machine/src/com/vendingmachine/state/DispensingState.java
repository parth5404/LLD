package com.vendingmachine.state;

import com.vendingmachine.machine.VendingMachine;

public class DispensingState implements VendingMachineState {

    @Override
    public boolean select(VendingMachine machine, int row, int col, int qty) {
        System.out.println("Cannot select item while dispensing.");
        return false;
    }

    @Override
    public boolean checkout(VendingMachine machine) {
        System.out.println("Cannot checkout while dispensing.");
        return false;
    }

    @Override
    public boolean completePayment(VendingMachine machine) {
        System.out.println("Payment already completed.");
        return false;
    }

    @Override
    public void insertCash(VendingMachine machine, com.vendingmachine.model.Denomination denomination) {
        System.out.println("Cannot insert cash. Dispensing in progress.");
    }

    @Override
    public boolean dispense(VendingMachine vendingMachine) {
        System.out.println("Item dispensed successfully!");
        return true;
    }

    @Override
    public void cancelDispense(VendingMachine machine) {
        System.out.println("Cannot cancel while item is dispensing.");
    }
}
