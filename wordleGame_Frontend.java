package GitWordle;

import javax.swing.*;

import Wordle.Backend.backendW;
import Wordle.Backend.weightClasses.searchW;

import java.awt.*;
import java.awt.event.*;

public class wordleGame_Frontend extends JFrame
{
    private backendW backend;

    private JPanel titleArea;
        private String titleString;
        private JTextField title;
    private JPanel wordGrid;
        private JTextArea[][] guesses;
    private JPanel keyboard;
        private JButton[][] keys;

    private int curRow, curCol;
    
    public wordleGame_Frontend (String word)
    {
        super("Wordle");
        setLayout(new BorderLayout());
        setLocation(200, 100);
        setSize(495, 626);

        backend = new backendW(word);

        loadTitle(); add(titleArea, BorderLayout.NORTH);
        loadGuessDisplay(); add(wordGrid, BorderLayout.CENTER);
        loadKeyboardDisplay(); add(keyboard, BorderLayout.SOUTH);
    }
    public wordleGame_Frontend ()
    {
        this("NULL");
    }

    private void loadTitle ()
    {
        titleArea = new JPanel();
        titleArea.setBackground(Color.blue);
            title = new JTextField();
            title.setEditable(false);
            title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
            title.setBackground(Color.blue);
            title.setForeground(Color.WHITE);
            titleString = "          Personal Wordle Challenge          ";
            title.setText(titleString);
        titleArea.add(title);
    }
    private void loadGuessDisplay ()
    {
        GridLayout g = new GridLayout(6, 5); 
        g.setHgap(20);
        g.setVgap(20);
        wordGrid = new JPanel(g);
        wordGrid.setBackground(Color.BLACK);

        guesses = new JTextArea[6][5];
        for (int r = 0; r < guesses.length; r++)
            for (int c = 0; c < guesses[r].length; c++) {
                JTextArea j = new JTextArea() 
                {
                    public void setText (String text) {
                        super.setText((" " + text));
                    }
                    public String getText () {
                        return super.getText().substring(1);
                    }
                    public void setBackground (Color c) {
                        super.setBackground(c);
                        this.setBorder(BorderFactory.createLineBorder(c, 15));
                    }
                };
                j.setVisible(true);
                j.setBorder(BorderFactory.createLineBorder(Color.white, 15));
                j.setEditable(false);
                j.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));

