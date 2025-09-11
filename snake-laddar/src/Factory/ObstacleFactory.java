package Factory;

import Stratergy.*;
import enums.obstacleType;

public class ObstacleFactory {
    public  static Obstacle create(obstacleType type,int src,int dest){
        switch(type){
            case SNAKES -> {
                    Snake snake =new Snake(src,dest);
            }
            case LADDERS -> {
                Laddar laddar =new Laddar(src,dest);
            }
        }
        return null;
    }
}
