public class Main {
    public static void main(String[] args) {
        Runnable r1 = () -> {
            DBConnection.getDBconnection("Thread1");
        };

        Runnable r2 = () -> {
            DBConnection.getDBconnection("Thread2");
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();
    }
}