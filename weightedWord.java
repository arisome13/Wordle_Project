package GitWordle;

import java.util.Comparator;

public class weightedWord 
{
    private char[] letters;
    private int value;

    public weightedWord (String s) 
    {
        if (s.length() != 5)
            throw new IllegalArgumentException(s + " does not have exactly 5 letters");

        letters = new char[5];

        for (int i = 0; i < 5; i++)
            letters[i] = s.charAt(i);

        value = 0;
    }
    public weightedWord (String s, int v) {
        this(s);
        value = v;
    }

    public static Comparator<weightedWord> getValueCmp () 
    {
        // sorts highest values first
        return new Comparator<weightedWord>() {
                public int compare(weightedWord o1, weightedWord o2) {
                    return Integer.compare(o2.getValue(), o1.getValue());
                }
        };
    }
    public static Comparator<weightedWord> getWordCmp () 
    {
        // sorts a -> z
        return new Comparator<weightedWord>() {
                public int compare(weightedWord o1, weightedWord o2) {
                    return o1.getWord().compareTo(o2.getWord());
                }
        };
    }

    public int getValue () {
        return value;
    }
    public String getWord () {
        String word = "";
        for (char c : letters) 
            word += c;
        return word;
    }

    public String toString () {
        return getWord();
    }
}