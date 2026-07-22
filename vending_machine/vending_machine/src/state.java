public interface state {
    boolean select();

    boolean pay();

    boolean dispense(machine vendingMachine);

    void cancel_dispense();

}
