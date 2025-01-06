import java.util.ArrayList;

public interface StudentManager {
    void addStudent(Student student);
    void removeStudent(int studentID);
    void updateStudent(Student studentID);
    ArrayList<Student> displayAllStudents();
    double calculateAverageGrade();
}
