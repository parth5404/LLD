package repo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserLockManager {
    private final Map<String, Object> locks = new ConcurrentHashMap<>();

    public Object getLock(String userId) {
        return locks.computeIfAbsent(userId, id -> new Object());
    }
}
