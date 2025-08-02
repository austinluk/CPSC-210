package model;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    private Transaction transaction;
    private LocalDate testDate;

    @BeforeEach
    void runBefore() {
        testDate = LocalDate.of(2025, 7, 12);
        transaction = new Transaction(1000, "Bi-Weekly Salary", "Income", testDate);
    }

    @Test
    void testConstructor() {
        assertEquals(1000, transaction.getAmount());
        assertEquals("Bi-Weekly Salary", transaction.getDescription());
        assertEquals("Income", transaction.getCategory());
        assertEquals(testDate, transaction.getDate());
    }
}
