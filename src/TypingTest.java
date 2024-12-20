import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class TypingTest extends JFrame {
    private JTextPane wordDisplay;
    private JTextField inputField;
    private JLabel timerLabel, wpmLabel, accuracyLabel;
    private StatsManager statsManager;
    private RandomWordGenerator wordGenerator;
    private Timer timer;
    private int timeLeft = 60;
    private boolean testRunning = false;

    public TypingTest() {
        setupFrame();
        initializeComponents();
        createLayout();
        setupMenuBar();
        restartTest();
    }

    private void setupFrame() {
        setTitle("Typing Speed Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(true);
    }

    private void initializeComponents() {
        statsManager = new StatsManager();
        wordGenerator = new RandomWordGenerator();
        String initialWords = wordGenerator.generateWords(50);
        statsManager.processInput("", initialWords);

        setupWordDisplay();
        setupInputField();
    }

    private void setupWordDisplay() {
        wordDisplay = new JTextPane();
        wordDisplay.setContentType("text/html");
        wordDisplay.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        wordDisplay.setEditable(false);
        wordDisplay.setFont(new Font("Consolas", Font.PLAIN, 20));
        wordDisplay.setBackground(Color.decode("#121212"));
        wordDisplay.setBorder(BorderFactory.createLineBorder(Color.decode("#2c2c2c")));
        wordDisplay.setFocusable(false);
        wordDisplay.setMargin(new Insets(10, 10, 10, 10));
    }

    private void setupInputField() {
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
                if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                        && !inputField.getText().trim().isEmpty()) {
                    String userInput = inputField.getText().trim();
                    statsManager.processInput(userInput, wordGenerator.getWords());
                    inputField.setText("");
                    updateWordHighlight();
                    updateStats();
                }
            }
        });
    }

    private void createLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#121212"));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(wordDisplay);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#2c2c2c")));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel statsPanel = createStatsPanel();
        mainPanel.add(statsPanel, BorderLayout.NORTH);

        add(mainPanel);
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(Color.decode("#121212"));
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(Color.decode("#2c2c2c"));
        resetButton.setForeground(Color.decode("#e0e0e0"));
        resetButton.setFont(new Font("Consolas", Font.BOLD, 16));
        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createLineBorder(Color.decode("#2c2c2c")));
        resetButton.addActionListener(e -> restartTest());

        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(resetButton, BorderLayout.EAST);
        return bottomPanel;
    }

    private JPanel createStatsPanel() {
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

        return statsPanel;
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.decode("#1e1e1e"));

        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.setForeground(Color.decode("#e0e0e0"));

        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(e -> restartTest());
        optionsMenu.add(restartItem);

        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
    }

    private void startTest() {
        testRunning = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    timeLeft--;
                    timerLabel.setText("Time: " + timeLeft + "s");
                    if (timeLeft <= 0) {
                        endTest();
                    }
                });
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
        updateWordHighlight();
        updateStats();
    }

    private void updateStats() {
        wpmLabel.setText("WPM: " + statsManager.getWPM());
        accuracyLabel.setText("Accuracy: " + statsManager.getAccuracy() + "%");
    }

    private void updateWordHighlight() {
        String highlightedText = statsManager.getHighlightedWords();
        wordDisplay.setText("<html><body style='font-family: Consolas; font-size: 20pt; background-color: #121212; color: #e0e0e0;'>" + highlightedText + "</body></html>");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TypingTest test = new TypingTest();
            test.setVisible(true);
        });
    }
}
