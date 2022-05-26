package app;

import model.ANSI;
import model.CharStates;

import java.util.*;

public class Main {
    public static final int GUESSES = 5;
    public static final int WORD_LENGTH = 5;

    public static String userInput() {
        String input;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print(": ");
            input = scanner.nextLine();
        } while (input.length() != WORD_LENGTH);

        return input.toLowerCase();
    }
    public static void main(String[] args) {
        boolean playAgain = true;
        WortleLogic wlogic = new WortleLogic();
        String actualWord = wlogic.getRandomWord();
        List<String> guesses = new ArrayList<>(GUESSES);
        List<List<CharStates>> priorCharStates = new ArrayList<>(GUESSES);

        List<Character> guessedWrong = new ArrayList<>(26);

        String input;

        // Todo: Add ASCII-Art logo
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();


        while (guesses.size() < GUESSES) {
            do {
                input = userInput();
            } while (!wlogic.checkWord(input));
            List<CharStates> charStates = wlogic.checkLetters(actualWord, input);

            for (int i = 0; i < charStates.size(); i++) {
                if (charStates.get(i) == CharStates.WRONG) {
                    guessedWrong.add(input.charAt(i));
                }
            }

            guesses.add(input);
            priorCharStates.add(charStates);

            ANSI.clearScreen();
            System.out.println("Wortle");
            System.out.println("------------------");

            System.out.printf("Guesses left: %d/%d%n", GUESSES - guesses.size(), GUESSES);

            for (int i = 0; i < guesses.size(); i++) {
                System.out.println(" " + ANSI.getFormattedGuess(guesses.get(i), priorCharStates.get(i)));
            }

            System.out.println("------------------");

            System.out.println(WortleLogic.generateKeyboard(guessedWrong));
        }
    }
}