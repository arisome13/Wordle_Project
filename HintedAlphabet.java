package GitWordle;

import java.util.ArrayList;

import Wordle.texts.fileInteraction;

public class HintedAlphabet
{
    public HintedLetter[] alph;

    public HintedAlphabet () 
    {
        alph = new HintedLetter[26];
        for (int i = 0; i < alph.length; i++)
            alph[i] = new HintedLetter((char)('a' + i));
    }

    public void updateHints (String word, Hint[] hints) 
    {
        for (HintedLetter l : alph)
            l.updateHints(word, hints);
    }
    public boolean matches (String word)
    {
        for (int i = 0; i < 5; i++)
            if (!alph[word.toLowerCase().charAt(i) - (int)'a'].matches(i))
                return false;
        return true;
    }
    public boolean matches (String word, boolean getHints)
    {
        for (int i = 0; i < 5; i++)
            if (!alph[word.toLowerCase().charAt(i) - (int)'a'].matches(i, getHints)) {
                if (getHints)
                    System.out.println(word + " doesn't match");
                return false;
            }
        if (getHints)
            System.out.println(word + " matches");
        return true;
    }
    
    public ArrayList<String> getMatchingGameWords ()
    {
        return getMatchingWords(fileInteraction.getGameWords());
    }
    public ArrayList<String> getMatchingWords (String[] validWords)
    {
        ArrayList<String> matched = new ArrayList<>();

        for (String w : validWords)
            if (matches(w))
                matched.add(w);
        
        return matched;
    }

    public boolean isBlank () {
        for (HintedLetter h : alph)
            if (!h.isBlank())
                return false;
        return true;
    }

    public String toString () 
    {
        String output = "";
        for (HintedLetter l : alph)
            output += l + "\n";
        return output.substring(0, output.length() - 1);
    }
}
