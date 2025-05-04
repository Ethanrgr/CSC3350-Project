package dto;

import model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

public class registrationRequest {
    private String email;
    private String password;
    private Role role;
    private Integer empid;
    
    public registrationRequest() {
    }
    
    
    public boolean isValid() {
        
        if (email == null || email.isBlank() || password == null || password.isBlank() || role == null) {
            return false;
        }
        
        return true;
    }
    
    public String getValidationMessage() {
        if (email == null || email.isBlank()) {
            return "Email is required";
        }
        
        if (password == null || password.isBlank()) {
            return "Password is required";
        }
        
        if (role == null) {
            return "Role is required";
        }
        
        return "All fields are valid";
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
    
    @JsonProperty("role")
    public void setRoleFromString(String role) {
        if (role != null) {
            this.role = Role.fromString(role);
        }
    }
    
    public Integer getEmpid() {
        return empid;
    }

    public void setEmpid(Integer empid) {
        this.empid = empid;
    }
}

