package service;

import java.util.EnumMap;
import java.util.Map;

import config.Config;
import config.RateLimiterType;
import config.State;
import repo.ConfigRepo;
import repo.StateRepo;
import repo.UserLockManager;
import rate_limiters.RateLimiter;
import rate_limiters.SlidingRW;
import rate_limiters.TokenBucket;

public class RateLimiterSvc {
    private final StateRepo stateRepo;
    private final ConfigRepo configRepo;
    private final UserLockManager lockManager;
    private final Map<RateLimiterType, RateLimiter<? extends Config, ? extends State>> limiters =
            new EnumMap<>(RateLimiterType.class);

    public RateLimiterSvc(StateRepo stateRepo, ConfigRepo configRepo, UserLockManager lockManager) {
        this.stateRepo = stateRepo;
        this.configRepo = configRepo;
        this.lockManager = lockManager;
        register(new TokenBucket());
        register(new SlidingRW());
    }

    public boolean isAllowed(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be null or blank");
        }

        Object lock = lockManager.getLock(userId);
        synchronized (lock) {
            State state = stateRepo.getState(userId);
            Config config = configRepo.getConfig(userId);

            if (state == null || config == null) {
                return false;
            }

            RateLimiter<? extends Config, ? extends State> limiter = limiters.get(config.getType());
            if (limiter == null) {
                throw new IllegalArgumentException("Unsupported rate limiter type: " + config.getType());
            }

            return evaluate(limiter, config, state);
        }
    }

    private void register(RateLimiter<? extends Config, ? extends State> limiter) {
        limiters.put(limiter.getType(), limiter);
    }

    private <C extends Config, S extends State> boolean evaluate(
            RateLimiter<C, S> limiter,
            Config config,
            State state) {
        if (!limiter.getConfigClass().isInstance(config)) {
            throw new IllegalStateException("Config type does not match limiter: " + config.getClass().getSimpleName());
        }
        if (!limiter.getStateClass().isInstance(state)) {
            throw new IllegalStateException("State type does not match limiter: " + state.getClass().getSimpleName());
        }

        return limiter.evaluate(limiter.getConfigClass().cast(config), limiter.getStateClass().cast(state));
    }
}
