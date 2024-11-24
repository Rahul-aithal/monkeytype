import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TypingTest app = new TypingTest();
            app.setVisible(true);
        });
    }
}
