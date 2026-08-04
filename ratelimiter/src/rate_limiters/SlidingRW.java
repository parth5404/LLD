package rate_limiters;

import java.util.Queue;

import config.Config;
import config.SlidingRWCfg;
import config.SlidingRWState;
import config.State;

public class SlidingRW implements RateLimiter {

    @Override
    public boolean evaluate(Config config, State state) {
        SlidingRWState rwstate = (SlidingRWState) state;
        SlidingRWCfg rwcfg = (SlidingRWCfg) config;
        long now = System.currentTimeMillis();
        while (!rwstate.getQueue().isEmpty() && now - rwstate.getQueue().peek() > rwcfg.getWindowLenInSec() / 1000) {
            rwstate.getQueue().poll();
        }

        if (rwstate.getQueue().size() < rwcfg.getMaxHits()) {
            rwstate.getQueue().add(now);
            return true;
        }

        return false;
    }

}
