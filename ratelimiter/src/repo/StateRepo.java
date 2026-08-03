package repo;

import config.State;

public interface StateRepo {
    State getState(String userID);

    void saveState(String userID, State state);
}
