package dto;

// No extra imports needed for this simple validation approach

public class CreateEmployeeRequest {
    private Integer empid;
    private String fName;
    private String lName;
    private String email;
    private String hireDate; 
    private Float salary;    
    private String ssn;
    
    // Address information
    private String street;
    private String zip;
    private Integer cityId;
    private Integer stateId;
    private String gender;
    private String identifiedRace;
    private String dob;
    private String mobilePhone;

    public CreateEmployeeRequest() {
    }

    public Integer getEmpid() { return empid; }
    public void setEmpid(Integer empid) { this.empid = empid; }

    public String getfName() { return fName; }
    public void setfName(String fName) { this.fName = fName; }

    public String getlName() { return lName; }
    public void setlName(String lName) { this.lName = lName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }

    public Float getSalary() { return salary; }
    public void setSalary(Float salary) { this.salary = salary; }

    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    
    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }
    
    public Integer getCityId() { return cityId; }
    public void setCityId(Integer cityId) { this.cityId = cityId; }
    
    public Integer getStateId() { return stateId; }
    public void setStateId(Integer stateId) { this.stateId = stateId; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getIdentifiedRace() { return identifiedRace; }
    public void setIdentifiedRace(String identifiedRace) { this.identifiedRace = identifiedRace; }
    
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    
    public String getMobilePhone() { return mobilePhone; }
    public void setMobilePhone(String mobilePhone) { this.mobilePhone = mobilePhone; }

    public boolean isValid() {
        return empid != null && empid > 0 &&
               fName != null && !fName.isBlank() &&
               lName != null && !lName.isBlank() &&
               email != null && !email.isBlank() && 
               hireDate != null && !hireDate.isBlank() && 
               ssn != null && !ssn.isBlank() &&
               salary != null && salary > 0;
    }

    
    public String getValidationMessage() {
        if (empid == null) return "Employee ID (empid) is required.";
        if (empid <= 0) return "Employee ID (empid) must be a positive number.";
        if (fName == null || fName.isBlank()) return "First name (fName) is required.";
        if (lName == null || lName.isBlank()) return "Last name (lName) is required.";
        if (email == null || email.isBlank()) return "Email is required.";
        
        if (hireDate == null || hireDate.isBlank()) return "Hire date (hireDate) is required.";
       
        if (ssn == null || ssn.isBlank()) return "SSN is required.";
        if (salary == null) return "Salary is required.";
        if (salary <= 0) return "Salary must be positive.";
        return "Request is valid."; 
    }
} 