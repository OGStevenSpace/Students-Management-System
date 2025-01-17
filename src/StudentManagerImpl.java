import java.util.ArrayList;
import java.sql.*;

public class StudentManagerImpl implements StudentManager {
    private static final Queries query = new Queries();
    private final DatabaseManager databaseManager;

    //Constructor with login and password in arguments used by GUI
    public  StudentManagerImpl(String login, String pass) {
        databaseManager = new DatabaseManager("jdbc:postgresql://localhost:5432/postgres",login,pass);
        //Creating a table is a default behavior, even if table already exists.
        try {
            databaseManager.execute(query.CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Add student with Student class
    @Override
    public void addStudent(Student student) {
        try {
            ResultSet resultSet = databaseManager.executeQuery(
                    query.ADD_STUDENT,
                    student.getfName(),
                    student.getlName(),
                    student.getbDay(),
                    student.getGrade());

            if (resultSet.next()) {
                int generatedId = resultSet.getInt("studentID");
                student.setStudentID(generatedId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Remove student from db using ID
    @Override
    public void removeStudent(int studentID) {
        try {
            databaseManager.executeUpdate(
                    query.DELETE_STUDENT,
                    studentID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Update student using Student class (including ID)
    //Student who was not added with addStudent yet and exists only in the app has ID = 0 by default.
    //Therefore, it won't be updated (no item with ID=0 exists).
    @Override
    public void updateStudent(Student student) {
        try {
            databaseManager.executeUpdate(
                    query.UPDATE_STUDENT,
                    student.getfName(),
                    student.getlName(),
                    student.getbDay(),
                    student.getGrade(),
                    student.getStudentID());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Creates an array with Student class objects created with the db data.
    @Override
    public ArrayList<Student> displayAllStudents() {
        ArrayList<Student> students = new ArrayList<>();

        try {
            ResultSet resultSet = databaseManager.executeQuery(query.GET_ALL_STUDENTS);
            while (resultSet.next()) {
                students.add(
                        new Student(
                                resultSet.getString("fName"),
                                resultSet.getString("lName"),
                                resultSet.getString("bDay"),
                                resultSet.getDouble("grade"),
                                resultSet.getInt("studentID")
                        )
                );
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return students;
    }

    @Override
    public double calculateAverageGrade() {
        try {
            ResultSet resultSet = databaseManager.executeQuery(query.CALCULATE_AVG);
            if (resultSet.next()) {
                return resultSet.getDouble("avg");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    return 0;
    }
}
