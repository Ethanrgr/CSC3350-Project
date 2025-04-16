package dao;

import java.util.Date;

public class employee {
    private int empId;
    private String fName;
    private String lName;
    private String email;
    private Date hireDate; //String or Date datatype - will decide later
    private float salary;
    private String ssn;

    public employee(int empId, String fName, String lName, String email, Date hireDate, float salary, String ssn){
        this.empId = empId;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.hireDate = hireDate;
        this.salary = salary;
        this.ssn = ssn;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    public Date getHireDate() {
        return hireDate;
    }
    public void setSalary(float salary) {
        this.salary = salary;
    }
    public float getSalary() {
        return salary;
    }
    public void setEmpId(int empId) {
        this.empId = empId;
    }
    public int getEmpId() {
        return empId;
    }
    public void setfName(String fName) {
        this.fName = fName;
    }
    public String getfName() {
        return fName;
    }
    public void setlName(String lName) {
        this.lName = lName;
    }
    public String getlName() {
        return lName;
    }
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
    public String getSsn() {
        return ssn;
    }
}
