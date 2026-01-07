package GitWordle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import Tools.Search.Binary;
import Tools.Sort.Mergesort;
import Tricks.fTricks;
import Wordle.Backend.weightClasses.weightedWord;
import Wordle.Backend.weightClasses.wordScale;

public class fileInteraction 
{
    public static String[] getGameWords () {
        return fTricks.readFile("/Users/arichapin/Code Projects/Fun_Stuff/src/Wordle/texts/gameWords.txt").split(", ");
    }
    public static String[] getAllWords () {
        return fTricks.readFile("/Users/arichapin/Code Projects/Fun_Stuff/src/Wordle/texts/allWords.txt").split(", ");
    }
    public static ArrayList<weightedWord> getGameWeights () {
        String[] lines = fTricks.readFile("/Users/arichapin/Code Projects/Fun_Stuff/src/Wordle/texts/gameWeights.txt").split("\n");

        ArrayList<weightedWord> weightedWords = new ArrayList<weightedWord>();
        for (String line : lines) 
        {
            weightedWords.add(new weightedWord(
                    line.substring(line.indexOf(" ") + 1), // word
                    Integer.parseInt(line.substring(0, line.indexOf(" "))) // wieght
                ));
        }
        return weightedWords;
    }

    public static void writeGameWeightToFile ()
    {
        ArrayList<weightedWord> weightedWordList = wordScale.getGameWordWeights();
        
        String outputText = "";
        for (weightedWord ww : weightedWordList)
            outputText += ww.getValue() + " " + ww.getWord() + "\n";

        String weightFile = "/Users/arichapin/Code Projects/Fun_Stuff/src/Wordle/texts/gameWeights.txt";
        Tricks.fTricks.writeFile(weightFile, outputText);
    }
    public static void writeAllWeightToFile () // based on gameWord frequency
    {
        ArrayList<weightedWord> weightedWordList = wordScale.getAllWordWeights();
        
        String outputText = "";
        for (weightedWord ww : weightedWordList)
            outputText += ww.getValue() + " " + ww.getWord() + "\n";

        String weightFile = "/Users/arichapin/Code Projects/Fun_Stuff/src/Wordle/texts/allWeights.txt";
        Tricks.fTricks.writeFile(weightFile, outputText);
    }

    @SuppressWarnings("all") // for the unclosed Scanner
    public static void addToList (String word, String fileName)
    {
        String filePath = "/Users/arichapin/Code Projects/Fun_Stuff/src/Wordle/texts/" + fileName + ".txt";
        Scanner in = new Scanner(System.in);

        switch (fileName)
        {
            case "allWords":
                System.out.println(word + " - are you sure you wanted to add this word?");

                ArrayList<String> yeses = new ArrayList<>(Arrays.asList("yes", "Yes", "Y", "y"));
                ArrayList<String> nos = new ArrayList<>(Arrays.asList("no", "No", "N", "n"));
                String answer = in.nextLine();

                while (!yeses.contains(answer)) 
                {
                    if (nos.contains(answer)) {
                        System.out.println("Ok, this word will not be added to the master list.");
                        return;
                    } else {
                        System.out.println("Did not understand input. Please enter \'yes\' or \'no\'.");
                        answer = in.nextLine();
                    }
                }
                break; // do what is after the switch
            case "gameWords":
                if (indexOf(word, "allWords") == -1)
                    throw new IllegalArgumentException("the word " + word + " is not a playable 5 letter word");
                break; // do what is after the switch
            default:
                throw new IllegalArgumentException("the argument fileName " + fileName + " is not a valid txt file with words");
        }

        in.close();
        fTricks.writeFile(filePath, fTricks.readFile(filePath) + ", " + word);
        sortWords(fileName);
    }
    public static void sortWords (String fileName) 
    {
        String filePath = "/Users/arichapin/Code Projects/Fun_Stuff/src/Wordle/texts/" + fileName + ".txt";
        String[] words = fTricks.readFile(filePath).split(", ");

        int x = 1;
        if (x == 1)
            throw new IllegalAccessError("Found problems with merge sort, fix before using this method");
        new Mergesort().sort(words);

        String allWrds = Arrays.toString(words);
        fTricks.writeFile(filePath, allWrds.substring(1, allWrds.length() - 1));
    }

    public static int indexOf (String word, String fileName) 
    {
        String[] words;
        
        if (fileName.equals("allWords"))
            words = fileInteraction.getAllWords();
        else if (fileName.equals("gameWords"))
            words = fileInteraction.getGameWords();
        else
            throw new IllegalArgumentException("the file " + fileName + " is not a readable word list");

        return Binary.search(words, word);
    }
}
