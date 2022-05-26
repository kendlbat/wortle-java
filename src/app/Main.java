package app;

import model.ANSI;
import model.CharStates;

import java.util.*;
import java.util.stream.Collectors;

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

    public static String getLogo() {
        return """
                          _____                   _______                   _____                _____                    _____            _____         \s
                         /\\    \\                 /::\\    \\                 /\\    \\              /\\    \\                  /\\    \\          /\\    \\        \s
                        /::\\____\\               /::::\\    \\               /::\\    \\            /::\\    \\                /::\\____\\        /::\\    \\       \s
                       /:::/    /              /::::::\\    \\             /::::\\    \\           \\:::\\    \\              /:::/    /       /::::\\    \\      \s
                      /:::/   _/___           /::::::::\\    \\           /::::::\\    \\           \\:::\\    \\            /:::/    /       /::::::\\    \\     \s
                     /:::/   /\\    \\         /:::/~~\\:::\\    \\         /:::/\\:::\\    \\           \\:::\\    \\          /:::/    /       /:::/\\:::\\    \\    \s
                    /:::/   /::\\____\\       /:::/    \\:::\\    \\       /:::/__\\:::\\    \\           \\:::\\    \\        /:::/    /       /:::/__\\:::\\    \\   \s
                   /:::/   /:::/    /      /:::/    / \\:::\\    \\     /::::\\   \\:::\\    \\          /::::\\    \\      /:::/    /       /::::\\   \\:::\\    \\  \s
                  /:::/   /:::/   _/___   /:::/____/   \\:::\\____\\   /::::::\\   \\:::\\    \\        /::::::\\    \\    /:::/    /       /::::::\\   \\:::\\    \\ \s
                 /:::/___/:::/   /\\    \\ |:::|    |     |:::|    | /:::/\\:::\\   \\:::\\____\\      /:::/\\:::\\    \\  /:::/    /       /:::/\\:::\\   \\:::\\    \\\s
                |:::|   /:::/   /::\\____\\|:::|____|     |:::|    |/:::/  \\:::\\   \\:::|    |    /:::/  \\:::\\____\\/:::/____/       /:::/__\\:::\\   \\:::\\____\\
                |:::|__/:::/   /:::/    / \\:::\\    \\   /:::/    / \\::/   |::::\\  /:::|____|   /:::/    \\::/    /\\:::\\    \\       \\:::\\   \\:::\\   \\::/    /
                 \\:::\\/:::/   /:::/    /   \\:::\\    \\ /:::/    /   \\/____|:::::\\/:::/    /   /:::/    / \\/____/  \\:::\\    \\       \\:::\\   \\:::\\   \\/____/\s
                  \\::::::/   /:::/    /     \\:::\\    /:::/    /          |:::::::::/    /   /:::/    /            \\:::\\    \\       \\:::\\   \\:::\\    \\    \s
                   \\::::/___/:::/    /       \\:::\\__/:::/    /           |::|\\::::/    /   /:::/    /              \\:::\\    \\       \\:::\\   \\:::\\____\\   \s
                    \\:::\\__/:::/    /         \\::::::::/    /            |::| \\::/____/    \\::/    /                \\:::\\    \\       \\:::\\   \\::/    /   \s
                     \\::::::::/    /           \\::::::/    /             |::|  ~|           \\/____/                  \\:::\\    \\       \\:::\\   \\/____/    \s
                      \\::::::/    /             \\::::/    /              |::|   |                                     \\:::\\    \\       \\:::\\    \\        \s
                       \\::::/    /               \\::/____/               \\::|   |                                      \\:::\\____\\       \\:::\\____\\       \s
                        \\::/____/                 ~~                      \\:|   |                                       \\::/    /        \\::/    /       \s
                         ~~                                                \\|___|                                        \\/____/          \\/____/        \s
                                                                                                                                                         \s
                """;
    }

    public static void main(String[] args) {
        boolean playAgain = true;
        WortleLogic wlogic = new WortleLogic();
        String actualWord = wlogic.getRandomWord();
        List<String> guesses = new ArrayList<>(GUESSES);
        List<List<CharStates>> priorCharStates = new ArrayList<>(GUESSES);

        List<Character> guessedWrong = new ArrayList<>(26);
        String guessedRight = ".....";
        List<Character> guessedInWord = new ArrayList<>(WORD_LENGTH);

        String input;

        ANSI.clearScreen();
        System.out.println(getLogo());

        while (guesses.size() < GUESSES) {
            do {
                input = userInput();
            } while (!wlogic.checkWord(input));
            List<CharStates> charStates = wlogic.checkLetters(actualWord, input);

            for (int i = 0; i < charStates.size(); i++) {
                if (charStates.get(i) == CharStates.WRONG && !guessedWrong.contains(input.charAt(i))) {
                    guessedWrong.add(input.charAt(i));
                } else if (charStates.get(i) == CharStates.CORRECT) {
                    guessedRight = guessedRight.substring(0, i) + input.charAt(i) + guessedRight.substring(i + 1);
                } else if (charStates.get(i) == CharStates.IN_WORD && !guessedInWord.contains(input.charAt(i))) {
                    guessedInWord.add(input.charAt(i));
                }
            }

            // Sort guessedWrong alphabetically
            guessedWrong = guessedWrong.stream().sorted().collect(Collectors.toList());

            guesses.add(input);
            priorCharStates.add(charStates);

            ANSI.clearScreen();
            System.out.println("Wortle");
            System.out.println("------------------");
            System.out.printf("Current RegEX: %s%n", WortleLogic.generateRegex(guessedWrong, guessedInWord, guessedRight));
            System.out.printf("Guesses left: %d/%d%n", GUESSES - guesses.size(), GUESSES);

            for (int i = 0; i < guesses.size(); i++) {
                System.out.println(" " + ANSI.getFormattedGuess(guesses.get(i), priorCharStates.get(i)));
            }

            System.out.println("------------------");

            System.out.println(WortleLogic.generateKeyboard(guessedWrong));
        }
    }
}