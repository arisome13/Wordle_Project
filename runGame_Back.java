package GitWordle;

import java.util.Scanner;

import Wordle.Backend.weightClasses.searchW;

public class runGame_Back 
{
    public static void playWordle () 
    {
        wordleGame_Backend b = new wordleGame_Backend();
        Scanner in = new Scanner(System.in);
        boolean done = false;
        int turnNumber = 1;

        while (!done)
        {
            System.out.println("This is turn #" + turnNumber + "\t\tBest guess is: " + searchW.bestGameFit(b.getHintedAlph()));
            System.out.print("Enter a 5 letter guess: ");
            String answer = in.nextLine();
            answer = answer.toLowerCase();

            String result = b.submit(answer);
            System.out.println("\t\t\t" + b.getNewestHint() + " " + result);
            turnNumber++;
            if (result.substring(result.indexOf(" ") + 1).equals("success")) {
                System.out.println("GOOD JOB! You have successfully guessed the correct answer: " + b.getWord());
                done = true;
            } else if (result.substring(result.indexOf(" ") + 1).equals("fail")) {
                System.out.println("Darn, you'll get it next time. The word was " + b.getWord());
                done = true;
            } else {
                //System.out.println("Not quite. Here is your hint: " + b.getNewestHint());
            }
        }
        in.close();
    }
    public static void main(String[] args) {
        playWordle();
    }
}
