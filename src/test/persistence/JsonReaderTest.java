package persistence;

import model.FinancialTracker;
import model.Transaction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

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

    @Test
    void testReaderEmptyFile() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFile.json");
        try {
            FinancialTracker tracker = reader.read();
            fail("Exception expected for empty file");
        } catch (IOException e) {
            // Expected
        } catch (Exception e) {
            // Expected - JSON parsing error for empty file
        }
    }

    @Test
    void testReaderInvalidDateFormat() {
        JsonReader reader = new JsonReader("./data/testReaderInvalidDate.json");
        try {
            FinancialTracker tracker = reader.read();
            fail("Exception expected for invalid date format");
        } catch (IOException e) {
            // Expected
        } catch (Exception e) {
            // Expected - date parsing error
        }
    }

    @Test
    void testReaderMissingRequiredFields() {
        JsonReader reader = new JsonReader("./data/testReaderMissingFields.json");
        try {
            FinancialTracker tracker = reader.read();
            fail("Exception expected for missing required fields");
        } catch (IOException e) {
            // Expected
        } catch (Exception e) {
            // Expected - JSON parsing error for missing fields
        }
    }

    @Test
    void testReaderLargeTransactionSet() {
        JsonReader reader = new JsonReader("./data/testReaderLargeSet.json");
        try {
            FinancialTracker tracker = reader.read();
            List<Transaction> transactions = tracker.getTransactions();
            assertEquals(15, transactions.size());

            // Verify first transaction
            checkTransaction(1500.0, "Transaction 1", "Income",
                    LocalDate.of(2025, 1, 1), transactions.get(0));

            // Verify middle transaction (salary payment)
            checkTransaction(2500.0, "Salary payment", "Income",
                    LocalDate.of(2025, 1, 6), transactions.get(5));

            // Verify rent payment (largest expense)
            checkTransaction(-1200.0, "Rent payment", "Housing",
                    LocalDate.of(2025, 1, 12), transactions.get(11));

            // Verify last transaction
            checkTransaction(-250.0, "Car insurance", "Insurance",
                    LocalDate.of(2025, 1, 15), transactions.get(14));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderSpecialCharactersInStrings() {
        JsonReader reader = new JsonReader("./data/testReaderSpecialChars.json");
        try {
            FinancialTracker tracker = reader.read();
            List<Transaction> transactions = tracker.getTransactions();
            assertEquals(2, transactions.size());

            // Test unicode and special characters
            checkTransaction(-25.50, "Café & restaurant bill", "Food",
                    LocalDate.of(2025, 7, 18), transactions.get(0));
            checkTransaction(100.0, "Deposit (ATM #123) \"Cash\"", "Banking",
                    LocalDate.of(2025, 7, 18), transactions.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
