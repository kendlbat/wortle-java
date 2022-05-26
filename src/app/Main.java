package app;

import model.ANSI;
import model.CharStates;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final int GUESSES = 6;
    public static final int WORD_LENGTH = 5;

    /**
     * Get input from the user
     * Keeps asking until a valid input is given
     * Ignores the case of the input, always returns lowercase
     * @param regex The regex to validate the input against
     * @return the word the user wants to guess
     */
    public static String userInputAnyCase(String regex) {
        String input;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print(": ");
            input = scanner.nextLine();
        } while (!input.toLowerCase().matches(regex));

        return input.toLowerCase();
    }

    /**
     * @return the logo of the game as a string
     * */
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
        WortleLogic wlogic;
        String actualWord;
        List<String> guesses;
        List<List<CharStates>> priorCharStates;
        String currentRegex;
        List<Character> guessedWrong;
        String guessedRight;
        List<List<Character>> guessedWrongAtPos;
        String input;
        String wordListFile;

        while (playAgain) {
            ANSI.clearScreen();
            System.out.println(getLogo());
            System.out.println();
            System.out.println("(c) Karim Memić & Tobias Kendlbacher 2022");
            System.out.println();
            System.out.println("Welcome to Wortle!");
            System.out.print("Please choose a wordlist (DE/EN)");
            wordListFile = userInputAnyCase("(de|en)").strip().toUpperCase();


            // TODO: ACTUALLY IMPLEMENT THIS

            ANSI.clearScreen();

            wlogic = new WortleLogic(String.format("wordlists/%s.txt", wordListFile));
            actualWord = wlogic.getRandomWord();
            guesses = new ArrayList<>(GUESSES);
            priorCharStates = new ArrayList<>(GUESSES);
            guessedWrong = new ArrayList<>(26);
            guessedRight = ".....";
            guessedWrongAtPos = new ArrayList<>(WORD_LENGTH);
            for (int i = 0; i < WORD_LENGTH; i++) {
                guessedWrongAtPos.add(new ArrayList<>(GUESSES));
            }

            System.out.printf("Wortle (%s)%n", wordListFile);
            System.out.println("------------------");
            System.out.println("Please enter your first guess");
            System.out.println("------------------");

            while (true) {
                do {
                    input = userInputAnyCase("[a-z]{5}");
                } while (!wlogic.checkWord(input));
                List<CharStates> charStates = wlogic.checkLetters(actualWord, input);

                for (int i = 0; i < charStates.size(); i++) {
                    if (charStates.get(i) == CharStates.WRONG && !guessedWrong.contains(input.charAt(i))) {
                        guessedWrong.add(input.charAt(i));
                    } else if (charStates.get(i) == CharStates.CORRECT) {
                        guessedRight = guessedRight.substring(0, i) + input.charAt(i) + guessedRight.substring(i + 1);
                    } else if (charStates.get(i) == CharStates.IN_WORD && !guessedWrongAtPos.get(i).contains(input.charAt(i))) {
                        guessedWrongAtPos.get(i).add(input.charAt(i));
                    }
                }

                // Sort guessedWrong alphabetically
                guessedWrong = guessedWrong.stream().sorted().collect(Collectors.toList());

                guesses.add(input);
                priorCharStates.add(charStates);
                currentRegex = WortleLogic.generateRegex(guessedWrong, guessedWrongAtPos, guessedRight);

                ANSI.clearScreen();
                System.out.printf("Wortle (%s)%n", wordListFile);
                System.out.println("------------------");
                System.out.printf("Current RegEX: %s%n", currentRegex);
                System.out.printf("Possible matches: %d/%d%n", wlogic.getMatchAmount(currentRegex), wlogic.getWordAmount());
                System.out.printf("Guesses left: %d/%d%n", GUESSES - guesses.size(), GUESSES);

                for (int i = 0; i < guesses.size(); i++) {
                    System.out.println(" " + ANSI.getFormattedGuess(guesses.get(i), priorCharStates.get(i)));
                }

                System.out.println("------------------");

                if (input.equals(actualWord)) {
                    System.out.printf("Wortle (%s)%n", wordListFile);
                    System.out.println("------------------");
                    System.out.println("Congratulations! You guessed the word!");
                    System.out.println("------------------");
                    System.out.print("Play again? (y/n)");
                    if (userInputAnyCase("(y|n)").equals("n")) {
                        playAgain = false;
                    }
                    break;
                }

                if (!(guesses.size() < GUESSES)) {
                    System.out.printf("Wortle (%s)%n", wordListFile);
                    System.out.println("------------------");
                    System.out.printf("You lost! The word was \"%s\"%n", actualWord);
                    System.out.println("------------------");
                    System.out.print("Play again? (y/n)");
                    if (userInputAnyCase("(y|n)").equals("n")) {
                        playAgain = false;
                    }
                    break;
                }

                System.out.println(WortleLogic.generateKeyboard(guessedWrong, guessedWrongAtPos, guessedRight));
            }

        }
    }
}