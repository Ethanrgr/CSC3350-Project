package model;

public class City {
    private int cityId;
    private String cityName;
    
    public City() {
    }
    
    public City(int cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }
    
    public int getCityId() {
        return cityId;
    }
    
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return cityId;
    }
    
    public void setId(int id) {
        this.cityId = id;
    }
    
    public String getName() {
        return cityName;
    }
    
    public void setName(String name) {
        this.cityName = name;
    }
} 