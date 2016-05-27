import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;


public class Key {
    Character coded;
    List<Answer> potential;

    public Key(Character character, List<Character> possible) {
        potential = new ArrayList<>();

        potential.addAll(possible.stream().map(ch -> new Answer(ch, character)).collect(Collectors.toList()));
        coded = character;
    }

    public Key() {
        potential = new ArrayList<>();
    }

    public Key(Pair<Character,List<Character>> possible) {
        this(possible.getKey(),possible.getValue());
    }

    public void put(List<Pair<Character, Character>> prev, Character character, List<Character> possible) {
        Answer answerToPut = null;
        List<Answer> answers = new ArrayList<>(potential);
        if(possible.isEmpty()) {

            return;
        }
        for (Answer answer : answers) {
            if (answer.decoded.equals(prev.get(0).getValue())) {
                answerToPut = answer;
                break;
            }
        }

        for (int i = 1; i < prev.size(); i++) {
            answers = answerToPut.next.potential;
            for (Answer answer : answers) {
                if (answer.decoded.equals(prev.get(i).getValue())) {
                    answerToPut = answer;
                    break;
                }
            }
        }
//        if (answerToPut.next != null) {
//            int j = 5;
//            return;
//        }
        answerToPut.next = new Key(character, possible);

    }

    public void deleteErrors(){
        // TODO: 12.05.2016  rewrite 
        int maxSize = 0;
        for (int i = 0; i < potential.size(); i++) {
            int curSize = 1;
            Answer curAnswer = potential.get(i);
            while(curAnswer.next != null) {
                curSize++;
            }
            if(curSize > maxSize) {
                maxSize = curSize;
            }
        }
        for (int i = 0; i < potential.size(); i++) {
            int curSize = 1;
            while(potential.get(i).next != null) {
                curSize++;
            }
            if(curSize < maxSize) {
                potential.remove(i);
                i--;
            }
        }
    }

    @Override
    public String toString() {
        String res = "";
        for (Answer answer : potential) {
            res += answer.toString() + " ";
        }
        return this.coded+ " ={"+ res + "}";
    }

    public List<List<Pair<Character, Character>>>  getAll() {
        Map<Answer, Boolean> history = new HashMap<>();
        List<List<Pair<Character, Character>>> allKeys = new ArrayList<>();
        for (Answer answer : potential) {
            if(!history.containsKey(answer)){
                List<Pair<Character, Character>> key = new ArrayList<>();
                dfs(answer, allKeys, key, history);

            }
        }
        int maxSize = 0;
        for (List<Pair<Character, Character>> allKey : allKeys) {
            if(allKey.size() > maxSize) {
                maxSize = allKey.size();
            }
        }
        final int max = maxSize;
        allKeys.removeIf(key -> key.size() < max);
        return allKeys;
    }

    public List<Pair<Character, Character>> getDefined () {
        Map<Character, Set<Character>> allOportunities = new HashMap<>();
        List<List<Pair<Character, Character>>> all = getAll();
        for (List<Pair<Character, Character>> key : all) {
            for (Pair<Character, Character> part : key) {
                if(!allOportunities.containsKey(part.getKey())) {
                    allOportunities.put(part.getKey(), new HashSet<>());
                }
                allOportunities.get(part.getKey()).add(part.getValue());
            }
        }
        List<Pair<Character, Character>> result = new ArrayList<>();
        for (Map.Entry<Character, Set<Character>> entry : allOportunities.entrySet()) {
            if(entry.getValue().size() == 1) {
                result.add(new Pair<>(entry.getKey(), entry.getValue().iterator().next()));
            }
        }
        return result;
    }

    private void remove(List<Pair<Character, Character>> prev) {
        Key keyToDel = this;
        for (int i = 0; i < prev.size() - 1; i++) {
            Pair<Character, Character> pair = prev.get(i);
            for (Answer answer : keyToDel.potential) {
                if(answer.code == pair.getKey()) {
                    keyToDel = answer.next;
                }
            }
        }
        Pair<Character, Character> pair = prev.get(prev.size() - 1);
        if(pair.getValue() == 'i') {
            int j = 5;
        }
        Answer answer = new Answer(pair.getValue(),pair.getKey());
        keyToDel.potential.remove(answer);
    }
//переписать dfs
    private void dfs(Answer answer,  List<List<Pair<Character, Character>>> allKeys, List<Pair<Character, Character>> key, Map<Answer, Boolean> history) {

        if(!history.containsKey(answer)) {
            key.add(new Pair<>(answer.code, answer.decoded));
        }


        if(answer.next == null) {
            allKeys.add(new ArrayList<>(key));

        } else {
            for (Answer answer1 : answer.next.potential) {
                if (!history.containsKey(answer1)) {
                    dfs(answer1, allKeys, key, history);
                }
            }
            history.put(answer, false);
        }
        key.remove(key.size() - 1);

      //  answer.flag = true;
    }
}

class Answer {
    static int counter = 0;
    int id;
    Character decoded;
    Character code;
    Key next;

    //boolean flag = true; // для dfs true-белая false-черная

    public Answer(Character answer, Character code) {
        this.decoded = answer;
        this.code = code;
        this.id = counter;
        counter ++;
    }

    @Override
    public String toString() {
        String str = decoded.toString() + " " + code.toString();
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        return id == answer.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
