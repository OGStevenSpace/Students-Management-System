import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StudentManagementApp {
    private StudentManagerImpl manager;

    public StudentManagementApp(String login, String password) {
        manager = new StudentManagerImpl(login, password);
        JFrame frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new GridLayout(6, 2));

        JLabel lblFName = new JLabel("First Name:");
        JTextField txtFName = new JTextField();
        JLabel lblLName = new JLabel("Last Name:");
        JTextField txtLName = new JTextField();
        JLabel lblBDay = new JLabel("Birthday (YYYY-MM-DD):");
        JTextField txtBDay = new JTextField();
        JLabel lblGrade = new JLabel("Grade:");
        JTextField txtGrade = new JTextField();

        JButton btnAdd = new JButton("Add Student");
        JButton btnDisplay = new JButton("Display All Students");
        JButton btnAverage = new JButton("Calculate Average");

        JTextArea output = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(output);

        panel.add(lblFName);
        panel.add(txtFName);
        panel.add(lblLName);
        panel.add(txtLName);
        panel.add(lblBDay);
        panel.add(txtBDay);
        panel.add(lblGrade);
        panel.add(txtGrade);
        panel.add(btnAdd);
        panel.add(btnDisplay);
        panel.add(btnAverage);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            try {
                String fName = txtFName.getText();
                String lName = txtLName.getText();
                String bDay = txtBDay.getText();
                double grade = Double.parseDouble(txtGrade.getText());
                manager.addStudent(new Student(fName, lName, bDay, grade, 0));
                output.append("Student added successfully.\n");
            } catch (Exception ex) {
                output.append("Error: " + ex.getMessage() + "\n");
            }
        });

        btnDisplay.addActionListener(e -> {
            ArrayList<Student> students = manager.displayAllStudents();
            output.setText("");
            for (Student student : students) {
                output.append(student.getStudentID() + " - " + student.getfName() + " " + student.getlName() + "\n");
            }
        });

        btnAverage.addActionListener(e -> {
            double avg = manager.calculateAverageGrade();
            output.append("Average Grade: " + avg + "\n");
        });

        frame.setVisible(true);
    }

}

