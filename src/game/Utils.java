package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Utils {

    //overwrite a file
    public static void writeFile(String filename, String text) {
        try {
            FileWriter fileWriter = new FileWriter("files/" + filename);
            fileWriter.write(text);
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred when writing into the file " + filename);
            e.printStackTrace();
        }
    }


    //read a file
    public static List<String> readFile(String filename) {
        List<String> info = new ArrayList<>();

        try {
            //read lines in file and return a list of strings
            File file = new File("files/" + filename);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                info.add(data);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred when reading the file " + filename);
            e.printStackTrace();
        }

        return info;
    }


    //print a given message and read the user's input
    public static String getPlayerInput(String text, String type) {
        boolean rightFormat = false;
        String input = "";
        while (!rightFormat) {
            //print text passed as parameter
            System.out.println(text);

            //read user input with a scanner object
            Scanner myObj = new Scanner(System.in);
            input = myObj.nextLine();

            //verify user input depending on input type
            if (type.equals("Dimensions")){
                rightFormat = verifyDimensionsFormat(input);
            }

            if (type.equals("Guess")){
                rightFormat = verifyGuessFormat(input);
            }

            if (type.equals("Replay")) {
                rightFormat = verifyReplayFormat(input);
            }
        }
        return input;
    }


    //verify the format of board dimensions
    public static boolean verifyDimensionsFormat(String input) {
        //check the format of provided board dimensions
        if (!input.matches("[0-9]+x[0-9]+")) {
            System.out.println("Invalid format provided for width and height.");
            return false;
        }
        try {
            String[] dimensions = input.split("x");
            int height = Integer.parseInt(dimensions[0]);
            int width = Integer.parseInt(dimensions[1]);
            //check that dimensions result in even number of cards as per game requirements
            if (width * height % 2 != 0) {
                System.out.println("Board dimensions must result in an even number of cards.");
                return false;
            } else if (width * height > 60) { //check that board is not larger than 60 cards as there are only 30 symbols available
                System.out.println("Board must result in a maximum of 60 cards.");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Invalid board size.");
            return false;
        }
    }


    //verify the format of a user's guess
    public static boolean verifyGuessFormat(String guess) {
        //check that guessed location pair format is correct, valid format: (row,column) (row,column)
        if (!guess.matches("[(][0-9]+,[0-9][)] [(][0-9]+,[0-9][)]")) {
            System.out.println("Invalid format provided for guess. Must be (row,column) (row,column).");
            return false;
        }

        try {
            //check that locations can be parsed to integer
            String[] pairs = guess.replaceAll("[(]", "").replaceAll("[)]", "").split(" ");
            String locations1 = pairs[0];
            String locations2 = pairs[1];

            String[] location1parts = locations1.split(",");
            Integer.parseInt(location1parts[0]);
            Integer.parseInt(location1parts[1]);

            String[] location2parts = locations2.split(",");
            Integer.parseInt(location2parts[0]);
            Integer.parseInt(location2parts[1]);
            return true;

        } catch (Exception e) {
            System.out.println("Invalid format provided for guess. Must be (row,column) (row,column).");
            return false;
        }
    }


    //verify the format of response to whether user wants to replay last game or not
    public static boolean verifyReplayFormat(String input) {
        String response = "Yes|yes|No|no|Y|y|N|n";
        try {
            if (input.matches(response)) {
                return true;
            } else {
                System.out.println("Invalid entry must be either Yes or No.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Invalid entry must be either Yes or No.");
            return false;
        }
    }


    //parse dimensions provided by user
    public static int[] parseDimensions(String input) {
        //turn input string to an array of integers
        String[] inputArray = input.split("x");
        try {
            return Stream.of(inputArray).mapToInt(Integer::parseInt).toArray();
        } catch (Exception e) {
            System.out.println("Error parsing dimensions.");
            return new int[0];
        }
    }


    //parse location pairs guessed by user
    public static int[] parseGuess(String input) {
        //turn input string to an array of integers
        String[] pairs = input.replaceAll("[(]", "").replaceAll("[)]", "").replace(" ", ",").split(",");
        return Arrays.stream(pairs).mapToInt(Integer::parseInt).toArray();
    }


    //clear console to a point where the previous guesses cant be seen without scrolling
    public static void clearConsole() {
        for (int clear = 0; clear < 200; clear++) {
            System.out.println("\b");
        }
    }


    //get lines from replay file
    public static String getReplayInfo(String filename, int line) {
        List<String> lastGame = Utils.readFile(filename);
        return lastGame.get(line);
    }

}