package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;

import model.FinancialTracker;
import model.Transaction;
import persistence.JsonReader;
import persistence.JsonWriter;

/**
 * Main GUI window for the Financial Tracker application. This is the base
 * visual layout without functionality.
 */
public class FinancialTrackerGUI extends JFrame {

    private static final int WINDOW_WIDTH = 800;
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

    // Components for transaction display
    private JScrollPane transactionScrollPane;
    private JList<String> transactionList;
    private DefaultListModel<String> listModel;

    // Buttons
    private JButton addTransactionButton;
    private JButton filterByCategoryButton;
    private JButton saveButton;
    private JButton loadButton;

    // Menu
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem addMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;
    private JMenuItem exitMenuItem;

    /**
     * Constructor to create the GUI window
     */
    public FinancialTrackerGUI() {
        tracker = new FinancialTracker();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        initializeGUI();
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

        // Initialize transaction list
        listModel = new DefaultListModel<>();
        transactionList = new JList<>(listModel);
        transactionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionScrollPane = new JScrollPane(transactionList);
        transactionScrollPane.setPreferredSize(new Dimension(750, 400));

        // Load existing data and refresh display
        refreshTransactionDisplay();

        // Initialize buttons
        addTransactionButton = new JButton("Add Transaction");
        filterByCategoryButton = new JButton("Filter by Category");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");

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
        exitMenuItem = new JMenuItem("Exit");

        fileMenu.add(addMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
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

        // Bottom panel with file operations
        bottomPanel.setBorder(BorderFactory.createTitledBorder("File Operations"));
        bottomPanel.add(saveButton);
        bottomPanel.add(loadButton);

        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Add event handlers to buttons and menu items
     */
    private void addEventHandlers() {
        // Add Transaction button handler
        addTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddTransactionDialog();
            }
        });

        // Filter by Category button handler
        filterByCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFilterByCategoryDialog();
            }
        });

        // Save button handler
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSaveDialog();
            }
        });

        // Load button handler
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoadDialog();
            }
        });

        // Menu item handlers
        addMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddTransactionDialog();
            }
        });

        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSaveDialog();
            }
        });

        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoadDialog();
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExitDialog();
            }
        });
    }

    /**
     * Show dialog for adding a new transaction
     */
    private void showAddTransactionDialog() {
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

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Transaction", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String amountText = amountField.getText().trim();
                String description = descriptionField.getText().trim();
                String category = (String) categoryCombo.getSelectedItem();
                String dateText = dateField.getText().trim();

                if (amountText.isEmpty() || description.isEmpty() || dateText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                    return;
                }

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
                System.exit(0);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Unable to save file. Exit anyway?");
                System.exit(0);
            }
        } else if (result == JOptionPane.NO_OPTION) {
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
     * Main method to run the GUI application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FinancialTrackerGUI();
        });
    }
}
