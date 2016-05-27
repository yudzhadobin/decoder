import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.*;
import java.nio.file.Files;

/**
 * Created by yudzh_000 on 28.04.2016.
 */
public class Dictionary {

    List<ArrayList<String>> topLevel;
    int curSize;
    int numElements;

    public Dictionary() {
        topLevel = new ArrayList<>();
        curSize = 0;
        numElements = 0;
    }

    private void expand(int newSize){
        while (curSize < newSize + 1) {
            topLevel.add(new ArrayList<>());
            curSize++;
        }
    }


    public void put(String string) {
        int i = string.length()-1;
        if(i + 1 > curSize) {
            expand(i);
        }
        topLevel.get(i).add(string);
        numElements++;

    }

    public void loadFromFile(Path path) throws IOException {
        List<String> data = Files.readAllLines(path);
        for (String s : data) {
            this.put(s.toLowerCase());
        }
    }


    public List<String> getAllStrings(int length, Map<Character, List<Integer>> keys) {
        List<String> result = new ArrayList<>(topLevel.get(length - 1));
        result.removeIf(s -> {
            Map<Character, List<Integer>> test = new HashMap<>();
            for (int i = 0; i < s.length(); i++) {
                if(keys.containsKey(s.charAt(i))) {
                    if(test.containsKey(s.charAt(i))) {
                        test.get(s.charAt(i)).add(i + 1);
                    } else {
                        test.put(s.charAt(i), new ArrayList<>());
                        test.get(s.charAt(i)).add(i + 1);
                    }
                }
            }
            boolean flag = keys.equals(test);
            return !flag;
        });
        if(result.isEmpty()) {
            result.addAll(new ArrayList<>(topLevel.get(length - 2))); //hack если в конце s
            result.removeIf(s -> {
                Map<Character, List<Integer>> test = new HashMap<>();
                for (int i = 0; i < s.length(); i++) {
                    if(keys.containsKey(s.charAt(i))) {
                        if(test.containsKey(s.charAt(i))) {
                            test.get(s.charAt(i)).add(i + 1);
                        } else {
                            test.put(s.charAt(i), new ArrayList<>());
                            test.get(s.charAt(i)).add(i + 1);
                        }
                    }
                }

                boolean flag = keys.equals(test);
                return !flag;
            });
        }
        return result;
    }
}