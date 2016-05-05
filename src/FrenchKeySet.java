/**
 * Created by yudzh_000 on 30.04.2016.
 */
public class FrenchKeySet extends KeySet {

    public FrenchKeySet(){
        char letter = 'a';
        do {
            opportunities.add(letter);
            letter ++;
        }while (letter != 'z' + 1);
        int i  =5;
    }
}
