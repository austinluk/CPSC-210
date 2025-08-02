package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Main GUI window for the Financial Tracker application. This is the base
 * visual layout without functionality.
 */
public class FinancialTrackerGUI extends JFrame {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

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

        // Add some sample data for visual purposes
        listModel.addElement("Sample Transaction 1: +$1000.00 - Salary (2024-01-15)");
        listModel.addElement("Sample Transaction 2: -$50.00 - Food (2024-01-16)");
        listModel.addElement("Sample Transaction 3: -$800.00 - Rent (2024-01-16)");

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
        JTextField dateField = new JTextField("2024-01-17");

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
            String amount = amountField.getText();
            String description = descriptionField.getText();
            String category = (String) categoryCombo.getSelectedItem();
            String date = dateField.getText();

            if (!amount.isEmpty() && !description.isEmpty()) {
                String transaction = String.format("New Transaction: %s%s - %s (%s)",
                        amount.startsWith("-") ? "" : "+$", amount, category, date);
                listModel.addElement(transaction);
                JOptionPane.showMessageDialog(this, "Transaction added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
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
            if (selectedCategory.equals("All")) {
                JOptionPane.showMessageDialog(this, "Showing all transactions");
            } else {
                JOptionPane.showMessageDialog(this, "Filtering by category: " + selectedCategory
                        + "\n(Filter functionality will be implemented in next step)");
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
            JOptionPane.showMessageDialog(this, "Data saved successfully!\n(Save functionality will be implemented in next step)");
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
            JOptionPane.showMessageDialog(this, "Data loaded successfully!\n(Load functionality will be implemented in next step)");
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
            showSaveDialog();
            System.exit(0);
        } else if (result == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
        // If CANCEL, do nothing (stay in application)
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
