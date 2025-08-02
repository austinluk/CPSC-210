package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Very simple loading screen for the Financial Tracker application.
 */
public class LoadingScreen extends JFrame {
    
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    private JProgressBar progressBar;
    private Timer progressTimer;
    private int progress = 0;
    
    public LoadingScreen() {
        initializeScreen();
        showLoadingScreen();
    }
    
    private void initializeScreen() {
        setTitle("Financial Tracker - Loading");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Create simple panel with background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Try to load and draw the background image
                ImageIcon bgImage = new ImageIcon("./data/LoadingScreen.jpg");
                if (bgImage.getIconWidth() > 0) {
                    g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Simple gradient background if image not found
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gradient = new GradientPaint(0, 0, new Color(200, 220, 100), 
                                                             getWidth(), getHeight(), new Color(255, 255, 150));
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        panel.setLayout(new BorderLayout());
        
        // Simple title
        JLabel title = new JLabel("Financial Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        title.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);
        
        // Create progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Loading...");
        progressBar.setFont(new Font("Arial", Font.PLAIN, 14));
        progressBar.setPreferredSize(new Dimension(300, 25));
        progressBar.setOpaque(false);
        
        // Bottom panel for progress bar
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        bottomPanel.add(progressBar);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(panel);
    }
    
    private void showLoadingScreen() {
        setVisible(true);
        
        // Progress bar animation timer
        progressTimer = new Timer(30, e -> {
            progress += 2;
            progressBar.setValue(progress);
            progressBar.setString("Loading... " + progress + "%");
            
            if (progress >= 100) {
                progressTimer.stop();
                // Close loading screen and open main app
                Timer closeTimer = new Timer(500, closeEvent -> {
                    setVisible(false);
                    dispose();
                    new FinancialTrackerGUI();
                });
                closeTimer.setRepeats(false);
                closeTimer.start();
            }
        });
        progressTimer.start();
    }
}
