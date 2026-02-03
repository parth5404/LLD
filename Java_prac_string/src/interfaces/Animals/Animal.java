package interfaces.Animals;

public interface Animal {
    int MAXIMUM = 100;

    public abstract void eat();

    void sleep();

    public static void speak() {
        System.out.println("Animal Interface speaking...");
    }
}
