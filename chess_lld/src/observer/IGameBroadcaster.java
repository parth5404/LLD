package observer;

public interface IGameBroadcaster {
    public void addSpectator(Spectator spec);
    public void removeSpectator(Spectator spec);
    public void notifySpectator();
}
