package GitWordle;

import java.util.ArrayList;

import Wordle.Backend.hintClasses.HintedAlphabet;
import Wordle.texts.fileInteraction;

public class searchW 
{
    public static String bestGameFit (HintedAlphabet letterList) {
        return orderedFits(letterList, fileInteraction.getGameWords(), 1)[0];
    }
    public static String[] topGameFits (HintedAlphabet letterList, int numWords) {
        return orderedFits(letterList, fileInteraction.getGameWords(), numWords);
    }
    public static String[] gameFits (HintedAlphabet letterList) {
        String[] words = fileInteraction.getGameWords();
        return orderedFits(letterList, words, words.length);
    }

    public static String bestFitAll (HintedAlphabet letterList) {
        return orderedFits(letterList, fileInteraction.getAllWords(), 1)[0];
    }
    public static String[] topFitsAll (HintedAlphabet letterList, int numWords) {
        return orderedFits(letterList, fileInteraction.getAllWords(), numWords);
    }
    public static String[] allFits (HintedAlphabet letterList) {
        String[] words = fileInteraction.getAllWords();
        return orderedFits(letterList, words, words.length);
    }

    // returns the matching words in order of likeliness
    public static String[] orderedFits (HintedAlphabet letterList, String[] availableWords, int maxNum)
    {
        // get the probability for each matching word
        // sorted the list form least to greatest value
        ArrayList<weightedWord> wordList = wordScale.getOrderedWeightedList(letterList.getMatchingWords(availableWords));

        // convert arraylist to array with 'maxNum' number of words
        String[] topArray = new String[Math.min(wordList.size(), maxNum)];
        for (int i = 0; i < topArray.length; i++)
            topArray[i] = wordList.get(i).getWord();

        // return strings in order of probability that match the hintedalphabet
        return topArray;
    }
}