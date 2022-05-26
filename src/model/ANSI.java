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

    /**
     * Converts a normal string to an ANSI-Colored string
     * @param color The ANSI-Color code to use
     *              Color codes are saved as constants in the class
     * @param string The string to be colored
     * @return The colored string (with RESET appended)
     * */
    public static String getColoredString(String color, String string) {
        return color + string + ANSI_RESET;
    }

    /**
     * Formats a guess in the default colors (red / yellow)
     * @param word The word to be formatted
     * @param charStates the state each character is in (like IN_WORD or WRONG)
     * @return The word, colored using ANSI-Escape codes
     * */
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

    /**
     * Tries to clear the screen using ANSI-Escape codes
     * If using escape-codes fails, 100 blank lines are printed instead
     *
     * This works in IntelliJ IDEA & Linux Terminal - Has not been tested on Windows yet
     * */
    public static void clearScreen() {
        // TODO: Test on Windows
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
