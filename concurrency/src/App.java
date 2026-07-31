public class App {
    public static void main(String[] args) throws InterruptedException {
        // 1. Check which thread is running our main method
        String mainThreadName = Thread.currentThread().getName();
        System.out.println("1. [" + mainThreadName + "] Main program started!");

        // 2. Create a new custom Thread using a Runnable (lambda)
        Thread workerThread = new Thread(() -> {
            String workerName = Thread.currentThread().getName();
            System.out.println("2. [" + workerName + "] Worker thread started doing tasks in PARALLEL!");
            
            try {
                // Simulate heavy work taking 2 seconds
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.println("4. [" + workerName + "] Worker thread finished its task after 2 seconds!");
        }, "My-First-Worker-Thread");

        // 3. Start the worker thread! 
        // NOTE: .start() asks OS to schedule this thread on a CPU core.
        // DO NOT call .run() directly, otherwise it will run sequentially on main thread!
        workerThread.start();

        // 4. Main thread continues its own work immediately without blocking!
        System.out.println("3. [" + mainThreadName + "] Main thread is doing other work while Worker runs asynchronously...");
        
        // Simulate main thread working for 1 second
        Thread.sleep(1000);
        System.out.println("3.5 [" + mainThreadName + "] Main thread finished its work after 1 second!");
    }
}

