import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonkeyTypeClone extends JFrame {
    private JTextArea wordDisplay;
    private JTextField inputField;
    private JLabel statsLabel;
    private JButton restartButton;
    private Timer timer;
    private int timeLeft = 60; // Example timer

    public MonkeyTypeClone() {
        setTitle("MonkeyType Clone");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel header = new JPanel();
        header.setBackground(Color.DARK_GRAY);
        header.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("MonkeyType");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Typing Panel
        JPanel typingPanel = new JPanel(new BorderLayout());
        wordDisplay = new JTextArea("randomized words will appear here...");
        wordDisplay.setFont(new Font("Sans-serif", Font.PLAIN, 18));
        wordDisplay.setEditable(false);
        wordDisplay.setLineWrap(true);
        typingPanel.add(new JScrollPane(wordDisplay), BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setFont(new Font("Sans-serif", Font.PLAIN, 18));
        typingPanel.add(inputField, BorderLayout.SOUTH);
        add(typingPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footer = new JPanel();
        footer.setLayout(new FlowLayout(FlowLayout.CENTER));
        statsLabel = new JLabel("WPM: 0 | Accuracy: 100% | Time Left: 60s");
        footer.add(statsLabel);

        restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartTest());
        footer.add(restartButton);

        add(footer, BorderLayout.SOUTH);

        // Timer for updates
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                statsLabel.setText("WPM: 0 | Accuracy: 100% | Time Left: " + timeLeft + "s");
                if (timeLeft <= 0) {
                    timer.stop();
                    inputField.setEditable(false);
                    statsLabel.setText("Time's up! Press Restart.");
                }
            }
        });

        setVisible(true);
        startTest();
    }

    private void startTest() {
        inputField.setEditable(true);
        inputField.requestFocus();
        timeLeft = 60;
        timer.start();
    }

    private void restartTest() {
        wordDisplay.setText("randomized words will appear here...");
        inputField.setText("");
        startTest();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MonkeyTypeClone::new);
    }
}
