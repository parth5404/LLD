public class Student {
    String Name;
    int Rollno;

    public  Student(String Name, int rollno){
        this.Name=Name;
        this.Rollno=rollno;
    }
    public void printDetails(){
        System.out.println(Name+" "+Rollno);
    }
}
