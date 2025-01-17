import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.toedter.calendar.JDateChooser;

public class StudentManagementApp {

    // Fields to manage the application state and UI
    private StudentManagerImpl manager; // Backend manager for handling students
    private Student lastSelectedStudent = null; // Keeps track of the last selected student in the table
    private final static Dimension objectSize = new Dimension(165, 25); // Standard size for form fields

    // UI components for the main panel
    private final JTextField fNameTxt = new JTextField();
    private final JTextField lNameTxt = new JTextField();
    private final JDateChooser bDayChooser = new JDateChooser();
    private final JTextField gradeTxt = new JTextField();

    public StudentManagementApp() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        JFrame frame = new JFrame("Student Management System");
        JPanel container = new JPanel();
        CardLayout cl = new CardLayout();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        container.setLayout(cl);
        container.setPreferredSize(new Dimension(600, 220));

        // Add login panel to the container and show it initially
        // The main panel is initialized within the loginPanel after successful login
        container.add(loginPanel(loginPanel, container, cl), "1");
        cl.show(container, "1");
        frame.add(container);
        frame.setSize(600, 220);
        frame.pack();
        frame.setVisible(true);
    }

    // Creates and sets up the login panel
    public JPanel loginPanel(JPanel loginPanel, JPanel container, CardLayout cl) {
        JPanel login = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // UI components for login panel
        JLabel user = new JLabel("User:");
        JLabel pass = new JLabel("Password:");
        JLabel output = new JLabel();

        JTextField txtUser = new JTextField();
        JTextField txtPass = new JTextField();

        JButton loginButton = new JButton("Log In");

        txtUser.setPreferredSize(objectSize);
        txtPass.setPreferredSize(objectSize);

        // Add components to the panel using GridBagConstraints
        gbc.insets = new Insets(10, 0, 0, 5);
        gbcManager(login, user, gbc, 0, 0, 1, GridBagConstraints.EAST);
        gbcManager(login, pass, gbc, 0, 1, 1, GridBagConstraints.EAST);
        gbcManager(login, output, gbc, 0, 2, 1, GridBagConstraints.EAST);

        gbc.insets = new Insets(10, 0, 0, 0);
        gbcManager(login, txtUser, gbc, 1, 0, 1, GridBagConstraints.WEST);
        gbcManager(login, txtPass, gbc, 1, 1, 1, GridBagConstraints.WEST);
        gbcManager(login, loginButton, gbc, 1, 2, 1, GridBagConstraints.EAST);

        login.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Action listener for login button
        loginButton.addActionListener(_ -> {
            output.setText("Connecting...");
            try {
                String loginVal = txtUser.getText();
                String passVal = txtPass.getText();
                manager = new StudentManagerImpl(loginVal, passVal);
                output.setText("Connected!");

                // Create and switch to mainPanel after successful login
                container.add(mainPanel(container), "2");
                cl.show(container, "2");
            } catch (Exception exception) {
                output.setText("Failed!");
            }
        });
        return login;
    }

    // Creates and sets up the main panel for managing students
    public JPanel mainPanel(JPanel container) {
        int gbcx;
        int dir;

        ArrayList<Student> students = manager.displayAllStudents();
        JPanel main = new JPanel(new BorderLayout());

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel detailPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Table for displaying students
        StudentTableModel tableModel = new StudentTableModel(students);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Buttons for actions
        JButton actionButton = new JButton("Add");
        actionButton.setActionCommand("Add");
        JButton clearButton = new JButton("Clear Form");
        JButton deleteButton = new JButton("Delete");

        // Labels for student details
        JLabel fName = new JLabel("First Name");
        JLabel lName = new JLabel("Last Name");
        JLabel bDay = new JLabel("Birthday");
        JLabel grade = new JLabel("Grade");

        JLabel avg = new JLabel("Average Grade: ");
        JLabel avgVal = new JLabel(String.valueOf(manager.calculateAverageGrade()));

        // Set preferred sizes for input fields
        fNameTxt.setPreferredSize(objectSize);
        lNameTxt.setPreferredSize(objectSize);
        bDayChooser.setPreferredSize(objectSize);
        gradeTxt.setPreferredSize(objectSize);

        // Add components to detailPanel

        gbc.insets = new Insets(5, 5, 5, 5);
        gbcx = 0;
        dir = GridBagConstraints.EAST;

        gbcManager(detailPanel, fName, gbc, gbcx, 0, 1, dir);
        gbcManager(detailPanel, lName, gbc, gbcx, 1, 1, dir);
        gbcManager(detailPanel, bDay, gbc, gbcx, 2, 1, dir);
        gbcManager(detailPanel, grade, gbc, gbcx, 3, 1, dir);

        gbcx = 1;
        dir = GridBagConstraints.WEST;

        gbcManager(detailPanel, fNameTxt, gbc, gbcx, 0, 1, dir);
        gbcManager(detailPanel, lNameTxt, gbc, gbcx, 1, 1, dir);
        gbcManager(detailPanel, bDayChooser, gbc, gbcx, 2, 1, dir);
        gbcManager(detailPanel, gradeTxt, gbc, gbcx, 3, 1, dir);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disable automatic resizing
        table.getColumnModel().getColumn(0).setPreferredWidth(40); // ID column
        table.getColumnModel().getColumn(1).setPreferredWidth(145); // First Name column
        table.getColumnModel().getColumn(2).setPreferredWidth(145); // Last Name column

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double-click
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        lastSelectedStudent = tableModel.getStudentAt(selectedRow);
                        fNameTxt.setText(lastSelectedStudent.getfName());
                        lNameTxt.setText(lastSelectedStudent.getlName());
                        try {
                            bDayChooser.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(lastSelectedStudent.getbDay()));
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                        gradeTxt.setText(String.valueOf(lastSelectedStudent.getGrade()));
                        actionButton.setText("Edit");
                        actionButton.setActionCommand("Edit");
                    } else {
                        resetForm(table, actionButton);
                    }
                }
            }
        });

        // Action listener for delete button
        deleteButton.addActionListener(_ -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(container,
                        "Are you sure you want to delete this student?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Student selectedStudent = tableModel.getStudentAt(selectedRow);
                    manager.removeStudent(selectedStudent.getStudentID());
                    tableModel.removeStudentAt(selectedRow);
                    resetForm(table, actionButton);
                    avgVal.setText(String.valueOf(manager.calculateAverageGrade()));
                    JOptionPane.showMessageDialog(container, "Student deleted.");
                }
            } else {
                JOptionPane.showMessageDialog(container, "No student selected.");
            }
        });

        // Action listener for clear button
        clearButton.addActionListener(_ -> {
            resetForm(table, actionButton);
        });

        // Action listener for add/edit button
        actionButton.addActionListener(e -> {
            String command = e.getActionCommand();

            if ("Add".equals(command)) {
                // Add new student
                Student student = new Student(
                        fNameTxt.getText(),
                        lNameTxt.getText(),
                        new SimpleDateFormat("yyyy-MM-dd").format(bDayChooser.getDate()),
                        Double.parseDouble(gradeTxt.getText())
                );

                manager.addStudent(student);
                students.add(student);
                tableModel.fireTableDataChanged();

            } else if ("Edit".equals(command)) {
                // Edit existing student
                if(lastSelectedStudent != null) {
                    lastSelectedStudent.setfName(fNameTxt.getText());
                    lastSelectedStudent.setlName(lNameTxt.getText());
                    lastSelectedStudent.setbDay(new SimpleDateFormat("yyyy-MM-dd").format(bDayChooser.getDate()));
                    lastSelectedStudent.setGrade(Double.parseDouble(gradeTxt.getText()));

                    manager.updateStudent(lastSelectedStudent);
                    tableModel.fireTableDataChanged();
                }
            }
            avgVal.setText(String.valueOf(manager.calculateAverageGrade()));
        });

        // Add action buttons to actionPanel
        actionPanel.add(deleteButton);
        actionPanel.add(clearButton);
        actionPanel.add(actionButton);

        statsPanel.add(avg);
        statsPanel.add(avgVal);

        gbcManager(detailPanel, actionPanel, gbc, 0, 4, 2, GridBagConstraints.EAST);

        // Add panels to the main panel
        main.add(statsPanel, BorderLayout.NORTH);
        main.add(tableScrollPane, BorderLayout.CENTER);
        main.add(detailPanel, BorderLayout.EAST);

        return main;
    }

    // Helper method for managing GridBagConstraints
    private void gbcManager(JPanel panel, Component component, GridBagConstraints gbc, int x, int y, int w, int anchor) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.anchor = anchor;
        panel.add(component, gbc);
    }

    // Resets the form to its default state
    private void resetForm(JTable table,JButton actionButton) {
        table.clearSelection();
        fNameTxt.setText("");
        lNameTxt.setText("");
        bDayChooser.setDate(null);
        gradeTxt.setText("");
        actionButton.setText("Add");
        actionButton.setActionCommand("Add");
    }
}

// Custom table model for displaying student data
class StudentTableModel extends javax.swing.table.AbstractTableModel {
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
        return switch (columnIndex) {
            case 0 -> student.getStudentID();
            case 1 -> student.getfName();
            case 2 -> student.getlName();
            default -> null;
        };
    }

    public Student getStudentAt(int rowIndex) {
        return students.get(rowIndex);
    }

    public void removeStudentAt(int rowIndex) {
        students.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);

    }
}