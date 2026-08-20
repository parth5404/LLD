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
        long elapsed = now - tokenBucketState.getLastRefillTime();
        double tokenVal = tokenBucketState.getAvailableTokens();
        if (elapsed > 0) {
            double token_add = tokenBucketConfig.getRefillRate() * elapsed / 1000.0;
            tokenVal = Math.min(tokenVal + token_add, tokenBucketConfig.getMaxTokens());
            tokenBucketState.setLastRefillTime(now);
        }
        if (tokenVal < 1.0) {
            tokenBucketState.setAvailableTokens(tokenVal);
            return false;
        }
        tokenBucketState.setAvailableTokens(tokenVal - 1.0);
        return true;

    }

}
