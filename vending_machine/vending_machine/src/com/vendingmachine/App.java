package com.vendingmachine;

import java.util.Scanner;

import com.vendingmachine.machine.VendingMachine;
import com.vendingmachine.model.Item;
import com.vendingmachine.model.Slot;
import com.vendingmachine.state.StateEnum;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Vending Machine Initialized.");

        VendingMachine machine = new VendingMachine();

        // Create a 2D array for the shelf
        Slot[][] customShelf = new Slot[10][10];

        // Create items and assign them to slots
        Item soda = new Item("Soda", 2.50);
        Item chips = new Item("Chips", 1.50);
        Item chocolate = new Item("Chocolate", 2.00);

        customShelf[0][0] = new Slot(0, 0, 10, soda, 10);
        customShelf[0][1] = new Slot(0, 1, 10, chips, 8);
        customShelf[1][0] = new Slot(1, 0, 10, chocolate, 5);

        // Set the custom shelf in the machine
        machine.setShelf(customShelf);
        machine.setCurrState(StateEnum.SELECTION);
        System.out.println("Shelf set successfully.");
        System.out.println("Slot [0][0]: " + machine.getShelf()[0][0].getItem().getName() + " (Qty: "
                + machine.getShelf()[0][0].getCurrent_qty() + ")");
        System.out.println("Slot [0][1]: " + machine.getShelf()[0][1].getItem().getName() + " (Qty: "
                + machine.getShelf()[0][1].getCurrent_qty() + ")");
        // Initialize Machine Wallet for change
        machine.machineWallet.add(com.vendingmachine.model.Denomination.ONE_DOLLAR, 10);
        machine.machineWallet.add(com.vendingmachine.model.Denomination.FIVE_DOLLARS, 5);
        machine.machineWallet.add(com.vendingmachine.model.Denomination.TEN_DOLLARS, 2);

        System.out.println("Slot [1][0]: " + machine.getShelf()[1][0].getItem().getName() + " (Qty: "
                + machine.getShelf()[1][0].getCurrent_qty() + ")");
        System.out.println("Current Machine State: " + machine.getCurrState());
        System.out.println("Enter commands (e.g., 'select A0 2', 'checkout', 'pay', 'dispense', 'cancel', 'exit'):");
        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine())
                break;
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                continue;

            String[] parts = input.split("\\s+");
            String command = parts[0].toLowerCase();

            if (command.equals("exit")) {
                break;
            } else if (command.equals("select")) {
                if (parts.length < 3) {
                    System.out.println("Usage: select <slot> <qty> (e.g., select A0 2)");
                    continue;
                }
                String slot = parts[1];
                int qty = Integer.parseInt(parts[2]);
                int row = machine.util.getRowFromStr(slot);
                int col = machine.util.getColFromStr(slot);
                machine.getState(machine.getCurrState()).select(machine, row, col, qty);
            } else if (command.equals("checkout")) {
                machine.getState(machine.getCurrState()).checkout(machine);
            } else if (command.equals("insert")) {
                if (parts.length < 2) {
                    System.out.println("Usage: insert <amount> (e.g., insert 5)");
                    continue;
                }
                double val = Double.parseDouble(parts[1]);
                com.vendingmachine.model.Denomination d = com.vendingmachine.model.Denomination.fromValue(val);
                if (d == null) {
                    System.out.println("Invalid denomination. Accepted values: 1, 2, 5, 10, 20, 50.");
                } else {
                    machine.getState(machine.getCurrState()).insertCash(machine, d);
                }
            } else if (command.equals("pay")) {
                machine.getState(machine.getCurrState()).completePayment(machine);
            } else if (command.equals("dispense")) {
                machine.getState(machine.getCurrState()).dispense(machine);
            } else if (command.equals("cancel")) {
                machine.getState(machine.getCurrState()).cancelDispense(machine);
            } else {
                System.out.println("Unknown command.");
            }
            System.out.println("Current Machine State: " + machine.getCurrState());
        }
    }
}
