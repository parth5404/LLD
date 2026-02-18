import observer.EmailObserver;
import observer.IObserver;
import observer.SMSObserver;
import subject.Subject;
import subject.YTImpl;

public  class Main {
    public static void main(String[] args) {
        YTImpl channel =new YTImpl();
        IObserver sher=new SMSObserver("SHER");
        IObserver lion=new EmailObserver("LION");
        channel.addObserver(sher);
        channel.addObserver(lion);
        channel.upload("sher & sherni");
    }
}
