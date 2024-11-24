import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TypingTest extends JFrame {
    private JTextPane wordDisplay;
    private JTextField inputField;
    private JLabel timerLabel, wpmLabel, accuracyLabel;
    private StatsManager statsManager;
    private RandomWordGenerator wordGenerator;
    private Timer timer;
    private int timeLeft = 60; // 60 seconds for the test
    private boolean testRunning = false;

    public TypingTest() {
        setTitle("MonkeyType Clone");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Initialize components
        statsManager = new StatsManager();
        wordGenerator = new RandomWordGenerator();
        String initialWords = wordGenerator.generateWords(50);
        statsManager.processInput("", initialWords);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#121212"));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Word display
        wordDisplay = new JTextPane();
        wordDisplay.setText(initialWords);
        wordDisplay.setEditable(false);
        wordDisplay.setFont(new Font("Consolas", Font.PLAIN, 20));
        wordDisplay.setBackground(Color.decode("#121212"));
        wordDisplay.setForeground(Color.decode("#e0e0e0"));
        wordDisplay.setBorder(BorderFactory.createLineBorder(Color.decode("#2c2c2c")));
        wordDisplay.setFocusable(false);
        wordDisplay.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(wordDisplay);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Input field
        inputField = new JTextField();
        inputField.setFont(new Font("Consolas", Font.PLAIN, 20));
        inputField.setBackground(Color.decode("#1e1e1e"));
        inputField.setForeground(Color.decode("#e0e0e0"));
        inputField.setCaretColor(Color.decode("#e0e0e0"));
        inputField.setBorder(BorderFactory.createLineBorder(Color.decode("#2c2c2c")));
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!testRunning) {
                    startTest();
                }

                // Process input on SPACE or ENTER
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String userInput = inputField.getText().trim();
                    statsManager.processInput(userInput, wordGenerator.getWords());
                    inputField.setText(""); // Clear input
                    updateWordHighlight();
                    updateStats();
                }
            }
        });

        // Reset Button
        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(Color.decode("#2c2c2c"));
        resetButton.setForeground(Color.decode("#e0e0e0"));
        resetButton.setFont(new Font("Consolas", Font.BOLD, 16));
        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createLineBorder(Color.decode("#2c2c2c")));
        resetButton.addActionListener(e -> restartTest());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.decode("#121212"));
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(resetButton, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Stats and timer panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3));
        statsPanel.setBackground(Color.decode("#121212"));

        timerLabel = new JLabel("Time: 60s", JLabel.CENTER);
        timerLabel.setForeground(Color.decode("#e0e0e0"));
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        statsPanel.add(timerLabel);

        wpmLabel = new JLabel("WPM: 0", JLabel.CENTER);
        wpmLabel.setForeground(Color.decode("#e0e0e0"));
        wpmLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        statsPanel.add(wpmLabel);

        accuracyLabel = new JLabel("Accuracy: 100%", JLabel.CENTER);
        accuracyLabel.setForeground(Color.decode("#e0e0e0"));
        accuracyLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        statsPanel.add(accuracyLabel);

        mainPanel.add(statsPanel, BorderLayout.NORTH);

        // Add main panel
        add(mainPanel);

        // Menu bar for restart
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.decode("#1e1e1e"));
        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.setForeground(Color.decode("#e0e0e0"));

        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(e -> restartTest());
        optionsMenu.add(restartItem);

        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);

        restartTest();
    }

    private void startTest() {
        testRunning = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeLeft--;
                timerLabel.setText("Time: " + timeLeft + "s");
                if (timeLeft <= 0) {
                    endTest();
                }
            }
        }, 1000, 1000);
    }

    private void endTest() {
        if (timer != null) timer.cancel();
        testRunning = false;
        inputField.setEditable(false);
        showResults();
    }

    private void showResults() {
        String results = "<html><center>" +
                "<h1>Test Complete!</h1>" +
                "<p><strong>WPM:</strong> " + statsManager.getWPM() + "</p>" +
                "<p><strong>Accuracy:</strong> " + statsManager.getAccuracy() + "%</p>" +
                "<p><strong>Total Words:</strong> " + statsManager.getWordsTyped() + "</p>" +
                "</center></html>";

        JOptionPane.showMessageDialog(this, results, "Results", JOptionPane.INFORMATION_MESSAGE);
    }

    private void restartTest() {
        if (timer != null) timer.cancel();
        timeLeft = 60;
        testRunning = false;
        inputField.setEditable(true);
        inputField.setText("");
        statsManager.resetStats();
        String generatedWords = wordGenerator.generateWords(50);
        statsManager.processInput("", generatedWords);
        wordDisplay.setText(statsManager.getHighlightedWords());
        updateStats();
    }

    private void updateStats() {
        wpmLabel.setText("WPM: " + statsManager.getWPM());
        accuracyLabel.setText("Accuracy: " + statsManager.getAccuracy() + "%");
    }

    private void updateWordHighlight() {
        wordDisplay.setText(statsManager.getHighlightedWords());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TypingTest test = new TypingTest();
            test.setVisible(true);
        });
    }
}
