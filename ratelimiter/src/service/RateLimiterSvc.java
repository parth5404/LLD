package service;

import config.Config;
import config.State;
import repo.ConfigRepo;
import repo.StateRepo;
import rate_limiters.RateLimiter;
import rate_limiters.TokenBucket;
import rate_limiters.SlidingRW;

public class RateLimiterSvc {
    private final StateRepo stateRepo;
    private final ConfigRepo configRepo;
    
    private final TokenBucket tokenBucket = new TokenBucket();
    private final SlidingRW slidingRW = new SlidingRW();

    public RateLimiterSvc(StateRepo stateRepo, ConfigRepo configRepo) {
        this.stateRepo = stateRepo;
        this.configRepo = configRepo;
    }

    public boolean isAllowed(String userId) {
        State state = stateRepo.getState(userId);
        Config config = configRepo.getConfig(userId);
        
        switch (config.getType()) {
            case TOKEN_BUCKET:
                return tokenBucket.evaluate(config, state);
            case SLIDING_WINDOW:
                return slidingRW.evaluate(config, state);
            default:
                throw new IllegalArgumentException("Unknown rate limiter type");
        }
    }
}
