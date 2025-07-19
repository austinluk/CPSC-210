package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import model.FinancialTracker;
import model.Transaction;

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

        System.out.println("\n=== Welcome to Financial Tracker ===");

        while (keepGoing) {
            displayMenu();
            String command = input.nextLine().toLowerCase().trim();
            processCommand(command);
        }

        System.out.println("\nGoodbye!");
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
        System.out.println("6. Quit");
        System.out.println("\ts -> :test:save work room to file");
        System.out.println("\tl -> :test:load work room from file");
        System.out.print("Please select an option (1-6): ");
    }

    /**
     * MODIFIES: this EFFECTS: processes the command entered by user and
     * executes the corresponding action; calls appropriate method based on
     * command or displays error for invalid input
     */
    private void processCommand(String command) {
        switch (command) {
            case "1":
                addTransaction();
                break;
            case "2":
                viewAllTransactions();
                break;
            case "3":
                viewFinancialSummary();
                break;
            case "4":
                filterTransactionsByCategory();
                break;
            case "5":
                deleteTransaction();
                break;
            case "6":
                keepGoing = false;
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
                break;
        }
    }

    /**
     * MODIFIES: this EFFECTS: prompts user for transaction details and adds a
     * new transaction to the tracker; validates that description and category
     * are not empty; uses current date if no date is provided; displays success
     * message with transaction type
     */
    private void addTransaction() {
        System.out.println("\n--- Add New Transaction ---");

        System.out.print("Enter amount (positive for income, negative for expense): $");
        double amount = Double.parseDouble(input.nextLine());

        System.out.print("Enter description: ");
        String description = input.nextLine().trim();

        if (description.isEmpty()) {
            System.out.println("Description cannot be empty!");
            return;
        }

        System.out.print("Enter category (e.g., Food, Rent, Salary, Entertainment): ");
        String category = input.nextLine().trim();

        if (category.isEmpty()) {
            System.out.println("Category cannot be empty!");
            return;
        }

        System.out.print("Enter date (YYYY-MM-DD) or press Enter for today: ");
        String dateInput = input.nextLine().trim();
        LocalDate date;

        if (dateInput.isEmpty()) {
            date = LocalDate.now();
        } else {
            date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        Transaction transaction = new Transaction(amount, description, category, date);
        tracker.addTransaction(transaction);

        String type;
        if (amount >= 0) {
            type = "Income";

        } else {
            type = "Expense";
        }
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
        System.out.print("Enter category to filter by: ");
        String category = input.nextLine().trim();

        if (category.isEmpty()) {
            System.out.println("Category cannot be empty!");
            return;
        }

        List<Transaction> filteredTransactions = tracker.getTransactionsByCategory(category);

        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions found for category: " + category);
            return;
        }

        System.out.println("\nTransactions in category '" + category + "':");

        double categoryTotal = 0.0;
        for (int i = 0; i < filteredTransactions.size(); i++) {
            Transaction currTransaction = filteredTransactions.get(i);
            String type = "";

            if (currTransaction.getAmount() >= 0) {
                type = "Income";
            } else {
                type = "Expense";
            }

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

        // Display transactions with numbers
        System.out.println("Select a transaction to delete:");
        for (int i = 0; i < transactions.size(); i++) {
            Transaction currTransaction = transactions.get(i);
            String type = "";

            if (currTransaction.getAmount() >= 0) {
                type = "Income";
            } else {
                type = "Expense";
            }

            System.out.println((i + 1) + ". [" + type + "] " + currTransaction.getDescription()
                    + " - $" + Math.abs(currTransaction.getAmount()) + " (" + currTransaction.getCategory()
                    + ") - " + currTransaction.getDate());
        }

        System.out.print("Enter transaction number to delete (or 0 to cancel): ");
        int choice;

        do {
            choice = Integer.parseInt(input.nextLine());

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

        } while (choice != 0);
    }
}
