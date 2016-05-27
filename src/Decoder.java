import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yudzh_000 on 30.04.2016.
 */
public class Decoder {
    private KeySet keySet;
    private List<String> all;

    private Dictionary dictionary;

    public Decoder(KeySet keySet, String test, Dictionary dictionary) {
        this.keySet = keySet;
        List<String> allStrings = Arrays.asList(test.replaceAll("[:,?.]", "").replaceAll("[-\\t]", " ").split(" "));
        Set<String> unique = new HashSet<>();
        for (String s : allStrings) {
            if(s.contains("—")) {
                continue;
            }
            unique.add(s);
        }

        this.all = new ArrayList<>();
        this.all.addAll(unique);
        Collections.sort(this.all, (o1, o2) -> Integer.compare(o2.length(), o1.length()));
        this.dictionary = dictionary;
    }

    public  List<Pair<Character, Character>> decode(){
        int numStepsWithoutChange = 0;
        int numDecoded = 0;
        Set<Pair<Character, Character>> allKeys = new HashSet<>();
        List<String> informativeWords = sortByNumberOfUndecidedCharacters(all, allKeys, 0);
        List<Character> top = findTopCharacters(informativeWords);
        Key key = initKey(top, informativeWords);
        do {
            List<Pair<Character, Character>> decoded = findRightKey(top, key, 20, informativeWords);
            allKeys.addAll(decoded);
            try {
                informativeWords = sortByNumberOfUndecidedCharacters(all, allKeys, numStepsWithoutChange);
            } catch (Exception e) {
                break;
            }
            top = findTopCharacters(informativeWords);
            if(allKeys.size() == numDecoded) {
                numStepsWithoutChange ++;
            } else {
                numStepsWithoutChange = 0;
            }
            numDecoded = allKeys.size();
        } while (numStepsWithoutChange < 10 || this.all.size() > 3);
        List<Pair<Character, Character>> keys = key.getDefined();
        for (String s : this.all) {
            if(s.length() > 2) {
                List<String> res = dictionary.getAllStrings(s.length(), findEntries(s, keys));
                if(res.size() == 1) {
                    for (int i = 0; i < s.length(); i++) {
                        if(!keys.contains(new Pair<>(s.charAt(i), res.get(0).charAt(i)))) {
                            keys.add(new Pair<>(s.charAt(i), res.get(0).charAt(i)));
                        }
                    }
                }
            }
        }

        return keys;
    }

    public String decodeWithKey(String string, List<Pair<Character, Character>> key) {
        String result = "";
        for (int i = 0; i < string.length(); i++) {
            boolean finded = false;
            for (int j = 0; j < key.size(); j++) {
                if(key.get(j).getKey() == string.charAt(i)) {
                    result += key.get(j).getValue().toString().toUpperCase();
                    finded = true;
                    break;
                }
            }
            if(!finded) {
                result += string.charAt(i);
            }
        }
        return result;
    }

    private Key initKey(List<Character> top, List<String> informativeWords) {
        Map<String, Set<Character>> op = new HashMap<>();
        for (int i = 0; i < informativeWords.size(); i++) {
            String word = informativeWords.get(i);
            for (Character opportunity : keySet.opportunities) {
                List<Pair<Character, Character>> keys = new ArrayList<>();
                keys.add(new Pair<>(top.get(0), opportunity));

                if (word.contains(top.get(0).toString())) {
                    if(!op.containsKey(word)) {
                        op.put(word, new HashSet<>());
                    }
                    int index = word.indexOf(top.get(0));
                    op.get(word).addAll(findAllOpportunities(keys, index, word));
                }

            }
        }
        List<Character> result = new ArrayList<>(keySet.opportunities);
        for (Set<Character> opot : op.values()) {
            result = cross(result, opot);
        }
        return new Key(new Pair<>(top.get(0), result));
    }

    private List<Pair<Character, Character>> findRightKey(List<Character> top, Key key, int n, List<String> informativeWords) {
        for (int i = 0; i < top.size(); i++) {
            final int sub = i;
            List<List<Pair<Character, Character>>> allKeys = key.getAll();
           // allKeys = filterKeys(allKeys);
            allKeys.parallelStream().forEach(previousKey -> {
                List<Character> allResults = new ArrayList<>();
                for (Character opportunity : keySet.opportunities) {
                    List<Pair<Character, Character>> curKey = new ArrayList<>(previousKey);
                    curKey.add(new Pair<>(top.get(sub), opportunity)); // сгенерировали ключ на текущую итерацию
                    Map<String, Set<Character>> op = new HashMap<>();// возможные варианты для каждого слова
                    for (String word : informativeWords) {
                        if (word.contains(top.get(sub).toString())) {
                            if (!op.containsKey(word)) {
                                op.put(word, new HashSet<>());
                            }
                            int index = word.indexOf(top.get(sub));
                            op.get(word).addAll(findAllOpportunities(curKey, index, word));
                        }
                    }
                    List<Character> result = new ArrayList<>(keySet.opportunities);
                    for (Set<Character> opot : op.values()) {
                        result = cross(result, opot);
                    }
                    allResults.addAll(result);
                }

                key.put(previousKey, top.get(sub), allResults);
            });
  //          key.deleteErrors();
            List<Pair<Character, Character>> result = key.getDefined();
            if(result.size() >= n){
                return result;
            }
        }
        return key.getDefined();
    }

