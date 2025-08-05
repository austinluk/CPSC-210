package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import model.Event;
import model.EventLog;
import model.FinancialTracker;
import model.Transaction;
import persistence.JsonReader;
import persistence.JsonWriter;

/**
 * Financial Tracker console application that allows users to manage their
 * personal finances. Users can add, view, edit, and delete transactions, as
 * well as view financial summaries.
 */
public class FinancialApp {

    private static final String JSON_STORE = "./data/FinancialHistory.json";
    private FinancialTracker tracker;
    private Scanner input;
    private boolean keepGoing;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    /**
     * EFFECTS: runs the financial tracker application
     */
    public FinancialApp() {
        runApp();
    }

    /**
     * MODIFIES: this EFFECTS: processes user input
     */
    private void runApp() {
        keepGoing = true;
        tracker = new FinancialTracker();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        System.out.println("\n=== Welcome to Financial Tracker ===");

        while (keepGoing) {
            displayMenu();
            String command = input.nextLine().toLowerCase().trim();
            processCommand(command);
        }

        System.out.println("\nGoodbye!");
        printEventLog();
    }

    /**
     * EFFECTS: prints all events from the event log to console
     */
    private void printEventLog() {
        System.out.println("\nEvent Log:");
        System.out.println("----------");
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.toString());
            System.out.println();
        }
    }

    /**
     * EFFECTS: displays the main menu to user
     */
    private void displayMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add Transaction");
        System.out.println("2. View All Transactions");
        System.out.println("3. View Financial Summary");
        System.out.println("4. Filter Transactions by Category");
        System.out.println("5. Delete Transaction");
        System.out.println("6. Save Financial History");
        System.out.println("7. Load Financial History");
        System.out.println("8. Quit");
        System.out.print("Please select an option (1-8): ");
    }

    /**
     * MODIFIES: this EFFECTS: processes the command entered by user and
     * executes the corresponding action; calls appropriate method based on
     * command or displays error for invalid input
     */
    private void processCommand(String command) {
        executeCommand(command);
    }

    /**
     * EFFECTS: executes the appropriate action based on the command
     */
    private void executeCommand(String command) {
        if (executeTransactionCommands(command)) {
            return;
        }
        if (executeFileCommands(command)) {
            return;
        }
        if (command.equals("8")) {
            keepGoing = false;
        } else {
            handleInvalidCommand();
        }
    }

    /**
     * EFFECTS: executes transaction-related commands; returns true if command
     * was handled
     */
    private boolean executeTransactionCommands(String command) {
        switch (command) {
            case "1":
                addTransaction();
                return true;
            case "2":
                viewAllTransactions();
                return true;
            case "3":
                viewFinancialSummary();
                return true;
            case "4":
                filterTransactionsByCategory();
                return true;
            case "5":
                deleteTransaction();
                return true;
            default:
                return false;
        }
    }

    /**
     * EFFECTS: executes file-related commands; returns true if command was
     * handled
     */
    private boolean executeFileCommands(String command) {
        switch (command) {
            case "6":
                saveFinancialHistory();
                return true;
            case "7":
                loadFinancialHistory();
                return true;
            default:
                return false;
        }
    }

    /**
     * EFFECTS: displays error message for invalid command
     */
    private void handleInvalidCommand() {
        System.out.println("Invalid selection. Please try again.");
    }

    /**
     * MODIFIES: this EFFECTS: prompts user for transaction details and adds a
     * new transaction to the tracker; validates that description and category
     * are not empty; uses current date if no date is provided; displays success
     * message with transaction type
     */
    private void addTransaction() {
        System.out.println("\n--- Add New Transaction ---");

        double amount = getTransactionAmount();
        String description = getTransactionDescription();
        if (description == null) {
            return;
        }

        String category = getTransactionCategory();
        if (category == null) {
            return;
        }

        LocalDate date = getTransactionDate();

        Transaction transaction = new Transaction(amount, description, category, date);
        tracker.addTransaction(transaction);
        displayTransactionAdded(amount);
    }

    /**
     * EFFECTS: prompts user for and returns transaction amount
     */
    private double getTransactionAmount() {
        System.out.print("Enter amount (positive for income, negative for expense): $");
        return Double.parseDouble(input.nextLine());
    }

    /**
     * EFFECTS: prompts user for and returns transaction description; returns
     * null if empty
     */
    private String getTransactionDescription() {
        System.out.print("Enter description: ");
        String description = input.nextLine().trim();
        if (description.isEmpty()) {
            System.out.println("Description cannot be empty!");
            return null;
        }
        return description;
    }

    /**
     * EFFECTS: prompts user for and returns transaction category; returns null
     * if empty
     */
    private String getTransactionCategory() {
        System.out.print("Enter category (e.g., Food, Rent, Salary, Entertainment): ");
        String category = input.nextLine().trim();
        if (category.isEmpty()) {
            System.out.println("Category cannot be empty!");
            return null;
        }
        return category;
    }

    /**
     * EFFECTS: prompts user for and returns transaction date; uses current date
     * if empty
     */
    private LocalDate getTransactionDate() {
        System.out.print("Enter date (YYYY-MM-DD) or press Enter for today: ");
        String dateInput = input.nextLine().trim();
        if (dateInput.isEmpty()) {
            return LocalDate.now();
        } else {
            return LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

    /**
     * EFFECTS: displays success message for added transaction
     */
    private void displayTransactionAdded(double amount) {
        String type = amount >= 0 ? "Income" : "Expense";
        System.out.println(type + " transaction added successfully!");
    }

    /**
     * EFFECTS: displays all transactions in the tracker with their details;
     * shows transaction number, type (Income/Expense), description, amount,
     * category, and date; displays message if no transactions found
     */
    private void viewAllTransactions() {
        System.out.println("\n--- All Transactions ---");
        List<Transaction> transactions = tracker.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("Total transactions: " + transactions.size());
        System.out.println();

        for (int i = 0; i < transactions.size(); i++) {
            Transaction currTransaction = transactions.get(i);

            String type;
            if (currTransaction.getAmount() >= 0) {
                type = "Income";
            } else {
                type = "Expense";
            }
            System.out.println((i + 1) + ". [" + type + "] " + currTransaction.getDescription()
                    + " - $" + Math.abs(currTransaction.getAmount()) + " (" + currTransaction.getCategory()
                    + ") - " + currTransaction.getDate());
        }
    }

    /**
     * EFFECTS: displays financial summary including total income, total
     * expenses, current balance, and total transaction count; provides feedback
     * on financial status (positive or negative balance)
     */
    private void viewFinancialSummary() {
        System.out.println("\n--- Financial Summary ---");

        double totalIncome = tracker.getTotalIncome();
        double totalExpenses = getTotalExpenses();
        double balance = totalIncome + totalExpenses; // expenses are negative

        System.out.println("Total Income: $" + totalIncome);
        System.out.println("Total Expenses: $" + Math.abs(totalExpenses));
        System.out.println("Current Balance: $" + balance);

        if (balance >= 0) {
            System.out.println("You're in the positive!");
        } else {
            System.out.println("You're spending more than you earn.");
        }

        System.out.println("Total Transactions: " + tracker.getTransactionCount());
    }

    /**
     * EFFECTS: calculates and returns the total amount of all expense
     * transactions; returns sum of all negative transaction amounts
     */
    private double getTotalExpenses() {
        double totalExpenses = 0.0;
        for (Transaction transaction : tracker.getTransactions()) {
            if (transaction.getAmount() < 0) {
                totalExpenses += transaction.getAmount();
            }
        }
        return totalExpenses;
    }

    /**
     * EFFECTS: prompts user for category name and displays all transactions in
     * that category; shows transaction details and calculates category total;
     * displays message if category is empty or no transactions found
     */
    private void filterTransactionsByCategory() {
        System.out.println("\n--- Filter by Category ---");
        String category = getCategoryInput();
        if (category == null) {
            return;
        }

        List<Transaction> filteredTransactions = tracker.getTransactionsByCategory(category);
        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions found for category: " + category);
            return;
        }

        displayCategoryTransactions(category, filteredTransactions);
    }

    /**
     * EFFECTS: prompts user for category and returns it; returns null if empty
     */
    private String getCategoryInput() {
        System.out.print("Enter category to filter by: ");
        String category = input.nextLine().trim();
        if (category.isEmpty()) {
            System.out.println("Category cannot be empty!");
            return null;
        }
        return category;
    }

    /**
     * EFFECTS: displays all transactions in the given category with total
     */
    private void displayCategoryTransactions(String category, List<Transaction> transactions) {
        System.out.println("\nTransactions in category '" + category + "':");
        double categoryTotal = 0.0;

        for (int i = 0; i < transactions.size(); i++) {
            Transaction currTransaction = transactions.get(i);
            String type = currTransaction.getAmount() >= 0 ? "Income" : "Expense";

            System.out.println((i + 1) + ". [" + type + "] " + currTransaction.getDescription()
                    + " - $" + Math.abs(currTransaction.getAmount()) + " - " + currTransaction.getDate());
            categoryTotal += currTransaction.getAmount();
        }

        System.out.println("\nCategory Total: $" + categoryTotal);
    }

    /**
     * MODIFIES: this EFFECTS: displays all transactions with numbers and allows
     * user to select one for deletion; removes the selected transaction from
     * the tracker; handles invalid input and provides user feedback; allows
     * user to cancel deletion by entering 0
     */
    private void deleteTransaction() {
        System.out.println("\n--- Delete Transaction ---");
        List<Transaction> transactions = tracker.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions to delete.");
            return;
        }

        displayTransactionsForDeletion(transactions);
        handleTransactionDeletion(transactions);
    }

    /**
     * EFFECTS: displays all transactions with numbers for deletion selection
     */
    private void displayTransactionsForDeletion(List<Transaction> transactions) {
        System.out.println("Select a transaction to delete:");
        for (int i = 0; i < transactions.size(); i++) {
            Transaction currTransaction = transactions.get(i);
            String type = currTransaction.getAmount() >= 0 ? "Income" : "Expense";

            System.out.println((i + 1) + ". [" + type + "] " + currTransaction.getDescription()
                    + " - $" + Math.abs(currTransaction.getAmount()) + " (" + currTransaction.getCategory()
                    + ") - " + currTransaction.getDate());
        }
    }

    /**
     * MODIFIES: this EFFECTS: handles user input for transaction deletion
     */
    private void handleTransactionDeletion(List<Transaction> transactions) {
        System.out.print("Enter transaction number to delete (or 0 to cancel): ");
        int choice = Integer.parseInt(input.nextLine());

        if (choice == 0) {
            System.out.println("Delete cancelled.");
            return;
        }

        if (choice < 1 || choice > transactions.size()) {
            System.out.println("Invalid transaction number.");
            return;
        }

        Transaction toDelete = transactions.get(choice - 1);
        boolean removed = tracker.removeTransaction(toDelete);

        if (removed) {
            System.out.println("Transaction deleted successfully!");
        } else {
            System.out.println("Failed to delete transaction.");
        }
    }

    // EFFECTS: saves the financial tracker to file
    private void saveFinancialHistory() {
        try {
            jsonWriter.open();
            jsonWriter.write(tracker);
            jsonWriter.close();
            System.out.println("Saved Financial History to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads financial tracker from file
    private void loadFinancialHistory() {
        try {
            tracker = jsonReader.read();
            System.out.println("Loaded Financial History from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
