package rate_limiters;

import config.Config;
import config.RateLimiterType;
import config.State;
import config.TokenBucketConfig;
import config.TokenBucketState;

public class TokenBucket implements RateLimiter<TokenBucketConfig, TokenBucketState> {

    @Override
    public RateLimiterType getType() {
        return RateLimiterType.TOKEN_BUCKET;
    }

    @Override
    public Class<TokenBucketConfig> getConfigClass() {
        return TokenBucketConfig.class;
    }

    @Override
    public Class<TokenBucketState> getStateClass() {
        return TokenBucketState.class;
    }

    @Override
    public boolean evaluate(TokenBucketConfig tokenBucketConfig, TokenBucketState tokenBucketState) {
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
