package ui;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import model.Event;
import model.EventLog;
import model.FinancialTracker;
import model.Transaction;
import persistence.JsonReader;
import persistence.JsonWriter;

/**
 * Main GUI window for the Financial Tracker application. This is the base
 * visual layout without functionality.
 */
public class FinancialTrackerGUI extends JFrame {

    private static final int WINDOW_WIDTH = 1050;
    private static final int WINDOW_HEIGHT = 600;
    private static final String JSON_STORE = "./data/FinancialHistory.json";

    // Data management
    private FinancialTracker tracker;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // Main panels
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JPanel rightPanel;

    // Components for transaction display
    private JScrollPane transactionScrollPane;
    private JList<String> transactionList;
    private DefaultListModel<String> listModel;

    // Visual component - Financial Summary Panel
    private FinancialSummaryPanel summaryPanel;

    // Buttons
    private JButton addTransactionButton;
    private JButton filterByCategoryButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton viewEventLogButton;

    // Menu
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem addMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;
    private JMenuItem viewEventLogMenuItem;
    private JMenuItem exitMenuItem;

    /**
     * Constructor to create the GUI window
     */
    public FinancialTrackerGUI() {
        tracker = new FinancialTracker();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        // Add some default transactions Test
        // addDefaultTransactions();
        initializeGUI();
    }

    /**
     * Add default transactions for demonstration purposes
     */
    private void addDefaultTransactions() {
        try {
            // Add some sample income transactions
            tracker.addTransaction(new Transaction(2500.00, "Monthly Salary", "Salary", LocalDate.now().minusDays(5)));
            tracker.addTransaction(new Transaction(500.00, "Freelance Work", "Salary", LocalDate.now().minusDays(10)));

            // Add some sample expense transactions
            tracker.addTransaction(new Transaction(-800.00, "Monthly Rent", "Rent", LocalDate.now().minusDays(3)));
            tracker.addTransaction(new Transaction(-150.00, "Groceries", "Food", LocalDate.now().minusDays(2)));
            tracker.addTransaction(new Transaction(-60.00, "Gas Bill", "Other", LocalDate.now().minusDays(1)));
            tracker.addTransaction(new Transaction(-45.00, "Restaurant Dinner", "Food", LocalDate.now()));
        } catch (Exception e) {
            // If there's any issue with default transactions, just continue
            System.out.println("Note: Could not add default transactions");
        }
    }

