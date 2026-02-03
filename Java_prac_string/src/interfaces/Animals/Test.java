package interfaces.Animals;

public class Test {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.sleep();
        System.out.println(Dog.MAXIMUM);
        System.out.println(Animal.MAXIMUM);
        Animal.speak();
    }
}
