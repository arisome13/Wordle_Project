package GitWordle;

import java.util.ArrayList;

import Tools.Sort.Mergesort;

public class wordScale 
{
    // WORD SCALES
    private static int[] weights1 = new int[26];
    private static int[][] weights2 = new int[26][26];
    private static int[][][] weights3 = new int[26][26][26];
    private static int[][][][] weights4 = new int[26][26][26][26];

    // only public method
    public static ArrayList<weightedWord> getOrderedWeightedList (ArrayList<String> relevantWords) 
    {
        // first step, getting the frequency arrays set up for the relevantWords
        for (int i = 1; i <= 4; i++)
            loadFrequency(i, relevantWords);
        
        ArrayList<weightedWord> weightedWords = new ArrayList<>();

        for (String word : relevantWords)
            weightedWords.add(new weightedWord(word, getWordWeight(word)));

        // order and return the weighed word list
        new Mergesort().sort(weightedWords, weightedWord.getValueCmp());
        return weightedWords;
    }
    
    // for a list of words, build the WORD SCALES
    private static void loadFrequency (int strLength, ArrayList<String> words)
    {
        String letSeq;
        for (int i = 0; i < words.size(); i++)
        {
            for (int k = 0; k < words.get(i).length() - (strLength - 1); k++)
            {
                letSeq = words.get(i).substring(k, k + strLength);

                switch (letSeq.length())
                {
                    case 4:
                        addWeightTo(weights4, letSeq.charAt(0), letSeq.charAt(1), letSeq.charAt(2), letSeq.charAt(3));
                        break;
                    case 3:
                        addWeightTo(weights3, letSeq.charAt(0), letSeq.charAt(1), letSeq.charAt(2));
                        break;
                    case 2:
                        addWeightTo(weights2, letSeq.charAt(0), letSeq.charAt(1));
                        break;
                    case 1:
                        addWeightTo(weights1, letSeq.charAt(0));
                        break;
                    default:
                        new Exception("incorrect letter sequence length").printStackTrace();
                }
            }
        }
    }
        private static void addWeightTo (int[][][][] a, char c1, char c2, char c3, char c4)
    {
        addWeightTo(a[c1-97], c2, c3, c4);
    }
        private static void addWeightTo (int[][][] a, char c1, char c2, char c3)
    {
        addWeightTo(a[c1-97], c2, c3);
    }
        private static void addWeightTo (int[][] a, char c1, char c2)
    {
        addWeightTo(a[c1-97], c2);
    }
        private static void addWeightTo (int[] a, char c1)
    {
        a[c1-97]++;
    }

    // request the weight of a certian word
    private static int getWordWeight (String word)
    {
        int weight = 0;
        for (int i = 0; i < word.length(); i++)
            weight += weights1[word.charAt(i)-97] / Math.pow(2, numOfPhrase(word, word.substring(i, i + 1), i) - 1);
        for (int i = 0; i < word.length() - 1; i++)
            weight += weights2[word.charAt(i)-97][word.charAt(i+1)-97] 
                / Math.pow(2, numOfPhrase(word, word.substring(i, i + 2), i) - 1);
        for (int i = 0; i < word.length() - 2; i++)
            weight += weights3[word.charAt(i)-97][word.charAt(i+1)-97][word.charAt(i+2)-97]
                / Math.pow(2, numOfPhrase(word, word.substring(i, i + 3), i) - 1);
        for (int i = 0; i < word.length() - 3; i++)
            weight += weights4[word.charAt(i)-97][word.charAt(i+1)-97][word.charAt(i+2)-97][word.charAt(i+3)-97]
                / Math.pow(2, numOfPhrase(word, word.substring(i, i + 4), i) - 1);
        return weight;
    }
        private static int numOfPhrase (String word, String phrase)
    {
        int count = 0;
        while (word.indexOf(phrase) > -1) {
            word = word.substring(word.indexOf(phrase)+phrase.length());
            count++;
        }
        return count;
    }
        private static int numOfPhrase (String word, String phrase, int maxIndex)
    {
        if (maxIndex > word.length()-1)
            throw new IllegalArgumentException("assert (maxIndex (" + maxIndex + ") <= last index in word) --> failed");
        return numOfPhrase(word.substring(0, maxIndex+1), phrase);
    }

    public static ArrayList<weightedWord> getGameWordWeights() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGameWordWeights'");
    }

    public static ArrayList<weightedWord> getAllWordWeights() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllWordWeights'");
    }
}
