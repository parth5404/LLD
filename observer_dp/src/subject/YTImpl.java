package subject;

import observer.IObserver;

import java.util.ArrayList;
import java.util.List;

public class YTImpl implements Subject {
    private String title;
    private List<IObserver>observers=new ArrayList<>();
    @Override
    public void addObserver(IObserver obv) {
        observers.add(obv);
    }
    @Override
    public void notifyObserver() {
        for(IObserver obv:observers){
            obv.update(title);
        }
    }
    @Override
    public void removeObserver(IObserver obv) {
        for(IObserver ob:observers){
            if(ob.equals(obv)){
                observers.remove(obv);
            }
        }
    }
    public void upload(String title){
        this.title=title;
        notifyObserver();
    }
}
