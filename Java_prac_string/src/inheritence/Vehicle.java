package inheritence;

public class Vehicle {
    int wheels;
    final int gears = 4;

    final void start() {
        //System.out.println(this);
        System.out.println("start " + wheels);
    }

    public static void main(String[] args) {
        Vehicle vehicle = new Vehicle();
    }
}
