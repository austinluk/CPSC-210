package model;

import java.util.List;

public class FinancialTracker {
    private List<Transaction> transactions;

    /**
     * EFFECT: construct a financial tracker with empty transaction list
     */
    public FinancialTracker(){
        // stub
    }

    /**
     * MODIFIES: this
     * EFFECTS: adds given transaction to the tracker, updates budget if applicable
     * REQUIRES: transaction is not null
     */
    public void addTransaction(Transaction transaction) {
        // stub
    }
    
    /**
     * MODIFIES: this
     * EFFECTS: removes given transaction from the tracker, updates budget if applicabl
     * REQUIRES: transaction is not null
     */
    public boolean removeTransaction(Transaction transaction) {
        return false; // stub
    }

    /**
     * EFFECTS: returns list of all transactions
     */
    public List<Transaction> getTransactions() {
        return null; //stub
    }

    /**
     * EFFECTS: returns list of transactions filtered by category
     * REQUIRES: category is not null
     */
    public List<Transaction> getTransactionsByCategory(String category) {
        return null; // stub
    }

    /**
     * EFFECTS: returns total income amount
     */
    public double getTotalIncome() {
        return 0; // stub
    }


    /**
     * EFFECTS: returns number of transactions
     */
    public int getTransactionCount() {
        return 0; // stub
    }
}

