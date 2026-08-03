package rate_limiters;

import config.Config;
import config.State;

public interface RateLimiter {
    boolean evaluate(Config config, State state);
}
