import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentManagementApp {

    private StudentManagerImpl manager;
    JPanel studDet = new JPanel();

    public StudentManagementApp() {

        JFrame frame = new JFrame("Student Management System");
        JPanel container = new JPanel();
        CardLayout cl = new CardLayout();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        container.setLayout(cl);
        container.add(loginPanel(cl, container), "1");
        cl.show(container, "1");
        frame.add(container);
        frame.setSize(800, 600);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel loginPanel(CardLayout cl, JPanel container) {
        JPanel login = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel user = new JLabel("User: ");
        JLabel pass = new JLabel("Password: ");
        JLabel output = new JLabel();
        JTextField txtUser = new JTextField();
        JTextField txtPass = new JTextField();

        JButton loginButton = new JButton("Log In");

        txtUser.setPreferredSize(new Dimension(250, 25));
        txtPass.setPreferredSize(new Dimension(250, 25));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 5);
        login.add(user, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 0, 0);
        login.add(txtUser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 5);
        login.add(pass, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        login.add(txtPass, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 5);
        login.add(output, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 0);
        login.add(loginButton, gbc);

        login.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        loginButton.addActionListener(e -> {
            output.setText("Connecting...");
            try {
                String loginVal = txtUser.getText();
                String passVal = txtPass.getText();
                manager = new StudentManagerImpl(loginVal, passVal);
                output.setText("Connected!");
                // Create and switch to mainPanel after successful login
                container.add(mainPanel(cl, container), "2");
                cl.show(container, "2");
            } catch (Exception exception) {
                output.setText("Failed!");
            }
        });

        return login;
    }

    public JPanel mainPanel(CardLayout cl, JPanel container) {
        ArrayList<Student> students = manager.displayAllStudents();
        StudentTableModel tableModel = new StudentTableModel(students);
        JTable table = new JTable(tableModel);

        // Add "View" and "Delete" buttons
        JButton viewButton = new JButton("View");
        JButton deleteButton = new JButton("Delete");

        // Action listener for "View"
        viewButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Student selectedStudent = tableModel.getStudentAt(selectedRow);
                JOptionPane.showMessageDialog(container,
                        "Details:\n" +
                                selectedStudent.displayInfo()
                );
            } else {
                JOptionPane.showMessageDialog(container, "No student selected.");
            }
        });

        // Action listener for "Delete"
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(container,
                        "Are you sure you want to delete this student?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Student selectedStudent = tableModel.getStudentAt(selectedRow);
                    manager.removeStudent(selectedStudent.getStudentID());
                    tableModel.removeStudentAt(selectedRow);
                    JOptionPane.showMessageDialog(container, "Student deleted.");
                }
            } else {
                JOptionPane.showMessageDialog(container, "No student selected.");
            }
        });

        // Panel layout
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.add(viewButton);
        actionPanel.add(deleteButton);

        JScrollPane tableScrollPane = new JScrollPane(table);
        JPanel main = new JPanel(new BorderLayout());
        main.add(tableScrollPane, BorderLayout.CENTER);
        main.add(actionPanel, BorderLayout.SOUTH);

        return main;
    }

}

class StudentTableModel extends AbstractTableModel {
    private final ArrayList<Student> students;
    private final String[] columnNames;

    public StudentTableModel(ArrayList<Student> students) {
        this.students = students;
        this.columnNames = new String[] {"ID", "First Name", "Last Name"}; //, "Birthday", "Grade"};
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch (columnIndex) {
            case 0: return student.getStudentID();
            case 1: return student.getfName();
            case 2: return student.getlName();
            //case 3: return student.getbDay();
            //case 4: return student.getGrade();
            default: return null;
        }
    }

    public Student getStudentAt(int rowIndex) {
        return students.get(rowIndex);
    }

    public void removeStudentAt(int rowIndex) {
        students.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}