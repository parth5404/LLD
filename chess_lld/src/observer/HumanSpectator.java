package observer;

public class HumanSpectator implements Spectator{
    private String name;
    public HumanSpectator(String name){
        this.name=name;
    }
    @Override
    public void update() {
        System.out.println("Human Spectator is Notified");
    }
}
