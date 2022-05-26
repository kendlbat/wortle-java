package model;

import java.util.List;

public abstract class ANSI {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static String getColoredString(String color, String string) {
        return color + string + ANSI_RESET;
    }

    public static String getFormattedGuess(String word, List<CharStates> charStates) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (charStates.get(i) == CharStates.CORRECT) {
                sb.append(getColoredString(ANSI_GREEN, word.charAt(i) + ""));
            } else if (charStates.get(i) == CharStates.IN_WORD) {
                sb.append(getColoredString(ANSI_YELLOW, word.charAt(i) + ""));
            } else {
                sb.append(word.charAt(i));
            }
        }
        return sb.toString();
    }

    public static void clearScreen() {
        // Intellij
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
