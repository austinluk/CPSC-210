package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

public class FinancialTrackerTest {

    private FinancialTracker tracker;
    private Transaction income1;
    private Transaction income2;
    private Transaction expense1;
    private LocalDate testDate1;
    private LocalDate testDate2;

    @BeforeEach
    void runBefore() {
        tracker = new FinancialTracker();
        testDate1 = LocalDate.of(2025, 7, 12);
        testDate2 = LocalDate.of(2025, 7, 13);
        income1 = new Transaction(1000, "Bi-Week Salary", "Income", testDate1);
        income2 = new Transaction(500, "Freelance", "Income", testDate2);
        expense1 = new Transaction(-200, "Groceries", "Food", testDate1);
    }

    @Test
    void testConstructor() {
        assertEquals(0, tracker.getTransactionCount());
    }

    @Test
    void testAddSingleTransaction() {
        tracker.addTransaction(income1);
        assertEquals(1, tracker.getTransactionCount());
    }

    @Test
    void testAddMultipleTransactions() {
        tracker.addTransaction(income1);
        tracker.addTransaction(expense1);
        tracker.addTransaction(income2);
        assertEquals(3, tracker.getTransactionCount());
    }

    @Test
    void testRemoveTransaction() {
        tracker.addTransaction(income1);
        assertTrue(tracker.removeTransaction(income1));
    }

    @Test
    void testGetTransactions() {
        tracker.addTransaction(income1);
        tracker.addTransaction(income2);
        List<Transaction> transactions = tracker.getTransactions();
        assertEquals(2, transactions.size());
    }

    @Test
    void testGetTransactionsByCategory() {
        tracker.addTransaction(income1);
        tracker.addTransaction(income2);

        List<Transaction> incomeTransactions = tracker.getTransactionsByCategory("Income");
        assertEquals(2, incomeTransactions.size());

    }

    @Test
    void testGetTotalIncome() {
        tracker.addTransaction(income1);
        tracker.addTransaction(income2);

        assertEquals(1500.0, tracker.getTotalIncome()); // 1000 + 500
    }

    @Test
    void testGetTransactionsCount() {
        tracker.addTransaction(income1);
        tracker.addTransaction(expense1);
        tracker.addTransaction(income2);
        assertEquals(3, tracker.getTransactionCount());
    }
}
