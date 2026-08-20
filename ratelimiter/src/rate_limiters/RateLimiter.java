package rate_limiters;

import config.Config;
import config.RateLimiterType;
import config.State;

public interface RateLimiter<C extends Config, S extends State> {
    RateLimiterType getType();

    Class<C> getConfigClass();

    Class<S> getStateClass();

    boolean evaluate(C config, S state);
}
