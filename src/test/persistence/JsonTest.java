package persistence;

import model.Transaction;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkTransaction(double amount, String description, String category,
            LocalDate date, Transaction transaction) {
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(category, transaction.getCategory());
        assertEquals(date, transaction.getDate());
    }
}
