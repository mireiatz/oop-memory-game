package game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Utils {

    // create a file given a file name and location
    public static void createFile(String fileName, String fileLocation) {
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


    // overwrite to a given file based in a given location
    public static void writeToFile(String fileName, String text) throws IOException {
        FileWriter fileWriter = new FileWriter("files/" + fileName);
        fileWriter.write(text);
        fileWriter.close();
    }


    // delete a specific file in a specific location
    public static void removeFile(String fileName, String fileLocation) {
        String fp = fileLocation + "/" + fileName;

        try {
            Files.deleteIfExists(Paths.get(fp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // read a given file in a given location and return each line in the file as list of strings
    public static List<String> readFile(String fileName) {
        List<String> info = new ArrayList<String>();

        String fp = "files/" + fileName;
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


    // pPrints to terminal a given message and read in the user's input
    public static String getPlayerInput(String text, String type) {
        boolean rightFormat = false;
        String input = "";
        while (!rightFormat) {
            //print text passed as parameter
            System.out.println(text);

            //read user input with a scanner object
            Scanner myObj = new Scanner(System.in);
            input = myObj.nextLine();  // Read user input

            //verify user input depending on input type
            if (type.equals("Guess"))
                rightFormat = verifyGuessFormat(input);

            if (type.equals("Dimensions"))
                rightFormat = verifyDimensionsFormat(input);

            if (type.equals("Replay")) {
                rightFormat = verifyReplayFormat(input);
            }
        }
        return input;
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
            //check that dimensions result in even number of tiles as per game requirements
            if (width * height % 2 != 0) {
                System.out.println("Board dimensions must result in an even number of tiles.");
                return false;
            } else if (width * height > 60) { //check that board is not larger than 60 tiles as there are only 30 symbols available
                System.out.println("Board must result in a maximum of 60 tiles.");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Invalid board size.");
            return false;
        }
    }


    //verify the format of response to whether user wants to replay last game or not
    public static boolean verifyReplayFormat(String input) {
        String response = "Yes|yes|No|no|Y|y|n|N";
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
    public static int[] parseDimensions(String input) throws Exception {
        //turn input string to an array of integers
        String[] inputArray = input.split("x");
        try {
            int[] parsedInput = Stream.of(inputArray).mapToInt(Integer::parseInt).toArray();
            return parsedInput;
        } catch (Exception e) {
            System.out.println("Error parsing dimensions.");
            int[] emptyDimensions = new int[0];
            return emptyDimensions;
        }
    }


    //parse location pairs guessed by user
    public static int[] parseGuesses(String input) {
        //turn input string to an array of integers
        String[] pairs = input.replaceAll("[(]", "").replaceAll("[)]", "").replace(" ", ",").split(",");
        int[] parsedGuesses = Arrays.stream(pairs).mapToInt(Integer::parseInt).toArray();
        return parsedGuesses;
    }


    // clear console to a point where the previous guesses cant be seen without scrolling
    public static void clearConsole() {
        for (int clear = 0; clear < 200; clear++) {
            System.out.println("\b");
        }
    }


    //get lines from replay file
    public static String getReplayInfo(int line) {
        List<String> lastGame = Utils.readFile("lastgame.txt");
        return lastGame.get(line);
    }

}