package clone;

public class Student implements Cloneable {
    int rollno;
    Address address;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Student student = (Student) super.clone();      // shallow copy of Student
        student.address = (Address) address.clone();    // deep copy of Address
        return student;
    }
}
