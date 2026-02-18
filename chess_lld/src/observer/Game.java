package observer;

import java.util.ArrayList;
import java.util.List;

public class Game implements IGameBroadcaster{
    private List<Spectator> spectators=new ArrayList<>();
    @Override
    public void addSpectator(Spectator spec) {
        spectators.add(spec);
    }

    @Override
    public void removeSpectator(Spectator spec) {
        spectators.remove(spec);
    }

    @Override
    public void notifySpectator() {
        for(Spectator sp:spectators){
            sp.update();
        }
    }
}
