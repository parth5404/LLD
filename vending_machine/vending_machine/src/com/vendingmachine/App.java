package com.vendingmachine;

import com.vendingmachine.machine.VendingMachine;
import com.vendingmachine.model.Item;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Vending Machine Initialized.");

        VendingMachine machine = new VendingMachine();
        Item soda = new Item("Soda", 2.50);

        System.out.println("Loaded Item: " + soda.getName() + " - $" + soda.getPrice());
        System.out.println("Current Machine State: " + machine.getCurrState());
    }
}
