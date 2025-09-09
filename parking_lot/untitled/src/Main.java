import Enums.PriceType;
import Enums.Vtype;
import Factory.VehicleFactory;
import model.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ParkingLot lot= new ParkingLot();
        EntryGate gate=new EntryGate();
        ExitGate gate1=new ExitGate();
        ParkingFloor P1= new ParkingFloor();
        lot.addFloor(P1);
        ParkingSpot s1= new ParkingSpot(Vtype.BIKE);
        P1.addSpot(s1);
       Vehicle v= VehicleFactory.create(Vtype.BIKE,1);


    }
}