package dal;

import app.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class FileHandler {
    /**
     * Reads the lines of a file and return them as a list
     * @param fileName the name / path of the file
     * @return the lines of the file
     * */
    public static List<String> readFileLines(String fileName) {
        List<String> list = new LinkedList<>();
        String line;

        // Try loading wordlist.txt from jar
        InputStream is = Main.class.getClassLoader().getResourceAsStream(fileName);

        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                while ((line = br.readLine()) != null) {
                    list.add(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                while ((line = br.readLine()) != null) {
                    list.add(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
