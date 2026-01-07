package GitWordle;

import java.util.ArrayList;

import Tools.Search.Binary;
import Wordle.Backend.hintClasses.Hint;
import Wordle.Backend.hintClasses.HintedAlphabet;
import Wordle.texts.fileInteraction;

public class wordleGame_Backend
{
    private final boolean EXPANDEDLIST;
    private final boolean HARDMODE;
    private final String WORD;
    private final String[] VALIDWORDS;

    private ArrayList<String> guessLog;
    private ArrayList<String> hintLog;
    private HintedAlphabet hintAlph;

    public wordleGame_Backend ()
    {
        this("NULL");
    }
    public wordleGame_Backend (String word) 
    {
        // true, false = default typical wordle
        EXPANDEDLIST = false;
        HARDMODE = true;
        
        String[] gameWords = fileInteraction.getGameWords();
        if (word.equals("NULL"))
            WORD = verifyWord(gameWords[(int)(Math.random() * gameWords.length)]);
        else
            WORD = verifyWord(word);

        if (EXPANDEDLIST)
            VALIDWORDS = fileInteraction.getAllWords();
        else 
            VALIDWORDS = gameWords;
        
        guessLog = new ArrayList<String>();
        hintLog = new ArrayList<String>();
        hintAlph = new HintedAlphabet();
    }

    //@SuppressWarnings("unused")
    public String submit (String guess)
    {
        if (HARDMODE && !hintAlph.matches(guess))
            return guess + ": doesn't comply with hard mode rules";

        else if (isValidGuess(guess)) 
        {
            enterGuess(guess);

            if (guess.equals(WORD))
                return "success";
            else if (guessLog.size() >= 6)
                return "fail";
            else
                return ""; // no information for the front end
        }
        else
            return guess + ": entered letters are not a valid word";
    }
    public boolean isValidGuess (String w) {
        return Tools.Search.Binary.search(VALIDWORDS, w) != -1;
    }
    public String getNewestHint() {
        return hintLog.getLast();
    }
    public String getWord() {
        return WORD;
    }

    private void enterGuess (String guess) 
    {
        guessLog.add(guess);

        Hint[] hint = getHint(guess);
        hintAlph.updateHints(guess, hint);

        String hintString = "";
        for (int k = 0; k < 5; k++) {
            if (hint[k] == Hint.INWORD)
                hintString += "E";
            else if (hint[k] == Hint.RIGHT)
                hintString += "O";
            else if (hint[k] == Hint.WRONG)
                hintString += "X";
            else 
                hintString += "?";
        }
        hintLog.add(hintString);
        return;
    }
    private Hint[] getHint (String guess) 
    {
        Hint[] hint = new Hint[5];

        String[] wArray = WORD.split("");
        String[] gArray = guess.split("");
        
        // sweep initially for correct placement
        for (int i = 0; i < 5; i++) {
            if (wArray[i].equals(gArray[i])) {
                hint[i] = Hint.RIGHT;
                wArray[i] = " ";
            } else {
                hint[i] = Hint.WRONG;
            }
        }

        // sweep guess lets for incorrect placement
        for (int i = 0; i < 5; i++)
        {
            // run through lets in word until you find the guessed letter at i, or finished
            int letIndex;
            for (letIndex = 0; letIndex < 5 && !wArray[letIndex].equals(gArray[i]); letIndex++);
            
            if (letIndex < 5 && hint[i] == Hint.WRONG) {
                wArray[letIndex] = " ";
                hint[i] = Hint.INWORD;
            }
        }

        return hint;
    }
    
    private static String verifyWord (String word)
    {
        if (word.length() != 5)
            new Exception("The returned word must be 5 letters in length.");
        if (!isWordleWord(word))
            new Exception("This word is not valid in wordle.");
        return word;
    }
    public static boolean isWordleWord (String word) {
        if (word.length() != 5)
            throw new IllegalArgumentException("a wordle word must be 5 letters: " + word);

        return Binary.search(fileInteraction.getAllWords(), word) != -1;
    }
    
    public HintedAlphabet getHintedAlph() {
        return hintAlph;
    }
}
