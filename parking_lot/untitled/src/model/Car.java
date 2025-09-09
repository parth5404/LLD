package model;

import Enums.Vtype;

public class Car extends Vehicle {
    public Car(int number, Vtype type) {
        super( number, Vtype.CAR); // super() call to parent constructor
    }
}
