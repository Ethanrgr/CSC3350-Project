package model;

import java.time.LocalDate;

public class Address {
    private int empId;  
    private String street;
    private int cityId;
    private int stateId;
    private String zip;
    private String gender;
    private String race;
    private LocalDate dob;
    private String phone;
    
    
    private City city;
    private State state;
    
    public Address() {
    }
    
    public Address(int empId, String street, int cityId, int stateId, String zip, 
                   String gender, String race, LocalDate dob, String phone) {
        this.empId = empId;
        this.street = street;
        this.cityId = cityId;
        this.stateId = stateId;
        this.zip = zip;
        this.gender = gender;
        this.race = race;
        this.dob = dob;
        this.phone = phone;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
        if (city != null) {
            this.cityId = city.getId();
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        if (state != null) {
            this.stateId = state.getId();
        }
    }
} 