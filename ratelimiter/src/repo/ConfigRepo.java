package repo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import config.Config;

public class ConfigRepo {
    public final Map<String, config.Config> configStore = new ConcurrentHashMap<>();

    public config.Config getConfig(String userID) {
        return configStore.get(userID);
    }

    public void setConfig(String userID, Config config) {
        configStore.put(userID, config);
    }

}   
