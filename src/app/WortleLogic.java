package app;

import dal.FileHandler;
import model.ANSI;
import model.CharStates;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WortleLogic {
    public List<String> wordList;

    public WortleLogic() {
        wordList = FileHandler.readFileLines("wordlist.txt");
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
}
