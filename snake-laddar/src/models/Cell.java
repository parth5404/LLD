package models;

import Stratergy.Obstacle;

public class Cell {
  public int pos;
  public Obstacle obs;

  Cell(int pos){
   this.pos=pos;
  }

  public boolean haveObstacle(){
        return obs!=null;
  }
  public void setObs(Obstacle obs){
      this.obs=obs;
  }
  public int getFinalpos(){
      return haveObstacle()? obs.movePlayer() : pos;
  }

}