                guesses[r][c] = j;
            }
        
        for (int r = 0; r < guesses.length; r++)
            for (int c = 0; c < guesses[r].length; c++)
                wordGrid.add(guesses[r][c]);
    }
    private void loadKeyboardDisplay ()
    {
        GridLayout g = new GridLayout();
        g.setColumns(20);
        g.setRows(3);
        keyboard = new JPanel(g);

        // initial place a pressed key would add a letter
        curRow = 0;
        curCol = 0;

        addKeys();
    }
    private void addKeys ()
    {
        String[][] keyNames = {{"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"}, 
                               {"a", "s", "d", "f", "g", "h", "j", "k", "l", "exit"}, 
                               {"ent.", "z", "x", "c", "v", "b", "n", "m", "<-", "rvl"}};
        keys = new JButton[3][0];
        GridBagConstraints c;
        JButton key;
        int row, col;
        for (row = 0; row < keyNames.length; row++)
        {
            keys[row] = new JButton[keyNames[row].length];

            for (col = 0; col < keyNames[row].length; col++)
            {
                String title = keyNames[row][col];
                key = new JButton(title);
                keys[row][col] = key;

                c = new GridBagConstraints();
                c.weightx = 0.1;
                c.gridy = row;

                if (title.equals("ent.")) {
                    key.addActionListener(enterListener());
                } else if (title.equals("<-")) {
                    key.addActionListener(deleteListener());
                } else if (title.equals("exit")) {
                    key.addActionListener(exitListener());
                } else if (title.equals("rvl")) {
                    key.addActionListener(revealListener());
                } else {
                    key.addActionListener(letterListener());
                    key.setText(key.getText().toUpperCase());
                    key.setOpaque(true);
                }
                keyboard.add(key, c);
            }
        }
    }

    private ActionListener enterListener () 
    {
        return new ActionListener() 
        {
            public void actionPerformed (ActionEvent e) 
            {
                if (curCol != 5) {
                    error("must give 5 letters before entering a guess");
                    System.out.println(curCol + " is curCol");
                    return;
                }

                String guess = "";
                for (int i = 0; i < guesses[curRow].length; i++)
                    guess += guesses[curRow][i].getText().toLowerCase();
                
                String toDo = backend.submit(guess);
                //System.out.println("To Do is: " + toDo + " for " + guess);
                
                if (toDo.length() == 0) {
                    updateKeys(backend.getNewestHint());
                    curRow++;
                    curCol = 0;
                } else if (toDo.equals("success")) {
                    updateKeys(backend.getNewestHint());
                    success();
                } else if (toDo.equals("fail")) {
                    updateKeys(backend.getNewestHint());
                    fail();
                } else {
                    error(toDo);
                }
            }
        };
    }
    private ActionListener deleteListener () {
        return new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                if (curCol == 0) {
                    error("       no letters to delete");
                } else {
                    guesses[curRow][--curCol].setText(" ");
                }
            }
        };
    }
    private ActionListener letterListener () {
        return new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                if (curCol == 5) {
                    error("         too many letters");
                } else {
                    guesses[curRow][curCol++].setText(((JButton) e.getSource()).getText());
                }
            }
        };
    }
    private ActionListener exitListener () {
        return new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                exit(1, "                   EXITING");
            }
        };
    }
    private ActionListener revealListener () {
        return new ActionListener() {
            public void actionPerformed (ActionEvent e) 
            {
                // best choice
                tempDisplay(2, "          Your best guess is \'" + searchW.bestGameFit(backend.getHintedAlph()) + "\'.");
                
                // top three
                //String[] likelyWords = searchW.topGameFits(3, backend.getHintedAlph());
                //tempDisplay(2, "          Your best guess is \'" + likelyWords[(int)(likelyWords.length * Math.random())] + "\'.");
            }
        };
    }

    private void error (String message) {
        tempDisplay(3, message);
    }
    private void tempDisplay (int secs, String message) {
        title.setText(message);
        new Timer(secs * 1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) 
                { title.setText(titleString); ((Timer)e.getSource()).stop(); }
        }).start();
    }
    
    private void exit (int secs, String message)
    {
        title.setText(message);
        new Timer(secs * 1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) 
                { System.exit(0);}
        }).start();
    }
    private void success () {
        exit(5, "           Success: " + backend.getWord());
    }
    private void fail () {
        exit(5, "        ran out of guesses");
    }

    private void updateKeys (String hint) 
    {
        for (int i = 0; i < hint.length(); i++) {
            if (hint.substring(i, i+1).equals("O")) {
                setGuessColor(i, Color.GREEN);
            } else if (hint.substring(i, i+1).equals("E")) {
                setGuessColor(i, Color.YELLOW);
            } else {
                setGuessColor(i, Color.LIGHT_GRAY);
            }
        }
    }
    private void setGuessColor (int location, Color c) 
    {
        guesses[curRow][location].setBackground(c);
        setKeyColor(guesses[curRow][location].getText(), c);
    }
    private void setKeyColor (String let, Color color) {
        for (int r = 0; r < keys.length; r++)
            for (int c = 0; c < keys[r].length; c++)
                if (keys[r][c].getText().equals(let)){
                    if (keys[r][c].getBackground().equals(Color.GREEN)) continue;
                    else if (keys[r][c].getBackground().equals(Color.YELLOW))
                        if (!color.equals(Color.GREEN)) continue;
                    keys[r][c].setBackground(color);
                }
    }
}
