import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.toedter.calendar.JDateChooser;

public class StudentManagementApp {

    private StudentManagerImpl manager;
    private Student lastSelectedStudent = null;

    public StudentManagementApp() {

        JFrame frame = new JFrame("Student Management System");
        JPanel container = new JPanel();
        CardLayout cl = new CardLayout();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        container.setLayout(cl);
        container.setPreferredSize(new Dimension(600, 220));
        container.add(loginPanel(cl, container), "1");
        cl.show(container, "1");
        frame.add(container);
        frame.setSize(600, 220);
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

    public JPanel mainPanel(JPanel container) {
        ArrayList<Student> students = manager.displayAllStudents();
        JPanel main = new JPanel(new BorderLayout());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel detailPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        StudentTableModel tableModel = new StudentTableModel(students);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        JButton actionButton = new JButton("Add");
        actionButton.setActionCommand("Add");
        JButton clearButton = new JButton("Clear Form");
        JButton deleteButton = new JButton("Delete");

        JLabel fName = new JLabel("First Name");
        JLabel lName = new JLabel("Last Name");
        JLabel bDay = new JLabel("Birthday");
        JLabel grade = new JLabel("Grade");

        JTextField fNameTxt = new JTextField();
        JTextField lNameTxt = new JTextField();
        JDateChooser bDayChooser = new JDateChooser();
        JTextField gradeTxt = new JTextField();

        fNameTxt.setPreferredSize(new Dimension(165, 25));
        lNameTxt.setPreferredSize(new Dimension(165, 25));
        bDayChooser.setPreferredSize(new Dimension(165, 25));
        gradeTxt.setPreferredSize(new Dimension(165, 25));

        // Add components to detailPanel

        //-----------------------------------------------------------------------------------------------------

        gbc.insets = new Insets(5, 5, 5, 5);

        //-----------------------------------------------------------------------------------------------------

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;

        //-----------------------------------------------------------------------------------------------------

        gbc.gridy = 0;
        detailPanel.add(fName, gbc);

        gbc.gridy = 1;
        detailPanel.add(lName, gbc);

        gbc.gridy = 2;
        detailPanel.add(bDay, gbc);

        gbc.gridy = 3;
        detailPanel.add(grade, gbc);

        //-----------------------------------------------------------------------------------------------------

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;

        //-----------------------------------------------------------------------------------------------------

        gbc.gridy = 0;
        detailPanel.add(fNameTxt, gbc);

        gbc.gridy = 1;
        detailPanel.add(lNameTxt, gbc);

        gbc.gridy = 2;
        detailPanel.add(bDayChooser, gbc);

        gbc.gridy = 3;
        detailPanel.add(gradeTxt, gbc);

        //-----------------------------------------------------------------------------------------------------

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
                        table.clearSelection();
                        fNameTxt.setText("");
                        lNameTxt.setText("");
                        bDayChooser.setDate(null);
                        gradeTxt.setText("");
                        actionButton.setText("Add");
                        actionButton.setActionCommand("Add");
                    }
                }
            }
        });

        // Action listener for "Delete"
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
                    fNameTxt.setText("");
                    lNameTxt.setText("");
                    bDayChooser.setDate(null);
                    gradeTxt.setText("");
                    actionButton.setText("Add");
                    actionButton.setActionCommand("Add");
                    JOptionPane.showMessageDialog(container, "Student deleted.");
                }
            } else {
                JOptionPane.showMessageDialog(container, "No student selected.");
            }
        });

        clearButton.addActionListener(_ -> {
            table.clearSelection();
            fNameTxt.setText("");
            lNameTxt.setText("");
            bDayChooser.setDate(null);
            gradeTxt.setText("");
            actionButton.setText("Add");
            actionButton.setActionCommand("Add");
        });

        actionButton.addActionListener(e -> {
            String command = e.getActionCommand();

            if ("Add".equals(command)) {
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
                if(lastSelectedStudent != null) {
                    lastSelectedStudent.setfName(fNameTxt.getText());
                    lastSelectedStudent.setlName(lNameTxt.getText());
                    lastSelectedStudent.setbDay(new SimpleDateFormat("yyyy-MM-dd").format(bDayChooser.getDate()));
                    lastSelectedStudent.setGrade(Double.parseDouble(gradeTxt.getText()));

                    manager.updateStudent(lastSelectedStudent);
                    tableModel.fireTableDataChanged();
                }
            }
        });

        actionPanel.add(deleteButton);
        actionPanel.add(clearButton);
        actionPanel.add(actionButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        detailPanel.add(actionPanel, gbc);


        main.add(tableScrollPane, BorderLayout.CENTER);
        main.add(detailPanel, BorderLayout.EAST);

        return main;
    }

}

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