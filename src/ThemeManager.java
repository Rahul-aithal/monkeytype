import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    private boolean darkMode = false;

    public void toggleTheme(JFrame frame) {
        darkMode = !darkMode;
        SwingUtilities.invokeLater(() -> {
            if (darkMode) {
                frame.getContentPane().setBackground(Color.DARK_GRAY);
                frame.getContentPane().setForeground(Color.WHITE);
            } else {
                frame.getContentPane().setBackground(Color.WHITE);
                frame.getContentPane().setForeground(Color.BLACK);
            }
            frame.repaint();
        });
    }
}
