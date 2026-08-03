package rate_limiters;

import config.Config;
import config.State;
import config.TokenBucketConfig;
import config.TokenBucketState;

public class TokenBucket implements RateLimiter {

    @Override
    public boolean evaluate(Config config, State state) {
        TokenBucketConfig tokenBucketConfig = (TokenBucketConfig) config;
        TokenBucketState tokenBucketState = (TokenBucketState) state;

        long now = System.currentTimeMillis();
        long elapsedMillis = now - tokenBucketState.getLastRefillTime();
        double availableTokens = tokenBucketState.getAvailableTokens();

        if (elapsedMillis > 0) {
            availableTokens = Math.min(
                    tokenBucketConfig.getMaxTokens(),
                    availableTokens + (elapsedMillis * tokenBucketConfig.getRefillRate() / 1000.0));
            tokenBucketState.setLastRefillTime(now);
        }

        if (availableTokens < 1.0) {
            tokenBucketState.setAvailableTokens(availableTokens);
            return false;
        }

        tokenBucketState.setAvailableTokens(availableTokens - 1.0);
        return true;
    }

}
