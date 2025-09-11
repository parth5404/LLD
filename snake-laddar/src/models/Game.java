package models;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

import Stratergy.Obstacle;
import enums.*;
import Factory.*;

public class Game {
    private Deque<Player>players=new LinkedList<>();
    private int size;
    private int snakes;
    private int ladders;
    private int ndice;
    private Board board;
    private Dice dice;

    Game(int snakes,int ladders,int size,int ndice){
            this.snakes=snakes;
            this.ladders=ladders;

            this.board=new Board(size);
            this.dice=new Dice(ndice);
            obsgen();
    }

    public void obsgen(){
        makeObs(snakes,obstacleType.SNAKES);
        makeObs(ladders,obstacleType.LADDERS);
    }
    public void makeObs(int count,obstacleType type){
        Random random=new Random();
        int size= board.getSize();
        while(count>0){
            int up= random.nextInt(size-1);
            int down=random.nextInt(up-1);
            Obstacle obstacle= ObstacleFactory.create(type,up,down);
           boolean obs= board.addObstacle(obstacle);
           if(obs)count--;
        }
    }
    public void addplayer(Player p){
        players.add(p);
    }
    public void startGame(){
        if(players.size()>1){
            if(players.peek().getPosition()== board.getSize()){
                System.out.println(players.peek().getName());
            }
           int num= dice.roll();
           board.getNewPosition(players.getFirst(),num);
            Player p=players.pop();
            players.add(p);
        }
    }

}
