package clone;

public class TestStud {
 public static void main(String[] args) throws CloneNotSupportedException {
     Student s1 = new Student();
     Address a1 = new Address();
     Address a2 = new Address();
     a1.value="Indore";
     a2.value="PUne";
     s1.rollno=25;
     s1.address=a1;
     Student s2= (Student)s1.clone();
     s2.rollno=36;
     s2.address=a2;
     System.out.println(s1.rollno+" "+s2.rollno);
 }
}