    /**
     * Initialize the main GUI window and components
     */
    private void initializeGUI() {
        setTitle("Financial Tracker");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        initializeComponents();
        layoutComponents();
        addEventHandlers();

        // Center the window on screen
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initialize all GUI components
     */
    private void initializeComponents() {
        // Initialize panels
        mainPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel(new FlowLayout());
        centerPanel = new JPanel(new BorderLayout());
        bottomPanel = new JPanel(new FlowLayout());
        rightPanel = new JPanel(new BorderLayout());

        // Initialize transaction list
        listModel = new DefaultListModel<>();
        transactionList = new JList<>(listModel);
        transactionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionScrollPane = new JScrollPane(transactionList);
        transactionScrollPane.setPreferredSize(new Dimension(500, 400));

        // Initialize visual component - Financial Summary Panel
        summaryPanel = new FinancialSummaryPanel();

        // Load existing data and refresh display
        refreshTransactionDisplay();

        // Initialize buttons
        addTransactionButton = new JButton("Add Transaction");
        filterByCategoryButton = new JButton("Filter by Category");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        viewEventLogButton = new JButton("View Event Log");

        // Initialize menu
        createMenuBar();
    }

    /**
     * Create the menu bar and menu items
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");

        addMenuItem = new JMenuItem("Add Transaction");
        saveMenuItem = new JMenuItem("Save Financial Data");
        loadMenuItem = new JMenuItem("Load Financial Data");
        viewEventLogMenuItem = new JMenuItem("View Event Log");
        exitMenuItem = new JMenuItem("Exit");

        fileMenu.add(addMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(viewEventLogMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Layout all components in the window
     */
    private void layoutComponents() {
        // Top panel with action buttons
        topPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        topPanel.add(addTransactionButton);
        topPanel.add(filterByCategoryButton);

        // Center panel with transaction list
        centerPanel.setBorder(BorderFactory.createTitledBorder("Transaction History"));
        centerPanel.add(transactionScrollPane, BorderLayout.CENTER);

        // Right panel with visual component
        rightPanel.add(summaryPanel, BorderLayout.CENTER);

        // Bottom panel with file operations
        bottomPanel.setBorder(BorderFactory.createTitledBorder("File Operations"));
        bottomPanel.add(saveButton);
        bottomPanel.add(loadButton);
        bottomPanel.add(viewEventLogButton);

        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Add event handlers to buttons and menu items
     */
    private void addEventHandlers() {
        addButtonHandlers();
        addMenuHandlers();
    }

    /**
     * Add event handlers to buttons
     */
    private void addButtonHandlers() {
        addTransactionButton.addActionListener(e -> showAddTransactionDialog());
        filterByCategoryButton.addActionListener(e -> showFilterByCategoryDialog());
        saveButton.addActionListener(e -> showSaveDialog());
        loadButton.addActionListener(e -> showLoadDialog());
        viewEventLogButton.addActionListener(e -> showEventLogDialog());
    }

    /**
     * Add event handlers to menu items
     */
    private void addMenuHandlers() {
        addMenuItem.addActionListener(e -> showAddTransactionDialog());
        saveMenuItem.addActionListener(e -> showSaveDialog());
        loadMenuItem.addActionListener(e -> showLoadDialog());
        viewEventLogMenuItem.addActionListener(e -> showEventLogDialog());
        exitMenuItem.addActionListener(e -> showExitDialog());
    }

    /**
     * Show dialog for adding a new transaction
     */
    private void showAddTransactionDialog() {
        JPanel panel = createTransactionInputPanel();
        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Transaction", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            processTransactionInput(panel);
        }
    }

    /**
     * Create input panel for transaction dialog
     */
    private JPanel createTransactionInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField amountField = new JTextField();
        JTextField descriptionField = new JTextField();
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "Food", "Rent", "Salary", "Entertainment", "Transportation", "Other"
        });
        JTextField dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        panel.add(new JLabel("Amount ($):"));
        panel.add(amountField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryCombo);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);

        return panel;
    }

    /**
     * Process input from transaction dialog
     */
    private void processTransactionInput(JPanel panel) {
        try {
            Component[] components = panel.getComponents();
            JTextField amountField = (JTextField) components[1];
            JTextField descriptionField = (JTextField) components[3];
            JComboBox<String> categoryCombo = (JComboBox<String>) components[5];
            JTextField dateField = (JTextField) components[7];

            String amountText = amountField.getText().trim();
            String description = descriptionField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String dateText = dateField.getText().trim();

            if (validateTransactionInput(amountText, description, dateText)) {
                createAndAddTransaction(amountText, description, category, dateText);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing transaction input.");
        }
    }

    /**
     * Validate transaction input fields
     */
    private boolean validateTransactionInput(String amount, String description, String date) {
        if (amount.isEmpty() || description.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return false;
        }
        return true;
    }

    /**
     * Create and add transaction to tracker
     */
    private void createAndAddTransaction(String amountText, String description, String category, String dateText) {
        try {
            double amount = Double.parseDouble(amountText);
            LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Transaction transaction = new Transaction(amount, description, category, date);
            tracker.addTransaction(transaction);
            refreshTransactionDisplay();

            JOptionPane.showMessageDialog(this, "Transaction added successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount (number).");
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in YYYY-MM-DD format.");
        }
    }

    /**
     * Show dialog for filtering transactions by category
     */
    private void showFilterByCategoryDialog() {
        String[] categories = {"All", "Food", "Rent", "Salary", "Entertainment", "Transportation", "Other"};
        String selectedCategory = (String) JOptionPane.showInputDialog(this,
                "Select category to filter by:", "Filter Transactions",
                JOptionPane.QUESTION_MESSAGE, null, categories, categories[0]);

        if (selectedCategory != null) {
            listModel.clear();

            if (selectedCategory.equals("All")) {
                refreshTransactionDisplay();
            } else {
                List<Transaction> filteredTransactions = tracker.getTransactionsByCategory(selectedCategory);

                if (filteredTransactions.isEmpty()) {
                    listModel.addElement("No transactions found for category: " + selectedCategory);
                } else {
                    for (Transaction transaction : filteredTransactions) {
                        String displayText = formatTransactionForDisplay(transaction);
                        listModel.addElement(displayText);
                    }
                }
            }
        }
    }

    /**
     * Show save confirmation dialog
     */
    private void showSaveDialog() {
        int result = JOptionPane.showConfirmDialog(this,
                "Save current financial data to file?", "Save Data",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try {
                jsonWriter.open();
                jsonWriter.write(tracker);
                jsonWriter.close();
                JOptionPane.showMessageDialog(this, "Data saved successfully!");
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Unable to save file: " + JSON_STORE);
            }
        }
    }

    /**
     * Show load confirmation dialog
     */
    private void showLoadDialog() {
        int result = JOptionPane.showConfirmDialog(this,
                "Load financial data from file? This will replace current data.", "Load Data",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try {
                tracker = jsonReader.read();
                refreshTransactionDisplay();
                JOptionPane.showMessageDialog(this, "Data loaded successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Unable to read from file: " + JSON_STORE);
            }
        }
    }

    /**
     * Show exit confirmation dialog with save option
     */
    private void showExitDialog() {
        int result = JOptionPane.showConfirmDialog(this,
                "Do you want to save before exiting?", "Exit Application",
                JOptionPane.YES_NO_CANCEL_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try {
                jsonWriter.open();
                jsonWriter.write(tracker);
                jsonWriter.close();
                JOptionPane.showMessageDialog(this, "Data saved successfully!");
                printEventLogToConsole();
                System.exit(0);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Unable to save file. Exit anyway?");
                printEventLogToConsole();
                System.exit(0);
            }
        } else if (result == JOptionPane.NO_OPTION) {
            printEventLogToConsole();
            System.exit(0);
        }
        // If CANCEL, do nothing (stay in application)
    }

    /**
     * Refresh the transaction display with current tracker data
     */
    private void refreshTransactionDisplay() {
        listModel.clear();
        List<Transaction> transactions = tracker.getTransactions();

        if (transactions.isEmpty()) {
            listModel.addElement("No transactions yet. Add some transactions to get started!");
        } else {
            for (Transaction transaction : transactions) {
                String displayText = formatTransactionForDisplay(transaction);
                listModel.addElement(displayText);
            }
        }

        // Refresh the visual component
        summaryPanel.repaint();
    }

    /**
     * Format a transaction for display in the list
     */
    private String formatTransactionForDisplay(Transaction transaction) {
        String sign = transaction.getAmount() >= 0 ? "+" : "";
        return String.format("%s$%.2f - %s (%s) [%s]",
                sign, transaction.getAmount(), transaction.getCategory(),
                transaction.getDate(), transaction.getDescription());
    }

    /**
     * Custom panel that displays financial summary with visual chart This
     * satisfies the GUI visual component requirement
     */
    private class FinancialSummaryPanel extends JPanel {

        private static final int PANEL_WIDTH = 250;
        private static final int PANEL_HEIGHT = 300;

        public FinancialSummaryPanel() {
            setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
            setBorder(BorderFactory.createTitledBorder("Financial Summary"));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (tracker == null) {
                return;
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawFinancialSummary(g2d);
            drawBarChart(g2d);
            drawTransactionCount(g2d);
        }

        /**
         * Draw financial summary text
         */
        private void drawFinancialSummary(Graphics2D g2d) {
            double[] amounts = calculateFinancialAmounts();
            double totalIncome = amounts[0];
            double totalExpenses = amounts[1];
            double balance = amounts[2];

            // Draw title
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.setColor(Color.BLACK);
            g2d.drawString("Financial Overview", 10, 30);

            // Draw text summary
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(String.format("Total Income: $%.2f", totalIncome), 10, 50);
            g2d.drawString(String.format("Total Expenses: $%.2f", totalExpenses), 10, 70);
            g2d.setColor(balance >= 0 ? Color.GREEN : Color.RED);
            g2d.drawString(String.format("Balance: $%.2f", balance), 10, 90);
        }

        /**
         * Calculate financial amounts
         */
        private double[] calculateFinancialAmounts() {
            double totalIncome = tracker.getTotalIncome();
            double totalExpenses = 0.0;
            for (Transaction t : tracker.getTransactions()) {
                if (t.getAmount() < 0) {
                    totalExpenses += Math.abs(t.getAmount());
                }
            }
            double balance = totalIncome - totalExpenses;
            return new double[]{totalIncome, totalExpenses, balance};
        }

        /**
         * Draw bar chart for income and expenses
         */
        private void drawBarChart(Graphics2D g2d) {
            double[] amounts = calculateFinancialAmounts();
            double totalIncome = amounts[0];
            double totalExpenses = amounts[1];

            int chartY = 110;
            int barHeight = 25;
            int barSpacing = 45;
            int maxBarWidth = 200;
            double maxAmount = Math.max(totalIncome, totalExpenses);

            if (maxAmount == 0) {
                maxAmount = 100;
            }

            drawIncomeBar(g2d, totalIncome, maxAmount, chartY, barHeight, maxBarWidth);
            drawExpenseBar(g2d, totalExpenses, maxAmount, chartY + barSpacing, barHeight, maxBarWidth);
        }

        /**
         * Draw income bar
         */
        private void drawIncomeBar(Graphics2D g2d, double income, double maxAmount, int y, int height, int maxWidth) {
            g2d.setColor(Color.GREEN);
            int incomeWidth = maxAmount > 0 ? (int) ((income / maxAmount) * maxWidth) : 0;
            g2d.fillRect(10, y, incomeWidth, height);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(10, y, maxWidth, height);
            g2d.drawString("Income", 10, y - 5);
        }

        /**
         * Draw expense bar
         */
        private void drawExpenseBar(Graphics2D g2d, double expenses, double maxAmount,
                int y, int height, int maxWidth) {
            g2d.setColor(Color.RED);
            int expenseWidth = maxAmount > 0 ? (int) ((expenses / maxAmount) * maxWidth) : 0;
            g2d.fillRect(10, y, expenseWidth, height);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(10, y, maxWidth, height);
            g2d.drawString("Expenses", 10, y - 5);
        }

        /**
         * Draw transaction count
         */
        private void drawTransactionCount(Graphics2D g2d) {
            g2d.setColor(Color.BLUE);
            g2d.drawString("Total Transactions: " + tracker.getTransactionCount(), 10, 210);
        }
    }

    /**
     * Show dialog displaying the event log
     */
    private void showEventLogDialog() {
        StringBuilder eventText = new StringBuilder();
        eventText.append("Event Log:\n");
        eventText.append("----------\n\n");

        boolean hasEvents = false;
        for (Event event : EventLog.getInstance()) {
            eventText.append(event.toString()).append("\n\n");
            hasEvents = true;
        }

        if (!hasEvents) {
            eventText.append("No events logged yet.");
        }

        JTextArea textArea = new JTextArea(eventText.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton clearButton = new JButton("Clear Event Log");
        clearButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to clear the event log?",
                    "Clear Event Log",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                EventLog.getInstance().clear();
                JOptionPane.showMessageDialog(this, "Event log cleared!");
                // Close the current dialog and reopen it to show the cleared log
                SwingUtilities.getWindowAncestor(clearButton).dispose();
                showEventLogDialog();
            }
        });
        buttonPanel.add(clearButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Financial Tracker Event Log",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Print the event log to console
     */
    private void printEventLogToConsole() {
        System.out.println("\nEvent Log:");
        System.out.println("----------");
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.toString());
            System.out.println();
        }
    }

    /**
     * Main method to run the GUI application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FinancialTrackerGUI();
        });
    }
}
