package clone;

public class Address implements Cloneable {
    String value;
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return  super.clone();
    }
}
