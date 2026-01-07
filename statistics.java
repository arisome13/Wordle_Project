package GitWordle;

import java.util.ArrayList;
import java.util.Arrays;

import Wordle.Backend.backendW;
import Wordle.Backend.weightClasses.searchW;
import Wordle.texts.fileInteraction;

public class statistics 
{
    private static boolean getMessages;
    private static ArrayList<String> failed = new ArrayList<>();
    private static String[] storedFails = "baker, boxer, brake, fatty, fewer, fixer, found, foyer, frown, froze, gazer, graze, hatch, jaunt, jewel, joker, jolly, krill, mower, newer, poker, prize, prove, shake, swore, tribe, vaunt, wafer, waste, watch, waver, willy, wooer, wound, wreak".split(", ");

    private static void debug (Object message) {
        if (getMessages)
            System.out.println(message.toString());
    }

    public static int stepsFor (String word)
    {
        backendW w = new backendW(word);
        String nextGuess;
        String result = "";
        int steps = 0;

        while (result.equals(""))
        {
            nextGuess = searchW.bestGameFit(w.getHintedAlph());

            String[] fits = searchW.gameFits(w.getHintedAlph());
            debug(" " + fits.length + " words left");
            if (fits.length < 15)
                debug(Arrays.toString(fits));

            debug("Next guess is " + nextGuess);
            result = w.submit(nextGuess);
            debug((steps + 1) + "            " + result + " " + w.getNewestHint());
            for (int i = 0; i < 5; i++)
                ;//debug(w.getHintedAlph().alph[(int)(word.charAt(i)) - (int)('a')]);
            steps++;
        }
        debug("final result was \"" + result +  "\"");
        if (result.equals("fail")) {
            failed.add(word);
            steps++;
        }

        return steps;
    }

    public static void printStatistics () 
    {
        printStatistics(fileInteraction.getGameWords(), "");
    }
    public static void printStatistics (String stopWord) 
    {
        printStatistics(fileInteraction.getGameWords(), stopWord);
    }
    public static void printStatistics (String[] wordsToTest, String stopWord) 
    {
        String[] words = wordsToTest;
        String word;
        int totalSteps = 0;
        int steps;

        for (int i = 0; i < words.length; i++)
        {
            word = words[i];
            steps = stepsFor(word);
            
            System.out.println(word + " " + steps);
            
            if (word.equals(stopWord))
                return;

            totalSteps += steps;
        }

        System.out.println("\nAverage steps taken: " + totalSteps / (double) words.length);
        System.out.println(failed + "\n");
    }

    public static void testRandWord () 
    {
        String[] words = fileInteraction.getGameWords();
        String word;
        
        word = words[(int)(words.length * Math.random())];

        System.out.println(word);
        System.out.println(stepsFor(word));
    }

    public static void testWord (String word) 
    {
        System.out.println(word.toUpperCase() + "\n");
        System.out.println(stepsFor(word));
    }

    public static void testFailedWords () {
        for (String w : storedFails) {
            testWord(w);
            System.out.println("\n");
        }
    }

    public static void main(String[] args) 
    {
        getMessages = true;
        testFailedWords();
    }
}
