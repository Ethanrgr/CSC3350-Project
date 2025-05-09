package model;


public class User {
    private String email;
    private String password;
    private Role role;

    public Integer getEmpid() {
        return empid;
    }

    public void setEmpid(Integer empid) {
        this.empid = empid;
    }

    private Integer empid;

    public User(String email, String password, Role role){
        this.email = email;
        this.password = password;
        this.role = role;
        this.empid = null;
    }
    public User(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String toString(){
        return "Email: " + this.email + " Password: " + this.password + " Role: " + this.role;
    }

}

