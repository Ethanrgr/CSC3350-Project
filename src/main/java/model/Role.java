package model;

public enum Role {
    EMPLOYEE,
    ADMIN;
    public static Role fromString(String value) {
        try {
            return Role.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + value +
                    ". Accepted values are: EMPLOYEE, ADMIN");
        }
    }
}


