package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Main GUI window for the Financial Tracker application.
 * This is the base visual layout without functionality.
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
                JOptionPane.showMessageDialog(FinancialTrackerGUI.this, 
                    "Add Transaction button clicked!");
            }
        });
        
        // Filter by Category button handler
        filterByCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(FinancialTrackerGUI.this, 
                    "Filter by Category button clicked!");
            }
        });
        
        // Save button handler
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(FinancialTrackerGUI.this, 
                    "Save button clicked!");
            }
        });
        
        // Load button handler
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(FinancialTrackerGUI.this, 
                    "Load button clicked!");
            }
        });
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
