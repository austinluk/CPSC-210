package model;

import java.time.LocalDate;

/**
 * Represents a financial transaction with an amount, description, category, date, and type.
 * A transaction can be either income (positive) or expense (negative).
 */
class Transaction {

    private double amount;
    private String description;
    private String category;
    private LocalDate date;
    /**
     * REQUIRES: amount > 0, description and category is not empty.
     * EFFECTS: creates a new transactions 
     */
    public Transaction(double amount, String description, String category, LocalDate date){
        // stub
    }

    /**
     * EFFECTS: returns the amount of the transaction
     */
    public double getAmount() {
        return 0; //stub
    }

    /** 
     * EFFECTS: returns the description of the transaction
     */
    public String getDescription(){
        return ""; //stub
    }

    /**
     * EFFECTS: returns the category of the transaction
     */
    public String getCategory(){
        return ""; // stub
    }


    /**
     * EFFECTS: returns the date of the transaction
     */
    public LocalDate getDate(){
        return null; // stub
    }

    

}