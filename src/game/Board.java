package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    public int width;
    public int height;
    public boolean isDisplayBoard;
    public boolean isReplay;
    public int attempts;
    private static ArrayList<List<String>> board;
    List<String> userInput = new ArrayList<String>();
    

    public Board(int width, int height, boolean isDisplayBoard, boolean isReplay) throws IOException {
        this.width = width;
        this.height = height;
        this.isDisplayBoard = isDisplayBoard;
        this.isReplay = isReplay;
        board = new ArrayList<List<String>>(height);
        if(!isDisplayBoard)
        {
            userInput.add("width:" + width);
            userInput.add("height:" + height);
        }
        if(!isReplay)
        {
            Utils.removeFile("latestAttempt.txt", "Tracking");
            Utils.createFile("latestAttempt.txt", "Tracking");
            Utils.createFile("leaderboard.txt", "Results");
        }
    }


    // Creates board based on height and width set by the constructor
    public void createBoard()
    {
        if(isReplay)
        {
            board = Utils.getReplayBoard();
        }
        else
        {
            List<String> c = Arrays.asList("!", "*", "$", "+", "-");

            for(int i=0;i<height;i++)
            {
                List<String> tmp = new ArrayList<String>();
                for(int j=0;j<width;j++)
                {
                    if(isDisplayBoard)
                    {
                        tmp.add("X");
                    }
                    else
                    {
                        int randomNumber = (int) (Math.random()*(c.size()-0)) + 0;
                        tmp.add(c.get(randomNumber));
                        // TODO Check to ensure there is atleast 1 unique pair if not update
                    }             
                }
    
                if(!isDisplayBoard)
                    userInput.add("boardCreationRow"+ i + ":" + tmp);
    
                board.add(tmp);
            }
        }

    }


    // Prints to terminal the board containing all of the symbols.
    // Only do this for debugging purposes
    public void printBoard()
    {
        for (List<String> list : board) {
            System.out.println(list);
         }   
    }


    // Prints a board with only the selections the user has selected.
    public void printDisplayBoard(int[] p , int[] p1)
    {
        ArrayList<List<String>> h = new ArrayList<List<String>>(height);
        for(int i=0;i<height;i++)
        {
            List<String> tmp = new ArrayList<String>();
            for(int j=0;j<width;j++)
            {
                if (Arrays.equals(new int[]{i,j}, p)) {
                    String s = (board.get(p[0])).get(p[1]);
                    tmp.add(s); 
                  } else if (Arrays.equals(new int[]{i,j}, p1)) {
                    String s = (board.get(p1[0])).get(p1[1]);
                    tmp.add(s); 
                  } else {
                    tmp.add("X");
                  }          
            }
            h.add(tmp);
        }

        for (List<String> list : h) {
            System.out.println(list);
         } 
    }


    // Checks if a valid user input matches 
    public boolean checkAnswer(int[] g, int[] g1) throws InterruptedException, IOException
    {
        attempts+=1;
        System.out.println("Checking if your selection is correct or incorrect!");
        printDisplayBoard(g, g1);
        String c = board.get(g[0]).get(g[1]);
        String c1 = board.get(g1[0]).get(g1[1]);
        Boolean chk = c.equals(c1);
        Thread.sleep(2000);
        Utils.clearConsole();
        System.out.println("Guess successful: " + chk + ". Characters at the indexes you selected " + c + " and " + c1  + " . Attempts=" + attempts);
        if(chk && !isReplay)
        {
            for (String x : userInput) {
                Utils.writeToFile("latestAttempt.txt", "Tracking", x);
            }
            String lb = String.format("%d flips on a %d X %d board",attempts,width,height);
            Utils.writeToFile("leaderboard.txt", "Results", lb);
            Utils.sortLeaderboard();
        }
        return chk;
    }


    // Method called to play the game. 
    // Will continually ask for user input until a matching pair is selected.
    public void play() throws Exception
    {
        boolean cm = false;
        List<String> p = new ArrayList<>();
        int rc = 0;
        if(isReplay)
        {
            p = Utils.getPlayerGuesses();
        }
            

        while(!cm)
        {
            String g = "";
            if(isReplay)
            {
                g = p.get(rc);
                rc++;
            }
            else
            {
                g = Utils.getPlayersGuess("Please enter your 2 guesses. Guesses start with 0 rather than 1. In the format 0,1:0,2");
                userInput.add("guess-" + g);             
            }

            // Confirm expected format is correct
            if(!Utils.isInputValid(g))
            {
                attempts+=1;
                System.out.println("Invalid input please try again.");
                
            }
            else if(Utils.chkBounds(g, width -1, height -1))
            {
                attempts+=1;
                System.out.println("Input exceeds the boundires of the board.");
            }
            else if(Utils.chkSameVals(g,":"))
            {
                attempts+=1;
                System.out.println("Cant make the same selection");
            }
            else
            {
                String[] g1 = g.split(":");
                List<int[]> tmp = new ArrayList<int[]>();
                for (String s : g1) {
                    int[] a = Utils.parsePlayersInputs(s, ",");
                    tmp.add(a);
                }
                cm = checkAnswer(tmp.get(0), tmp.get(1));
            }
            System.out.println(String.format("You have made %d attempts to solve this game.",attempts));
        }
    }
}
