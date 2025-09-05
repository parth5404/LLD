//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    public static void main(String[] args) {
        Student s1=new Student("sher",54);
        StudentSaver save=new StudentSaver(s1);
        Saver ing=new DbSave();
        ing.save(s1);
    }
}