package model;

import Enums.Vtype;

public abstract class Vehicle {
   private int id;
   private int number;
   private Vtype type;

   Vehicle ( int number, Vtype type){
       this.number=number;
       this.type=type;
   }
}
