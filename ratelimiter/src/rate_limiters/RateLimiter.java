package rate_limiters;

import config.Config;
import config.State;

public interface RateLimiter<C extends Config, S extends State> {
    boolean evaluate(C config, S state);
}
