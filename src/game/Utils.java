package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Utils {

    // Creates a file given a file name and location
    public static void createFile(String fileName, String fileLocation)
    {
        String fp = fileLocation + "/" + fileName;
        try {
            File myObj = new File(fp);
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            } else {
              System.out.println(fp + " already exists.");
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }


    // Writes to a given file based in a given location
    public static void writeToFile(String fileName, String fileLocation, String text) throws IOException
    {
        String fp = fileLocation + "/" + fileName;
        FileOutputStream fos = new FileOutputStream(fp, true);
        fos.write((text + "\r\n").getBytes());
        fos.close();     
    }


    // Deletes a specific file in a specific location
    public static void removeFile(String fileName, String fileLocation)
    {
        String fp = fileLocation + "/" + fileName;

        try {
            Files.deleteIfExists(Paths.get(fp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Reads a given file in a given location.
    // Returns each line in the file as list of strings.
    public static List<String> readFile(String fileName, String fileLocation)
    {
        List<String> info = new ArrayList<String>(); 
        
        String fp = fileLocation + "/" + fileName;
        try {
            File myObj = new File(fp);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              info.add(data);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
          
          return info;
    }


    // Takes a guess inputted by a user and parses it into an int[] so it can be compared
    public static int[] parsePlayersInputs(String guess, String delim) throws Exception
    {
        // Confirm the correct delimitor was used 
        if(!guess.contains(delim)){
            throw new Exception("Incorrect delimitor used.");
        }

        String[] g = guess.split(delim);
        int[] arr = Stream.of(g).mapToInt(Integer::parseInt).toArray();

        return arr;
    }


    // Prints to terminal a given message and reads in the users input
    public static String getPlayersGuess(String displayText)
    {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println(displayText);         
        return myObj.nextLine();  
    }


    // Prints to terminal a given message and reads in the users input
    public static String getUserInput(String displayText, String typ)
    {
        boolean cm = false;
        String g = "";
        while(!cm)
        {
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            System.out.println(displayText);
            g = myObj.nextLine();  // Read user input
            if(typ.equals("Guess"))
                cm = verifyGuessFormat(g);

            if(typ.equals("Dim"))
                cm = verifyDimFormat(g);
                
            if(typ.equals("Replay"))
            {
                String rc = "Yes|yes|No|no|Y|y";
                if(g.matches(rc))
                {
                    cm = true;
                }
                else
                {
                    System.out.println("Invalid entry must be either Yes or No");
                }
            }    
        }
        return g;  
    }


    // Verifies that the format a user inputs a guess is a valid format
    // Valid format is 0,1:0,1
    public static boolean verifyGuessFormat(String g)
    {
        if(!g.contains(":") && g.contains(","))
        {
            return false;
        }
        
        try {
            for (String s : g.split(":")) {
                s.split(",");
            }
            return true;
            
        } catch (Exception e) {
            System.out.println("Invalid format provided for guess " + g);
            return false;
        }
    }


    // Verifies that the format of the user input for a guess is in a valid format
    // Format should be either digit,digit:digit,digit
    public static boolean verifyDimFormat(String input)
    {
        boolean chk = input.matches("[0-9]+,[0-9]+");
        if(chk == false)
            System.out.println("Invalid format provided for width and height.");
        return chk;
    }


    // Clears console to a point where the previous guesses cant be seen without scrolling
    public static void clearConsole()
    {
        for(int clear = 0; clear < 200; clear++)
        {
            System.out.println("\b");
        }
    }


    //Verifies if a given string matches a given regex string
    public static boolean isInputValid(String input)
    {
        String c = "[0-9]+,[0-9]+:[0-9]+,[0-9]+";
        return input.matches(c);
    }


    // Checks if a given input has the same values when split
    public static boolean chkSameVals(String input, String delim)
    {
        String[] i = input.split(delim);
        return i[0].matches(i[1]);
    }


    // Checks if the input exceeds the boundies of a given board
    public static boolean chkBounds(String input, int width, int height) throws Exception
    {
        List<Boolean> chk = new ArrayList<>();
        String[] v = input.split(":");
        for (String s : v) {
            int[] pv = parsePlayersInputs(s, ",");
            if(pv[0] >= width || pv[1] >= height)
            {
                chk.add(false);
            }
            else
            {
                chk.add(true);
            }
        }
        return chk.contains(false);
    }


    // Sorts leaderboard is asending order. lowest score is top of the file.
    public static void sortLeaderboard() throws IOException
    {
        List<String> s = Utils.readFile("leaderboard.txt", "Results");
        List<Integer> val = new ArrayList<>();
        List<String> nl = new ArrayList<>();

        for (String str : s) {
            String[] v = str.split(" ");
            val.add(Integer.valueOf(v[0]));
        }
        Collections.sort(val);

        for (int v :val) {
            for (String str : s) {
                if(str.contains(String.valueOf(v)))
                {
                    nl.add(str);
                    break;
                }
            }
        }
        removeFile("leaderboard.txt", "Results");
        createFile("leaderboard.txt", "Results");
        for (String str : nl) {
            writeToFile("leaderboard.txt", "Results", str);
        }
    }


    // Extracts information from the last attempt file in order to recreate the game board in order to replay
    public static ArrayList<List<String>> getReplayBoard()
    {
        List<String> v = readFile("latestAttempt.txt", "Tracking");
        ArrayList<List<String>> nvv = new ArrayList<>();
        for (String s : v) {
            if(s.contains("boardCreationRow"))
            {
                String ns = (s.split(":"))[1];
                ns = ns.substring(1, ns.length() - 1);
                ns = ns.replaceAll("\\s+","");
                nvv.add(Arrays.asList(ns.split(",")));
            }
        }
        return nvv;
    }


    // Reads in a user input and verifies it meets a set of rules
    public static List<String> getPlayerGuesses()
    {
        List<String> v = readFile("latestAttempt.txt", "Tracking");
        List<String> nv = new ArrayList<>();
        for (String s : v) {
            if(s.contains("guess"))
            {
                String[] ns = (s.split("-"));
                if(ns.length == 1)
                {
                    // Handles case where user hits enter button
                    nv.add("");
                }
                else
                {
                    nv.add(ns[1]);
                }
            }
        }
        return nv;
    }


    // Extracts the width or height information of the most recent attempt.
    // Used for the purpose to replay game.
    public static String extractDims(String str)
    {
        List<String> v = readFile("latestAttempt.txt", "Tracking");
        String st = "";
        for (String s : v) {
            if(s.contains(str))
                st = (s.split(":"))[1];
        }
        return st;
    }
}
