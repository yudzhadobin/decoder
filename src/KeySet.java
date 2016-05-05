import java.util.*;

/**
 * Created by yudzh_000 on 30.04.2016.
 */
public class KeySet {

    public Map<Character, Character> key;
    List<Character> opportunities;


    Stack<Character> doUndo = new Stack<>();
    public KeySet(){
        key = new HashMap<>();
        opportunities = new ArrayList<>();

//        char letter = 'а';
//        do {
//            letter ++;
//        }while (letter != 'я' + 1);
    }

    public void setAnswer(char key, char value) {
        this.key.put(key, value);
        reset();
    }

    public void cancel() {
        opportunities.add(doUndo.pop());
    }
    public void reset() {
        while (doUndo.size() != 0) {
            cancel();
        }
        for (Map.Entry<Character, Character> entry : key.entrySet()) {
            opportunities.remove((Object)entry.getValue());
            doUndo.push(entry.getValue());
        }
    }
    public List<Character> getOpportunities() {
       return new ArrayList<>(this.opportunities);
    }


}
