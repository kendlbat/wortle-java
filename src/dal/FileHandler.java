package dal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class FileHandler {
    public static String getRandomWord() {
        Random random = new Random();

        List<String> words = readFileLines("wordlist.txt");

        return words.get(random.nextInt(words.size()));
    }

    public static List<String> readFileLines(String fileName) {
        List<String> list = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            list.add(br.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
