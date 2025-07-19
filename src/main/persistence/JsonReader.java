package persistence;

import model.FinancialTracker;
import model.Transaction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.time.LocalDate;

import org.json.*;

// Represents a reader that reads financial tracker from JSON data stored in file
public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads financial tracker from file and returns it;
    // throws IOException if an error occurs reading data from file
    public FinancialTracker read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseFinancialTracker(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses financial tracker from JSON object and returns it
    private FinancialTracker parseFinancialTracker(JSONObject jsonObject) {
        FinancialTracker tracker = new FinancialTracker();
        addTransactions(tracker, jsonObject);
        return tracker;
    }

    // MODIFIES: tracker
    // EFFECTS: parses transactions from JSON object and adds them to financial tracker
    private void addTransactions(FinancialTracker tracker, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("transactions");
        for (Object json : jsonArray) {
            JSONObject nextTransaction = (JSONObject) json;
            addTransaction(tracker, nextTransaction);
        }
    }

    // MODIFIES: tracker
    // EFFECTS: parses transaction from JSON object and adds it to financial tracker
    private void addTransaction(FinancialTracker tracker, JSONObject jsonObject) {
        double amount = jsonObject.getDouble("amount");
        String description = jsonObject.getString("description");
        String category = jsonObject.getString("category");
        String dateString = jsonObject.getString("date");
        LocalDate date = LocalDate.parse(dateString);

        Transaction transaction = new Transaction(amount, description, category, date);
        tracker.addTransaction(transaction);
    }
}
