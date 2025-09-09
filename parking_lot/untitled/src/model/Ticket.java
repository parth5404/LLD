package model;
import Enums.PaymentStatus;
import Enums.Vtype;
import java.time.LocalDateTime;

public class Ticket {
    private static int nextId=0;
    private int ticketId;
    private LocalDateTime entryTime;
    private Vtype vehicle;
    private int floorid;
    private int spotid;
    private PaymentStatus status = PaymentStatus.PENDING;

    public Ticket( LocalDateTime entryTime, Vtype type, int floorid, int spootid) {
        this.ticketId = ++nextId;
        this.entryTime = entryTime;
        this.vehicle = type;
        this.floorid=floorid;
        this.spotid=spotid;
    }

    public int getTicketId() { return ticketId; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public Vtype getVehicle() { return vehicle; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public int getSpotid(){return spotid;}
    public int getFloorid(){return floorid;}
}
