package persistence;

import model.FinancialTracker;
import model.Transaction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            FinancialTracker tracker = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyFinancialTracker() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFinancialTracker.json");
        try {
            FinancialTracker tracker = reader.read();
            assertEquals(0, tracker.getTransactionCount());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralFinancialTracker() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralFinancialTracker.json");
        try {
            FinancialTracker tracker = reader.read();
            List<Transaction> transactions = tracker.getTransactions();
            assertEquals(2, transactions.size());
            checkTransaction(1000.0, "Salary", "Income",
                    LocalDate.of(2025, 1, 15), transactions.get(0));
            checkTransaction(-50.0, "Groceries", "Food",
                    LocalDate.of(2025, 1, 16), transactions.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderFinancialTrackerWithEdgeCases() {
        JsonReader reader = new JsonReader("./data/testReaderEdgeCaseFinancialTracker.json");
        try {
            FinancialTracker tracker = reader.read();
            List<Transaction> transactions = tracker.getTransactions();
            assertEquals(3, transactions.size());

            // Test zero amount transaction
            checkTransaction(0.0, "Balance adjustment", "Other",
                    LocalDate.of(2025, 1, 1), transactions.get(0));

            // Test large negative amount
            checkTransaction(-999.99, "Rent & utilities", "Housing",
                    LocalDate.of(2025, 1, 2), transactions.get(1));

            // Test large positive amount  
            checkTransaction(5000.0, "Bonus payment", "Income",
                    LocalDate.of(2025, 1, 3), transactions.get(2));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
