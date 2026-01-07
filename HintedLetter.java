package GitWordle;

import java.util.Arrays;

public class HintedLetter
{
    private char letter;
    private Hint[] letterHints;
    
    // null     = no info (guessed letters have been 'letter')
    // INWORD   = word contains the 'letter' ('letter' has recieved an INWORD or RIGHT in update)
    // WRONG    = word doesn't contain the letter (all hints are WRONG)
    private Hint letterState;

    public HintedLetter (char let)
    {
        letter = let;
        letterHints = new Hint[5];
        letterState = null;
    }

    public void updateHints (String guess, Hint[] guessHint) 
    {
        for (int i = 0; i < 5; i++)
        {
            if (guess.charAt(i) == letter)
            {
                if (guessHint[i] == Hint.WRONG)
                {
                    boolean hasMaybe = false; // find out if letter hints contains a "maybe"
                    for (int j = 0; j < 5; j++)
                    {
                        if (guessHint[j] == Hint.INWORD && guess.charAt(j) == letter) 
                        {
                            hasMaybe = true;
                            break;
                        }
                    }

                    if (!hasMaybe) // if letterHints only contains RIGHTs and WRONGs
                    {
                        for (int j = 0; j < 5; j++)
                            if (letterHints[j] == null)
                                letterHints[j] = Hint.WRONG;
                        
                        boolean isAllWrong = true;
                        for (Hint h : letterHints) 
                            if (h != null)
                                isAllWrong = false;
                        if (isAllWrong)
                            letterState = Hint.WRONG;
                    }
                    else // guessHints contain a Hint.INWORD for this letter
                    {
                        letterHints[i] = Hint.WRONG;
                    }
                } 
                else if (guessHint[i] == Hint.INWORD) 
                {
                    letterHints[i] = Hint.WRONG;
                    letterState = Hint.INWORD;
                }
                else if (guessHint[i] == Hint.RIGHT)
                {
                    letterHints[i] = Hint.RIGHT;
                    letterState = Hint.INWORD;
                }
            }
            else // word letter at i is a random letter (not this.letter)
            {
                if (guessHint[i] == Hint.RIGHT)
                    letterHints[i] = Hint.WRONG;
                else if (guessHint[i] == Hint.WRONG || guessHint[i] == Hint.INWORD)
                    ; // no effect
            }
        }

        check();
    }
    private void check () {
        for (int i = 0; i < 5; i++) {
            if (letterState == Hint.WRONG && letterHints[i] != Hint.WRONG) {
                new Exception("CHECK letter hints!!").printStackTrace();
                System.exit(0);
            }
        }
    }

    public boolean matches (int location)
    {
        return matches(location, false);
    }
    public boolean matches (int location, boolean getHints)
    {
        if (getHints) {
            if (letterState == Hint.WRONG)
                System.out.println("Isn't in word: ");
            System.out.println(letter + " at " + location + " is " + letterHints[location] + " so matches returns: " + (letterHints[location] != Hint.WRONG));
        }

        return letterState != Hint.WRONG && letterHints[location] != Hint.WRONG;
    }

    public boolean isIncluded () {
        return letterState == Hint.INWORD;
    }
    public boolean isntIncluded () {
        return letterState == Hint.WRONG;
    }
    public boolean isBlank () {
        for (Hint h : letterHints)
            if (h != null)
                return false;
        return true;
    }

    public String toString () {
        if (letterState == Hint.WRONG)
            return "[" + letter + " :  NOT_IN_WORD ]";
        return "[" + letter + " : " + Arrays.toString(letterHints) + "]";
    }
}

