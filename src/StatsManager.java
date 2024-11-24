public class StatsManager {
    private int correctWords = 0;
    private int totalWords = 0;
    private String currentWords = "";

    public void processInput(String input, String allWords) {
        String[] words = allWords.split("\\s+");
        totalWords++;
        if (totalWords <= words.length && input.equals(words[totalWords - 1])) {
            correctWords++;
        }
        currentWords = allWords;
    }

    public int getWPM() {
        return correctWords; // Simplified WPM logic
    }

    public int getAccuracy() {
        return totalWords == 0 ? 100 : (correctWords * 100) / totalWords;
    }

    public int getWordsTyped() {
        return totalWords; // Total words typed
    }

    public void resetStats() {
        correctWords = 0;
        totalWords = 0;
    }

    public String getHighlightedWords() {
        return currentWords;
    }
}
