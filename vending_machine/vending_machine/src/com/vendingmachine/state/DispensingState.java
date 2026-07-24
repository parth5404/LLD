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
    public boolean dispense(VendingMachine machine) {
        for (java.util.Map.Entry<com.vendingmachine.model.Item, Integer> entry : machine.cart.entrySet()) {
            com.vendingmachine.model.Item item = entry.getKey();
            int qtyToDispense = entry.getValue();

            // Deduct from shelf
            for (int r = 0; r < machine.getShelf().length; r++) {
                for (int c = 0; c < machine.getShelf()[r].length; c++) {
                    com.vendingmachine.model.Slot slot = machine.getShelf()[r][c];
                    if (slot != null && slot.getItem() != null && slot.getItem().equals(item)) {
                        if (qtyToDispense > 0) {
                            int deduct = Math.min(qtyToDispense, slot.getCurrent_qty());
                            slot.setCurrent_qty(slot.getCurrent_qty() - deduct);
                            qtyToDispense -= deduct;
                        }
                    }
                }
            }
            System.out.println("Dispensed: " + entry.getValue() + " x " + item.getName());
        }

        // Clear cart and return to SELECTION state
        machine.cart.clear();
        machine.setCurrState(StateEnum.SELECTION);
        System.out.println("All items dispensed successfully!");
        return true;
    }

    @Override
    public void cancelDispense(VendingMachine machine) {
        System.out.println("Cannot cancel while item is dispensing.");
    }
}
