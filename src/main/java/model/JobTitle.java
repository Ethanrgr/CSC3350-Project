package model;

public class JobTitle {
    private int jobTitleId;
    private String title;
    
    public JobTitle() {
    }
    
    public JobTitle(int jobTitleId, String title) {
        this.jobTitleId = jobTitleId;
        this.title = title;
    }
    
    public int getJobTitleId() {
        return jobTitleId;
    }
    
    public void setJobTitleId(int jobTitleId) {
        this.jobTitleId = jobTitleId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
} 