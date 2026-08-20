package rate_limiters;

import config.Config;
import config.RateLimiterType;
import config.SlidingRWCfg;
import config.SlidingRWState;
import config.State;

public class SlidingRW implements RateLimiter<SlidingRWCfg, SlidingRWState> {

    @Override
    public RateLimiterType getType() {
        return RateLimiterType.SLIDING_WINDOW;
    }

    @Override
    public Class<SlidingRWCfg> getConfigClass() {
        return SlidingRWCfg.class;
    }

    @Override
    public Class<SlidingRWState> getStateClass() {
        return SlidingRWState.class;
    }

    @Override
    public boolean evaluate(SlidingRWCfg rwcfg, SlidingRWState rwstate) {
        long now = System.currentTimeMillis();
        while (!rwstate.getQueue().isEmpty() && now - rwstate.getQueue().peek() > rwcfg.getWindowLenInMillis()) {
            rwstate.getQueue().poll();
        }

        if (rwstate.getQueue().size() < rwcfg.getMaxHits()) {
            rwstate.getQueue().add(now);
            return true;
        }

        return false;
    }

}
