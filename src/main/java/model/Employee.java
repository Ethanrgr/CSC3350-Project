package model;

public class Employee {
    private int empId;
    private String fName;
    private String lName;
    private String email;
    private String hireDate; //String or Date datatype - will decide later
    private float salary;
    private String ssn;
    
    // Address information
    private String street;
    private String zip;
    private City city;
    private State state;
    private String gender;
    private String identifiedRace;
    private String dob;
    private String mobilePhone;

    public Employee(int empId, String fName, String lName, String email, String hireDate, float salary, String ssn){
        this.empId = empId;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.hireDate = hireDate;
        this.salary = salary;
        this.ssn = ssn;
    }
    
    public Employee(int empId, String fName, String lName, String email, String hireDate, float salary, String ssn,
                   String street, String zip, City city, State state, String gender, String identifiedRace, 
                   String dob, String mobilePhone) {
        this.empId = empId;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.hireDate = hireDate;
        this.salary = salary;
        this.ssn = ssn;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.state = state;
        this.gender = gender;
        this.identifiedRace = identifiedRace;
        this.dob = dob;
        this.mobilePhone = mobilePhone;
    }
    
    public Employee() {
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }
    public String getHireDate() {
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
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getZip() {
        return zip;
    }
    
    public void setZip(String zip) {
        this.zip = zip;
    }
    
    public City getCity() {
        return city;
    }
    
    public void setCity(City city) {
        this.city = city;
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getIdentifiedRace() {
        return identifiedRace;
    }
    
    public void setIdentifiedRace(String identifiedRace) {
        this.identifiedRace = identifiedRace;
    }
    
    public String getDob() {
        return dob;
    }
    
    public void setDob(String dob) {
        this.dob = dob;
    }
    
    public String getMobilePhone() {
        return mobilePhone;
    }
    
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
