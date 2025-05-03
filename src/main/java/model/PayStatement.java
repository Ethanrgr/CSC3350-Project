package model;

import java.time.LocalDate;

public class PayStatement {
    private int id;
    private int empId;
    private LocalDate payDate;
    private double amount;
    
    public PayStatement(int id, int empId, LocalDate payDate, double amount) {
        this.id = id;
        this.empId = empId;
        this.payDate = payDate;
        this.amount = amount;
    }
    
    public PayStatement() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getEmpId() {
        return empId;
    }
    
    public void setEmpId(int empId) {
        this.empId = empId;
    }
    
    public LocalDate getPayDate() {
        return payDate;
    }
    
    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
