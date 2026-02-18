package observer;

public class EmailObserver implements IObserver{
    private String name;
    public EmailObserver(String name){
        this.name=name;
    }
    @Override
    public void update(String title) {
        System.out.println(title+" "+name+" "+"observer.Email");
    }
}
