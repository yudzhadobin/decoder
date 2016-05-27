import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by yudzh_000 on 28.04.2016.
 */
public class main {


    public static void main(String[] args) throws IOException {

        Dictionary dictionary =  new Dictionary();
        Path path = null;
        switch (args[0]) {
            case "en":
                path = Paths.get(".\\bin\\Dictionaries\\english.txt");
                break;
            case "es":
                path = Paths.get(".\\bin\\Dictionaries\\espan.txt");
                break;
            case "fr":
                path = Paths.get(".\\bin\\Dictionaries\\fr.txt");
                break;
        }

        dictionary.loadFromFile(path);
        KeySet keySet = new EnglishKeySet();
        String content = new Scanner(new File(args[1].replaceAll("\\?", ""))).useDelimiter("\\Z").next();
        Decoder decoder = new Decoder(keySet, content, dictionary);
        List<Pair<Character, Character>> result = decoder.decode();

        String res = decoder.decodeWithKey(content, result);
        File f = new File(args[2]);
        f.createNewFile();
        PrintWriter writer = new PrintWriter(args[2].replaceAll("\\?",""), "UTF-8");
        writer.print("SEQ {");
        for (Pair<Character, Character> pair : result) {
            writer.print(pair.getValue().toString().toUpperCase() + ",");
        }
        writer.println("}:");
        writer.println(res);
        writer.close();

    }
}
