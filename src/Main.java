import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        //StudentManagerImpl test = new StudentManagerImpl("postgres", "test1234");
        //Student testStudent = new Student("Krzysztof", "Oleksiak", "1998-07-09", 4);
        StudentManagementApp window = new StudentManagementApp("postgres", "test1234");



        //System.out.println(test.calculateAverageGrade());
        //test.addStudent(testStudent);
        //System.out.println(test.calculateAverageGrade());
        //System.out.println(test.displayAllStudents().toString());
        //test.removeStudent(1);
        //System.out.println(test.displayAllStudents().toString());
    }
}