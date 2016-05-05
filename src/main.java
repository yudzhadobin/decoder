import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by yudzh_000 on 28.04.2016.
 */
public class main {

    public static void main(String[] args) throws IOException {
        Dictionary dictionary =  new Dictionary();
        Path path = Paths.get("D:\\MyPrj\\Moscow_decoder\\Dictionaries\\english.txt");
        dictionary.loadFromFile(path);
        KeySet keySet = new EnglishKeySet();
        //String dataToDecode = "шувои,:?. эаы уытфвзы, йпш збо бъъчй б ьчыщвёчы чвзыоувои ъпчвзй по бо бъъчвзбэвпо-ёй-бъъчвзбэвпо ёбуву—ыбза бъъчвзбэвпо збо ыоьптзы чвзыоувои во эаы хбй мпуэ бъътпътвбэы ьпт вэ. вь оызыуубтй, бо бъъчвзбэвпо збо бъъчй зшуэпм зпоуэтбвоэу ёбуыг по эаы чвзыоувои уэбэшу пёэбвоыг ьтпм иппичы ъчбй. ьпт ыщбмъчы, бо бъъчвзбэвпо збо заызж эаы чвзыоувои уэбэшу бог эаыо бъъчй зшуэпм зпоуэтбвоэу эабэ бччпх эаы шуыт эп тшо вэ шочвзыоуыг ьпт б уъызвьвз фбчвгвэй ъытвпг. бо бъъчвзбэвпо збо бчуп тыуэтвзэ шуы пь эаы бъъчвзбэвпо эп б уъызвьвз гыфвзы, во бггвэвпо эп бой пэаыт зпоуэтбвоэу.\tэаы чвзыоувои уытфвзы ву б уызшты мыбоу пь зпоэтпччвои бззыуу эп йпшт бъъчвзбэвпоу. хаыо бо бъъчвзбэвпо заызжу эаы чвзыоувои уэбэшу, эаы иппичы ъчбй уытфыт увиоу эаы чвзыоувои уэбэшу тыуъпоуы шувои б жый ъбвт эабэ ву шовюшычй буупзвбэыг хвэа эаы бъъчвзбэвпо. йпшт бъъчвзбэвпо уэптыу эаы ъшёчвз жый во вэу зпмъвчыг .бъж ьвчы бог шуыу вэ эп фытвьй эаы чвзыоувои уэбэшу тыуъпоуы.\tбой бъъчвзбэвпо эабэ йпш ъшёчвуа эатпшиа иппичы ъчбй збо шуы эаы иппичы ъчбй чвзыоувои уытфвзы. оп уъызвбч бззпшоэ пт тыивуэтбэвпо ву оыыгыг. бггвэвпобччй, ёызбшуы эаы уытфвзы шуыу оп гыгвзбэыг ьтбмыхптж бъву, йпш збо бгг чвзыоувои эп бой бъъчвзбэвпо эабэ шуыу б мвовмшм бъв чыфыч пь 3 пт авиаыт.";
//
        String dataToDecode = "бъъчвзбэвпо";
        Decoder decoder = new Decoder(keySet,dataToDecode,dictionary);
        decoder.decode(7);

//        SortedMap<Integer, Character> map = new TreeMap<>();
//        map.put(0, 's');
//        map.put(1, 'b');
//        map.put(3, 'o');
//        List<MyUniqueString> list = dictionary.getAllWithCharactersAt(map,15);
        int i = 5;
    }
}
