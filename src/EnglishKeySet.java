import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yudzh_000 on 29.04.2016.
 */
public class EnglishKeySet extends KeySet {

    public EnglishKeySet(){
        char letter = 'a';
        do {
            opportunities.add(letter);
            letter ++;
        }while (letter != 'z' + 1);
    }

}
