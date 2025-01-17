# Student Management System

This is a Java-based student management system that utilizes a graphical user interface (GUI) for managing student data. The system allows users to perform CRUD operations (Create, Read, Update, and Delete) on student records stored in a PostgreSQL database.

## Features

- **Login**: A login screen to authenticate the user.
- **Student CRUD Operations**: Add, edit, delete, and view student records.
- **Student Table**: Displays a table with student information such as ID, first name, and last name.
- **Date Picker**: A date picker for selecting the student's birthday.
- **Database Integration**: PostgreSQL database integration for storing student data.

## Setup

### Requirements

- Java 8 or higher.
- PostgreSQL database running locally or remotely.
- A JDBC driver for PostgreSQL (e.g., `postgresql-42.2.x.jar`).
- The calendar library (`jcalendar-1.4.jar` -> https://toedter.com/jcalendar/)

### Database Setup

The system connects to a PostgreSQL database running on `localhost:5432` by default. Ensure the database is accessible and create the following table:

```sql
CREATE TABLE IF NOT EXISTS students (
    studentID SERIAL PRIMARY KEY,
    fName VARCHAR(30),
    lName VARCHAR(100),
    bDay DATE,
    grade DOUBLE PRECISION
);
```

## Running the Application
1. Compile the Java code: 
```
javac Main.java StudentManagementApp.java StudentManagerImpl.java Student.java StudentTableModel.java
```
2. Run the Main class to start the application: 
```
java Main
```

### Login Credentials
- The application requires login credentials (username and password) to access the student management features.
- Upon successful login, users will be able to view, add, edit, and delete student records.

## How to Use
### Login Screen
Enter the username and password to log in to the system.
If the credentials are correct, the user will be redirected to the main student management panel.
### Student Management Panel

The main panel displays a table of all students with columns: ID, First Name, and Last Name.
You can interact with the student records by double-clicking a row to populate the form fields for editing.

You can:
- Add a new student.
- Edit an existing student's details.
- Delete a student record.

### Form Fields
- **First Name:** The student's first name.
- **Last Name:** The student's last name.
- **Birthday:** The student's birthdate, selected using the date picker.
- **Grade:** The student's grade (a numeric value).

### Actions
- **Add:** Adds a new student to the database and updates the table.
- **Edit:** Edits the selected student's details and updates the database.
- **Delete:** Deletes the selected student's record from the database.

### Table
The table displays the list of students.
Clicking a row twice will load the student's data into the form for editing.

The table columns are:
- **ID:** The student's ID.
- **First Name:** The student's first name.
- **Last Name:** The student's last name.

## Code Overview
### StudentManagementApp
This is the main GUI class that handles the login screen, main panel, and student management interface. It uses CardLayout for switching between the login and main panels.

### StudentManagerImpl
This class implements the StudentManager interface and provides the CRUD operations (add, remove, update, display) for managing student data. It connects to the DB with DatabaseManager class.

### DatabaseManager
This class provides methods for connecting to the PostgreSQL database and to execute the CRUD operations (add, remove, update, display) using queries stored in the Queries class. It can return the necessary values after the action is completed.

### Student
This class represents a student with attributes such as first name, last name, birthday, grade, and student ID.

### StudentTableModel
This class is a custom table model used to display student data in the table. It extends AbstractTableModel and provides the necessary methods for displaying and manipulating the data.

### StudentManager Interface
This interface defines the methods for managing student records in the database, including adding, removing, updating, displaying all students, and calculating the average grade.

## License
This project is licensed under the MIT License.
