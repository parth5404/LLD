import models.Game;
import models.Players;

public class Main {
    public static void main(String[] args) {
        Game game=new Game(100,2,5,5);
        Players p1=new Players("Parth");
        Players p2=new Players("Shyam");
        game.addPlayers(p1);
        game.addPlayers(p2);
        game.startGame();
    }
}