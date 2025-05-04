package model;

public class State {
    private int stateId;
    private String stateName;
    private String abbreviation;
    
    public State() {
    }
    
    public State(int stateId, String stateName, String abbreviation) {
        this.stateId = stateId;
        this.stateName = stateName;
        this.abbreviation = abbreviation;
    }
    
    public int getStateId() {
        return stateId;
    }
    
    public void setStateId(int stateId) {
        this.stateId = stateId;
    }
    
    public String getStateName() {
        return stateName;
    }
    
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    
    public String getAbbreviation() {
        return abbreviation;
    }
    
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    
    // Backward compatibility methods
    public int getId() {
        return stateId;
    }
    
    public void setId(int id) {
        this.stateId = id;
    }
    
    public String getName() {
        return stateName;
    }
    
    public void setName(String name) {
        this.stateName = name;
    }
} 