    private List<String> sortByNumberOfUndecidedCharacters(List<String> words, Set<Pair<Character, Character>> key, int pointer) {
        Map<String, Integer> sub = new HashMap<>();
        List<Pair<Character, Character>>  definedKeys = new ArrayList<>();
        definedKeys.addAll(key);
        List<String> result = new ArrayList<>();
        List<String> filtred = new ArrayList<>();
        for (String word : words) {
            int numbElement = word.length();
            for (Pair<Character, Character> definedKey : definedKeys) {
                int index = 0;
                while ((index = word.indexOf(definedKey.getKey(), index)) != -1) {
                    numbElement--;
                    index ++;
                }
            }
            sub.put(word, numbElement);
            if(numbElement != 0) {
                filtred.add(word);
            }
        }
        sub = MapUtil.sortByValue(sub);
        this.all = filtred;
        Iterator<Map.Entry<String, Integer>> iterator = sub.entrySet().iterator();
        for (int i = 0; i < pointer; i++) {
            iterator.next();
        }
        String main = iterator.next().getKey();
        result.add(main);
        Set<Character> top = new HashSet<>();
        for (char c : main.toCharArray()) {
            top.add(c);
        }
        for (Pair<Character, Character> defined : definedKeys) {
            top.remove(defined.getKey());
        }
        sub = new HashMap<>();
        for (String word : words) {
            int founed = 0;
            for (Character c : top) {
                int index = 0;
                while ((index = word.indexOf(c , index)) != -1) {
                    founed++;
                    index ++;
                }
                sub.put(word, founed);
            }
        }
        sub = MapUtil.sortByValue(sub);

        iterator = sub.entrySet().iterator();
        iterator.next();
        result.add(iterator.next().getKey());
        try {
            result.add(iterator.next().getKey());
        } catch (NoSuchElementException ex) {
                int j  = 5;
        }
        return result;
    }

    private List<List<Pair<Character, Character>>> filterKeys(List<List<Pair<Character, Character>>> allKeys) {
        List<List<Pair<Character, Character>>> result = new ArrayList<>(allKeys);
        for (List<Pair<Character, Character>> key : allKeys) {
            for (String s : all) {
                List<String> allStrings = dictionary.getAllStrings(s.length(), findEntries(s, key));
                if (allStrings.isEmpty()) {
                    result.remove(key);
                }
            }
        }
        return result;
    }

    private List<Character> findAllOpportunities(List<Pair<Character, Character>> keys, int index, String string) {
        List<String> allStrings = dictionary.getAllStrings(string.length(), findEntries(string, keys));
        Set<Character> characters = null;
        try {
            characters = allStrings.stream().map(s -> s.charAt(index)).collect(Collectors.toSet());
        }catch (Exception e) {
            int b = 5;
        }
        return new ArrayList<>(characters);
    }

    private List<Character> findTopCharacters(List<String> words) {
        Map<Character, Integer> frequentlyEncountered = new HashMap<>();
        for (String s : words) {
            for (int i = 0; i < s.length(); i++) {
                if (frequentlyEncountered.containsKey(s.charAt(i))) {
                    frequentlyEncountered.put(s.charAt(i), frequentlyEncountered.get(s.charAt(i)) + 1);
                } else {
                    frequentlyEncountered.put(s.charAt(i), 1);
                }
            }
        }
        frequentlyEncountered = MapUtil.sortByValue(frequentlyEncountered);
        return new ArrayList<>(frequentlyEncountered.keySet());
    }

    private  Map<Character, List<Integer>>  findEntries(String string, List<Pair<Character, Character>> keys) {
        Map<Character, List<Integer>> result = new HashMap<>();
        int index = -1;
        for (Pair<Character, Character> pair : keys) {
            do {
                index = string.indexOf(pair.getKey(), index + 1);
                if(index >= 0) {
                    if(result.containsKey(pair.getValue())) {
                        result.get(pair.getValue()).add(index + 1);
                    } else {
                        result.put(pair.getValue(), new ArrayList<>());
                        result.get(pair.getValue()).add(index + 1);
                    }
                }
            }while (index >= 0);
        }
        return result;
    }

    private List<Character> cross(List<Character> first, Set<Character> second) {
        List<Character> result = new ArrayList<>(first);
        result.removeIf(character -> !second.contains(character));
        return result;
    }
}
