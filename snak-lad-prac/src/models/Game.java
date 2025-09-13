package models;

import factory.ObstacleFactory;
import stratergy.Snakes;

import java.util.Deque;
import java.util.LinkedList;

public class Game {
    int size;
    int dicenum;
    private Deque<Players> players=new LinkedList<>();
    int snakes;
    int ladders;
    Board board;
    Dice dice;
    public Game(int size, int dicenum, int snakes, int ladders){
        this.size=size;
        this.dicenum=dicenum;
        this.snakes=snakes;
        this.ladders=ladders;

         board=new Board(size);
         dice=new Dice(dicenum);
        initializeobstacles();
    }

    public void initializeobstacles(){
        genrateSnake();
        genrateLaddar();
    }
    public void genrateSnake(){
        int cnt=0;
        while(cnt<snakes){
            int src = (int)(Math.random() * size);
            int dest=(int)(Math.random()* (src-1));
//            int src=27;
//            int dest=21;
          if(board.addObstacle(ObstacleFactory.create("SNAKES",src,dest))){
              System.out.println(src);
              System.out.println(dest);
              cnt++;
          }
        }
    }
    public void genrateLaddar(){
        int cnt=0;
        while(cnt<ladders){
            int src = (int)(Math.random() * size);
            int dest=(int)(Math.random()* (src+1));
            if(board.addObstacle(ObstacleFactory.create("LADDARS",src,dest))){
                cnt++;
            }
        }
    }

    public void addPlayers(Players p){
        players.add(p);
    }

    public boolean isWinner(Players p){
        if(p.getPosition()>=size){
            System.out.println(p.getName());
            return true;
        }
        return false;
    }
    public void startGame(){
        if(players.size()<2)return;

        while(true) {
            Players p=players.getFirst();
            players.remove();
            int offset = dice.getRandom();
            board.playerpos(p, offset);
            if(isWinner(p))break;
            players.add(p);
        }

System.out.println("Sher");
    }



}
