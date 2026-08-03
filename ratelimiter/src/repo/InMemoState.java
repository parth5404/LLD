package repo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import config.State;

public class InMemoState implements StateRepo {
    public final Map<String, State> stateStore = new ConcurrentHashMap<>();

    @Override
    public State getState(String userID) {
        return stateStore.get(userID);
    }

    @Override
    public void saveState(String userID, State state) {
        stateStore.put(userID, state);
    }

}
