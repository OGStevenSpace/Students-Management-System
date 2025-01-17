import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private final String url;
    private final String login;
    private final String pass;

    //Constructor
    public DatabaseManager(String url, String login, String pass) {
        this.url = url;
        this.login = login;
        this.pass = pass;
    }

    //I'm not using the pool managers, so each connection is created anew when needed and closed within try() statement.
    private Connection getConnection() {
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

    // Utility method to execute queries
    public void execute(String query) throws SQLException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }

    // Utility method to execute update queries (INSERT, UPDATE, DELETE)
    public void executeUpdate(String query, Object... params) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setParameters(preparedStatement, params);
            preparedStatement.executeUpdate();
        }
    }

    // Utility method to execute SELECT queries
    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection connection = getConnection(); // Ensure the caller closes this
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        setParameters(preparedStatement, params);
        return preparedStatement.executeQuery();
    }


    // Helper method to set parameters in PreparedStatement
    private void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

}

//Query storage. It makes it easier to modify or to add new queries.
class Queries {
    public String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS students (" +
            "studentID SERIAL PRIMARY KEY, " +
            "fName VARCHAR(30), " +
            "lName VARCHAR(100), " +
            "bDay DATE, " +
            "grade DOUBLE PRECISION);";
    public String ADD_STUDENT = "INSERT INTO students(fName, lName, bDay, grade) VALUES " +
            "(?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)" +
            "RETURNING studentID";
    public String DELETE_STUDENT = "DELETE FROM students WHERE studentID = ?";
    public String UPDATE_STUDENT = "UPDATE students SET " +
            "fName = ?, " +
            "lName = ?, " +
            "bDay = TO_DATE(?, 'YYYY-MM-DD'), " +
            "grade = ? " +
            "WHERE studentID = ?";
    public String GET_ALL_STUDENTS = "SELECT * FROM students";
    public String CALCULATE_AVG = "SELECT AVG(grade) FROM students";
}
