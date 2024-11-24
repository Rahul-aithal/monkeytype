import java.util.Random;

public class RandomWordGenerator {
    private static final String[] WORDS = {"java", "swing", "monkey", "type", "dark", "theme", "modern", "ui", "ux", "test"};
    private String generatedWords = "";

    public String generateWords(int count) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            sb.append(WORDS[random.nextInt(WORDS.length)]).append(" ");
        }
        generatedWords = sb.toString().trim();
        return generatedWords;
    }

    public String getWords() {
        return generatedWords;
    }
}
