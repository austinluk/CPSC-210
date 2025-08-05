package model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

public class FinancialTracker implements Writable {

    private List<Transaction> transactions;

    /**
     * EFFECT: construct a financial tracker with empty transaction list
     */
    public FinancialTracker() {
        transactions = new ArrayList<>();
    }

    /**
     * MODIFIES: this EFFECTS: adds given transaction to the tracker, updates
     * budget if applicable REQUIRES: transaction is not null
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        EventLog.getInstance().logEvent(new Event("Transaction added: " + transaction.getDescription()
                + " ($" + transaction.getAmount() + ")"));
    }

    /**
     * MODIFIES: this EFFECTS: removes given transaction from the tracker,
     * updates budget if applicabl REQUIRES: transaction is not null
     */
    public boolean removeTransaction(Transaction transaction) {
        boolean removed = transactions.remove(transaction);
        if (removed) {
            EventLog.getInstance().logEvent(new Event("Transaction removed: " + transaction.getDescription()
                    + " ($" + transaction.getAmount() + ")"));
        }
        return removed;
    }

    /**
     * EFFECTS: returns list of all transactions
     */
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * EFFECTS: returns list of transactions filtered by category REQUIRES:
     * category is not null
     */
    public List<Transaction> getTransactionsByCategory(String category) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getCategory().equals(category)) {
                filteredTransactions.add(transaction);
            }
        }
        EventLog.getInstance().logEvent(new Event("Transactions filtered by category: " + category
                + " (found " + filteredTransactions.size() + " transactions)"));
        return filteredTransactions;
    }

    /**
     * MODIFIES: this EFFECTS: removes all transactions from the tracker
     */
    public void clearTransactions() {
        int count = transactions.size();
        transactions.clear();
        EventLog.getInstance().logEvent(new Event("All transactions cleared (" + count + " transactions removed)"));
    }

    /**
     * EFFECTS: returns total income amount
     */
    public double getTotalIncome() {
        double totalIncome = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                totalIncome += transaction.getAmount();
            }
        }
        return totalIncome;
    }

    /**
     * EFFECTS: returns number of transactions
     */
    public int getTransactionCount() {
        return transactions.size();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("transactions", transactionsToJson());
        return json;
    }

    // EFFECTS: returns transactions in this financial tracker as a JSON array
    private JSONArray transactionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Transaction t : transactions) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }
}