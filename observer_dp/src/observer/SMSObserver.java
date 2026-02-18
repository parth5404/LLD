package observer;

public class SMSObserver implements IObserver {
    private String name;
    public SMSObserver(String name){
        this.name=name;
    }
    @Override
    public void update(String title) {
        System.out.println(title+" "+name+" "+"observer.SMS");
    }
}
