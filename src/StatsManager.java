public class StatsManager {
    private int correctWords = 0;
    private int totalWords = 0;
    private String[] currentWords;
    private boolean[] wordStatus;
    private long startTime = -1;

    public void processInput(String input, String allWords) {
        if (currentWords == null || !String.join(" ", currentWords).equals(allWords)) {
            currentWords = allWords.split("\\s+");
            wordStatus = new boolean[currentWords.length];
            totalWords = 0;  // Reset totalWords when new words are set
            correctWords = 0;  // Reset correctWords when new words are set
        }
        
        // Don't process empty input on initialization
        if (!input.isEmpty() && totalWords < currentWords.length) {
            wordStatus[totalWords] = input.equals(currentWords[totalWords]);
            if (wordStatus[totalWords]) {
                correctWords++;
            }
            totalWords++;
        }
    }
    

    public int getWPM() {
        if (startTime == -1 || totalWords == 0) {
            return 0;
        }
        // Calculate minutes elapsed since start
        double minutes = (System.currentTimeMillis() - startTime) / 60000.0;
        // WPM = (correct words) / minutes
        return (int) (correctWords / minutes);
    }
    
    public int getAccuracy() {
        return totalWords == 0 ? 100 : (correctWords * 100) / totalWords;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    

    public int getWordsTyped() {
        return totalWords;
    }

    public void resetStats() {
        correctWords = 0;
        totalWords = 0;
        currentWords = null;
        wordStatus = null;
        startTime = -1;  // Reset start time
    }

    public String getHighlightedWords() {
        StringBuilder highlighted = new StringBuilder();
        for (int i = 0; i < currentWords.length; i++) {
            if (i < totalWords) {
                if (wordStatus[i]) {
                    highlighted.append("<span style='color: #4caf50;'>").append(currentWords[i]).append("</span> ");
                } else {
                    highlighted.append("<span style='color: #f44336;'>").append(currentWords[i]).append("</span> ");
                }
            } else if (i == totalWords) {
                // Highlight current word
                highlighted.append("<span style='background-color: #2c2c2c;'>").append(currentWords[i]).append("</span> ");
            } else {
                highlighted.append(currentWords[i]).append(" ");
            }
        }
        return highlighted.toString().trim();
    }
    
}

