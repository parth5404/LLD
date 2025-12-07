package inheritence;

public class Car extends Vehicle {
    public static void main(String[] args) {
        Car car = new Car();
        car.wheels = 4;
        car.start();
    }

//    public void start() {
//        super.start();
//        // System.out.println(this + " " + super);
//        System.out.println("Start Child " + wheels);
//    }
}
