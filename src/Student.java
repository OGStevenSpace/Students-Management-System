public class Student {
    private String fName;
    private String lName;
    private String bDay;
    private double grade;
    private int studentID;

    public Student(String fName, String lName, String bDay, double grade, int studentID) {
        this.fName = fName;
        this.lName = lName;
        this.bDay = bDay;
        this.grade = grade;
        this.studentID = studentID;
    }

    public Student(String fName, String lName, String bDay, double grade) {
        this.fName = fName;
        this.lName = lName;
        this.bDay = bDay;
        this.grade = grade;
        this.studentID = 0;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getbDay() {
        return bDay;
    }

    public void setbDay(String bDay) {
        this.bDay = bDay;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String displayInfo() {
        return
                "ID: " + studentID + '\n' +
                "Full name: " + fName + " " + lName + '\n' +
                "Birth: " + bDay + '\n' +
                "Grade: " + grade;

    }
}
