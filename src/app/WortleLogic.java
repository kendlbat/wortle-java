package app;

import dal.FileHandler;
import model.ANSI;
import model.CharStates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WortleLogic {
    public List<String> wordList;

    public WortleLogic(String wordListFile) {
        wordList = FileHandler.readFileLines(wordListFile);
    }

    /**
     * Get a random word from the word list.
     * @return a random word from the internal <i>wordList</i>
     * */
    public String getRandomWord() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(wordList.size());
        return wordList.get(randomIndex);
    }
    /**
     * Check a words letters against the actual word
     * @param actualWord The reference word to be checked against
     * @param input The word to be checked
     * @return The states <i>(colors)</i> of the characters
     */
    public List<CharStates> checkLetters(String actualWord, String input) {
        List<CharStates> charStates = new ArrayList<>(input.length());
        List<String> charPool = new ArrayList<>(actualWord.length());

        for (int i = 0; i < actualWord.length(); i++) {
            charPool.add(actualWord.charAt(i) + "");
        }

        for (int i = 0; i < input.length(); i++) {
            charStates.add(CharStates.WRONG);
            if (actualWord.charAt(i) == input.charAt(i)) {
                charStates.set(i, CharStates.CORRECT);
                charPool.remove(input.charAt(i) + "");
            }
        }

        for (int i = 0; i < input.length(); i++) {
            if (charPool.contains(input.charAt(i) + "")) {
                charStates.set(i, CharStates.IN_WORD);
                charPool.remove(input.charAt(i) + "");
            }
        }

        return charStates;
    }

    /**
     * Checks if a word is in the wordList
     * @param word The word to be checked
     * @return whether the word is contained inside the list.
     * */
    public boolean checkWord(String word) {
        return wordList.contains(word);
    }

    /**
     * Checks the amount of words from wordlist match with the given regex
     * @param regex The regex to be used
     * @return The amount of words matched
     */
    public int getMatchAmount(String regex) {
        int matchAmount = 0;
        for (String word : wordList) {
            if (word.matches(regex)) {
                matchAmount++;
            }
        }
        return matchAmount;
    }

    /**
     * Gets the amount of words currently loaded
     * @return amount of words in wordList
     */
    public int getWordAmount() {
        return wordList.size();
    }

    /**
     * Generates a keyboard for visual output to the user.
     * @param guessedWrong The list of characters that were already guessed, but not in the word
     * @return A formatted keyboard as string
     * */
    public static String generateKeyboard(List<Character> guessedWrong) {
        String order = "q w e r t z u i o p\n a s d f g h j k l\n  y x c v b n m";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < order.length(); i++) {
            if (guessedWrong.contains(order.charAt(i))) {
                sb.append(ANSI.getColoredString(ANSI.ANSI_BLACK, order.charAt(i) + ""));
            } else {
                sb.append(order.charAt(i));
            }
        }

        return sb.toString();
    }

    /**
     * Generates a regex based on facts already known to the user
     * @param guessedWrong A list of all characters that were already wrongly guessed
     * @param guessedWrongAtPos A list of the positions characters were wrongly guessed at
     * @param guessedRight A string with the currently known letters
     *                     Example: .a.se
     * @return A valid regex for the given conditions
     */
    public static String generateRegex(List<Character> guessedWrong, List<List<Character>> guessedWrongAtPos, String guessedRight) {
        StringBuilder sb = new StringBuilder();

        // Check for only lowercase letters (lookahead)
        sb.append("(?=^[a-zäöüß]+$)");

        List<Character> haveToBeIncluded = new ArrayList<>();
        for (List<Character> list : guessedWrongAtPos) {
            for (Character c : list) {
                if (!haveToBeIncluded.contains(c)) {
                    haveToBeIncluded.add(c);
                }
            }
        }

        if (guessedWrong.size() != 0) {
            sb.append("(?=^[^");
            for (Character gw : guessedWrong) {
                if (!haveToBeIncluded.contains(gw) && !guessedRight.contains(gw + "")) {
                    sb.append(gw);
                }
            }
            sb.append("]+$)");
        }

        for (Character c : haveToBeIncluded) {
            sb.append(String.format("(?=.*%c)", c));
        }

        for (int i = 0; i < guessedWrongAtPos.size(); i++) {
            if (guessedRight.charAt(i) == '.' && guessedWrongAtPos.get(i).size() != 0) {
                sb.append(String.format("(?=.{%d}[^", i));
                for (Character c : guessedWrongAtPos.get(i)) {
                    sb.append(c);
                }
                sb.append("])");
            }
        }

        sb.append("^").append(guessedRight).append("$");

        return sb.toString();
    }
}
