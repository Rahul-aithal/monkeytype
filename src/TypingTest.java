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
        setLayout(new CardLayout());
        setResizable(false);

        JPanel menuPanel = createMenuPanel();
        JPanel testPanel = createTestPanel();

        // Add panels to the CardLayout
        getContentPane().add(menuPanel, "Menu");
        getContentPane().add(testPanel, "Test");

        // Start with the menu panel
        CardLayout cardLayout = (CardLayout) getContentPane().getLayout();
        cardLayout.show(getContentPane(), "Menu");
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(Color.decode("#121212"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Typing Speed Test", JLabel.CENTER);
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 32));
        titleLabel.setForeground(Color.decode("#e0e0e0"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        menuPanel.add(titleLabel, gbc);

        JButton startButton = new JButton("Start Test");
        styleButton(startButton);
        startButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) getContentPane().getLayout();
            cardLayout.show(getContentPane(), "Test");
            restartTest();
        });
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        menuPanel.add(startButton, gbc);

        JButton instructionsButton = new JButton("Instructions");
        styleButton(instructionsButton);
        instructionsButton.addActionListener(e -> showInstructions());
        gbc.gridy = 2;
        menuPanel.add(instructionsButton, gbc);

        JButton exitButton = new JButton("Exit");
        styleButton(exitButton);
        exitButton.addActionListener(e -> System.exit(0));
        gbc.gridy = 3;
        menuPanel.add(exitButton, gbc);

        return menuPanel;
    }

    private JPanel createTestPanel() {
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

        return mainPanel;
    }

    private void styleButton(JButton button) {
        button.setBackground(Color.decode("#2c2c2c"));
        button.setForeground(Color.decode("#e0e0e0"));
        button.setFont(new Font("Consolas", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.decode("#2c2c2c")));
    }

    private void showInstructions() {
        String instructions = "<html><center><h2>Instructions</h2>"
                + "<p>1. Type the words displayed as fast as you can.</p>"
                + "<p>2. Press SPACE or ENTER after each word.</p>"
                + "<p>3. Your WPM and accuracy will be calculated.</p>"
                + "<p>4. The test lasts 60 seconds.</p></center></html>";
        JOptionPane.showMessageDialog(this, instructions, "Instructions", JOptionPane.INFORMATION_MESSAGE);
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
