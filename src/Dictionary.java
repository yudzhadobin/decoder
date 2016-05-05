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

    List<ArrayList<Bucket>> topLevel;
    int curSize;
    int numElements;

    public Dictionary() {
        topLevel = new ArrayList<>();
        curSize = 0;
        numElements = 0;
    }

    private void expand(int newSize){
        while (curSize < newSize + 1) {
            ArrayList<Bucket> listToAdd = new ArrayList<>();
            for (int i = 0; i <= curSize; i++) {
                listToAdd.add(new Bucket());
            }
            topLevel.add(listToAdd);
            curSize++;
        }
    }


    public void put(MyUniqueString string) {
        int i = string.length()-1;
        if(i + 1 > curSize) {
            expand(i);
        }
        List<Bucket> listToPut = topLevel.get(i);
        for (int j = 0; j < string.length() ; j++) {
            listToPut.get(j).put(string.charAt(j), string);
        }
        numElements++;
            
    }

    public void put(String string) {
        MyUniqueString str = new MyUniqueString(string);
        put(str);
    }


    public void loadFromFile(Path path) throws IOException {
        List<String> data = Files.readAllLines(path);
        for (String s : data) {
            this.put(s.toLowerCase());
        }
    }

    public List<MyUniqueString> getAllWithLengthEquals(int length) {
        return topLevel.get(length-1).get(0).getAllStrings();
    }

    public List<MyUniqueString> getAllWithCharactersAt(Map<Integer, Character> values, int length) {
        try {
            List<Bucket> buckets = this.topLevel.get(length - 1);

            Iterator it = values.entrySet().iterator();
            Map.Entry<Integer, Character> first = (Map.Entry) it.next();
            List<MyUniqueString> allStrings = buckets.get(first.getKey() - 1).getAllWithCharactersAt(first.getValue());
            while (it.hasNext()) {
                Map.Entry<Integer, Character> entry = (Map.Entry) it.next();
                for (int i = 0; i < allStrings.size(); i++) {
                    if (allStrings.get(i).charAt(entry.getKey() - 1) != entry.getValue()) {
                        allStrings.remove(allStrings.get(i));
                        i--;
                    }
                }
            }
            return allStrings;
        } catch (Exception e) {
            int i = 5;
        }
        return null;
    }
}

class Bucket{

    private HashMap<Character, List<MyUniqueString>> bucket;

    public Bucket() {
        bucket = new HashMap<>();
    }

    public void put(char character, MyUniqueString string) {
        if(!bucket.containsKey(character)) {
            bucket.put(character, new ArrayList<>());
        }
        bucket.get(character).add(string);
    }

    public List<MyUniqueString> getAllStrings() {
        List<MyUniqueString> result = new ArrayList<>();
        for (Character character : bucket.keySet()) {
            result.addAll(bucket.get(character));
        }
        return result;
    }

    public List<MyUniqueString> getAllWithCharactersAt(Character character) {
        try {
            return new ArrayList<>(bucket.get(character));
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

}
