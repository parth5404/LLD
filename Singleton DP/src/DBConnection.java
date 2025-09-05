public class DBConnection {
    // volatile is important
    private static volatile DBConnection connection;

    private DBConnection(String name) {
        System.out.println("DB connected " + name);
    }

    public static DBConnection getDBconnection(String name) {
        // 1st check (no lock) - fast path for already-initialized case
        if (connection == null) {
            synchronized (DBConnection.class) {
                // 2nd check (with lock) - ensures only one thread constructs
                if (connection == null) {
                    connection = new DBConnection(name);
                }
            }
        }
        return connection;
    }
}
