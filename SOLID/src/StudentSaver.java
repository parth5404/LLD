interface Saver{
    void save(Student s);
}

class DbSave implements Saver{
    public void save(Student s1){
        System.out.println(s1.Name+" "+"DB SAVE");
    }
}
class FileSave implements Saver{
    public void save(Student s1){
        System.out.println(s1.Name+" "+"FILE SAVE");
    }
}


public class StudentSaver {
    public StudentSaver(Student s1) {
        System.out.println("student saved "+s1.Name);
    }
}
