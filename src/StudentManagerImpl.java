import java.util.ArrayList;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class StudentManagerImpl implements StudentManager {
    //Using PostgreSQL for database
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    //Initializing login and password for further reference.
    private final String login;
    private final String pass;

    //Constructor with login and password in arguments used by GUI
    public  StudentManagerImpl(String login, String pass) {
        this.login = login;
        this.pass = pass;

        //Creating a table is a default behavior, even if table already exists.
        createTable();
    }

    //Constructor without parameters asks for login and password. Used for debugging.
    public StudentManagerImpl() {
        Scanner scanner = new Scanner(System.in);

        //Scanning user keyboard input for login, then for password.
        System.out.println("Enter login");
        String login = scanner.nextLine();
        System.out.println("Enter password");
        String pass = scanner.nextLine();

        this.login = login;
        this.pass = pass;

        //Creating a table is a default behavior, even if table already exists.
        createTable();
    }

    //Connection is a method that gets a db connection using the class login & password.
    //It should be used each time the user wants to read from/write to the db.
    //It returns the connection for further use.
    //It is private so the method can only be called from within the class.
    private Connection connection() {
        Properties props = new Properties();

        //setting params for login and password. To connect with db we need to use 'user' and 'password' params
        props.setProperty("user", this.login);
        props.setProperty("password", this.pass);

        //Trying to connect with the db with the login and password.
        try {
            return DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //This method creates a db table.
    //Using the postgres query it only creates one if it does not exist already.
    //It is private so the method can only be called from within the class. User should not be able to create a table on their own.
    private void createTable(){
        String query =
                "CREATE TABLE IF NOT EXISTS students (" +
                        "studentID SERIAL PRIMARY KEY, " +
                        "fName VARCHAR(30), " +
                        "lName VARCHAR(100), " +
                        "bDay DATE, " +
                        "grade DOUBLE PRECISION);";

        try (Statement statement = connection().createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query: " + query, e);
        }
    }

    //Add student with Student class
    @Override
    public void addStudent(Student student) {
        String query = "INSERT INTO students(fName, lName, bDay, grade) VALUES " +
                "(?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";

        try (PreparedStatement preparedStatement = connection().prepareStatement(query)) {
            preparedStatement.setString(1, student.getfName());
            preparedStatement.setString(2, student.getlName());
            preparedStatement.setString(3, student.getbDay());
            preparedStatement.setDouble(4, student.getGrade());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query: " + query, e);
        }
    }

    //Remove student from db using ID
    @Override
    public void removeStudent(int studentID) {
        String query = "DELETE FROM students WHERE studentID = ?";

        try (PreparedStatement preparedStatement = connection().prepareStatement(query)) {
            preparedStatement.setInt(1, studentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query: " + query, e);
        }
    }

    //Update student using Student class (including ID)
    //Student who was not added with addStudent yet and exists only in the app has ID = 0 by default.
    //Therefore, it won't be updated (no item with ID=0 exists).
    @Override
    public void updateStudent(Student student) {
        String query = "UPDATE students SET fName = ?, lName = ?, bDay = ?, grade = ? WHERE studentID = ?";
        try (PreparedStatement preparedStatement = connection().prepareStatement(query)) {
            preparedStatement.setString(1, student.getfName());
            preparedStatement.setString(2, student.getlName());
            preparedStatement.setString(3, student.getbDay());
            preparedStatement.setDouble(4, student.getGrade());
            preparedStatement.setInt(5, student.getStudentID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query: " + query, e);
        }
    }

    //Creates an array with Student class objects created with the db data.
    @Override
    public ArrayList<Student> displayAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";

        try (Statement statement = connection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
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

        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query: " + query, e);
        }
        return students;
    }

    @Override
    public double calculateAverageGrade() {
        String query = "SELECT AVG(grade) FROM students";
        try (Statement statement = connection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getDouble("avg");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query: " + query, e);
        }
        return 0;
    }
}
