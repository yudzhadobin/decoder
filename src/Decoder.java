import java.util.*;

/**
 * Created by yudzh_000 on 30.04.2016.
 */
public class Decoder {
    private KeySet key;
    private List<String> test;

    private Dictionary dictionary;
    private List<String> informativeWords;

    private final int K = 1;

    public Decoder(KeySet key, String test, Dictionary dictionary) {
        this.key = key;
        this.test = Arrays.asList(test.replaceAll("[:,?.]", "").replaceAll("[-\\t]", " ").split(" "));
        Collections.sort(this.test, (o1, o2) -> Integer.compare(o2.length(), o1.length()));
        this.dictionary = dictionary;
        this.informativeWords = new ArrayList<>();
    }

    public void decode(int n) {
        informativeWords.addAll(test.subList(0,K));
        Object[] frequentlyEncountered = countEntry().keySet().toArray();
        List<Character> codes =  new ArrayList<Character>();
        for (int i = 0; i < frequentlyEncountered.length; i++) {
            codes.add((Character)frequentlyEncountered[i]);
        }
        recurs(codes.get(0), 0, n, codes);
    }

    boolean isExit = false;
    private void recurs (Character code, int i, int n, List<Character> codes) {
        List<Character> opportunities = key.getOpportunities();
        if(i >= n) {
            isExit = true;
            return;
        }
        for (int i1 = 0; i1 < opportunities.size(); i1++) {
            Character cur = opportunities.get(i1);
            key.setAnswer(code, cur);
            if(key.key.size() >= 4) {
                if (key.key.get('б') == 'a' && key.key.get('в') == 'i' && key.key.get('э') == 't' && key.key.get('ч') == 'l') {
                    int j = 5;
                }
            }
            if(check(informativeWords)) {
                if(checkOnAll()) {
                    recurs(codes.get(i + 1), i + 1, n, codes);
                    if(isExit) {
                        return;
                    }
                }
            } else {
                key.cancel();
            }
        }
        key.key.remove(code);
        key.reset();
    }

    private Map<Character, Integer> countEntry() {
        Map<Character, Integer> frequentlyEncountered = new HashMap<>();
        for (String s : informativeWords) {
            for (int i = 0; i < s.length(); i++) {
                if (frequentlyEncountered.containsKey(s.charAt(i))) {
                    frequentlyEncountered.put(s.charAt(i), frequentlyEncountered.get(s.charAt(i)) + 1);
                } else {
                    frequentlyEncountered.put(s.charAt(i), 1);
                }
            }
        }
        Map<Character, Integer> sorted = MapUtil.sortByValue(frequentlyEncountered);
        return sorted;
    }

    private boolean check(List<String> test) {
        for (String informativeWord : test) {
            Map<Integer, Character> founded = new TreeMap<>();
            for (Map.Entry<Character, Character> entry : key.key.entrySet()) {
                int pos = informativeWord.indexOf(entry.getKey());
                if (pos == -1) {
                    continue;
                } else {
                    founded.put(pos + 1, entry.getValue());
                }
            }
            if(founded.isEmpty()) {
                continue;
            }
            if (dictionary.getAllWithCharactersAt(founded, informativeWord.length()).isEmpty()) {
                return false;
            }

        }

        return true;
    }

    private boolean checkOnAll() {
        return check(this.test);

    }
}
