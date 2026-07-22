public class dispensing implements state {

    @Override
    public boolean select() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'select'");
    }

    @Override
    public boolean pay() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pay'");
    }

    @Override
    public boolean dispense(machine vendingMachine) {
        System.out.println("Item dispensed successfully!");
        return true;
    }

    @Override
    public void cancel_dispense() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancel_dispense'");
    }

}
