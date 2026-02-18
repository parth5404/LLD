import enums.Color;
import models.Player;
import observer.HumanSpectator;

public class Main {
    public static void main (String[] args){
        HumanSpectator spec= new HumanSpectator("Sher");
        Player one=new Player("INDIA", Color.BLACK);
    }
}