package subject;

import observer.IObserver;

public interface Subject {

    void addObserver(IObserver obv);

    public void notifyObserver();

    void removeObserver(IObserver obv);
}
