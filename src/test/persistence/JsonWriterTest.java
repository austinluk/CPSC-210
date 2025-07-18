package persistence;

import model.FinancialTracker;
import model.Transaction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            FinancialTracker tracker = new FinancialTracker();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyFinancialTracker() {
        try {
            FinancialTracker tracker = new FinancialTracker();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyFinancialTracker.json");
            writer.open();
            writer.write(tracker);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyFinancialTracker.json");
            tracker = reader.read();
            assertEquals(0, tracker.getTransactionCount());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralFinancialTracker() {
        try {
            FinancialTracker tracker = new FinancialTracker();
            tracker.addTransaction(new Transaction(1000.0, "Salary", "Income",
                    LocalDate.of(2025, 1, 15)));
            tracker.addTransaction(new Transaction(-50.0, "Groceries", "Food",
                    LocalDate.of(2025, 1, 16)));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralFinancialTracker.json");
            writer.open();
            writer.write(tracker);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralFinancialTracker.json");
            tracker = reader.read();
            List<Transaction> transactions = tracker.getTransactions();
            assertEquals(2, transactions.size());
            checkTransaction(1000.0, "Salary", "Income",
                    LocalDate.of(2025, 1, 15), transactions.get(0));
            checkTransaction(-50.0, "Groceries", "Food",
                    LocalDate.of(2025, 1, 16), transactions.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterFinancialTrackerWithSingleTransaction() {
        try {
            FinancialTracker tracker = new FinancialTracker();
            tracker.addTransaction(new Transaction(2500.0, "Freelance work", "Income",
                    LocalDate.of(2025, 7, 18)));
            JsonWriter writer = new JsonWriter("./data/testWriterSingleTransaction.json");
            writer.open();
            writer.write(tracker);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterSingleTransaction.json");
            tracker = reader.read();
            List<Transaction> transactions = tracker.getTransactions();
            assertEquals(1, transactions.size());
            checkTransaction(2500.0, "Freelance work", "Income",
                    LocalDate.of(2025, 7, 18), transactions.get(0));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterFinancialTrackerWithSpecialCharacters() {
        try {
            FinancialTracker tracker = new FinancialTracker();
            tracker.addTransaction(new Transaction(-25.50, "Café & bakery", "Food",
                    LocalDate.of(2025, 7, 18)));
            tracker.addTransaction(new Transaction(100.0, "Cash deposit (ATM #123)", "Banking",
                    LocalDate.of(2025, 7, 18)));
            JsonWriter writer = new JsonWriter("./data/testWriterSpecialChars.json");
            writer.open();
            writer.write(tracker);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterSpecialChars.json");
            tracker = reader.read();
            List<Transaction> transactions = tracker.getTransactions();
            assertEquals(2, transactions.size());
            checkTransaction(-25.50, "Café & bakery", "Food",
                    LocalDate.of(2025, 7, 18), transactions.get(0));
            checkTransaction(100.0, "Cash deposit (ATM #123)", "Banking",
                    LocalDate.of(2025, 7, 18), transactions.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
