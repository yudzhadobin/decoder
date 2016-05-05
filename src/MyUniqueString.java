/**
 * Created by yudzh_000 on 28.04.2016.
 */
public class MyUniqueString {
    String string;

    public MyUniqueString (String string) {
        this.string = string;
    }

    public int length() {
        return string.length();
    }

    public char charAt(int i) {
        return string.charAt(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyUniqueString string1 = (MyUniqueString) o;

        return string != null ? string.equals(string1.string) : string1.string == null;

    }

    @Override
    public int hashCode() {
        return string != null ? string.hashCode() : 0;
    }

    @Override
    public String toString() {
        return string.toString();
    }
}
