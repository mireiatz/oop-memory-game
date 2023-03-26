package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class Main {
    static String userName;
    static int width;
    static int height;
    static ArrayList<List<String>> displayBoard;
    static ArrayList<List<String>> masterBoard;

    static int attempts;

    public static void main(String[] args) throws Exception {
        Thread.sleep(1000);
        setupGame();
        attempts = 0;
        displayBoard = createGameBoard(width,height,true);
        masterBoard = createGameBoard(width,height,false);
        ArrayList<List<String>> allGuesses = new ArrayList<List<String>>();
        printToTerminal("Printing master board for debugging purposes:");
        printBoard(masterBoard);
        boolean cm = false;
        while(!cm)
        {
            if(!win()) {
                System.out.println("Board configuration:");
                printBoard(displayBoard);

                //locations
                ArrayList<String> guesses = new ArrayList<String>();
                Scanner myObj = new Scanner(System.in);
                for (int i = 0; i < 2; i++) {
                    System.out.println("Enter guess row,column (e.g. 1,2)");
                    String guess = myObj.nextLine();
                    guesses.add(guess);
                }
                allGuesses.add(guesses);

                //print locations up
                checkAnswer(guesses);
            }else{
                //check if higher score and save

                //save game details
                String text = height + "x" + width + "\n" + masterBoard + "\n" + allGuesses;
                Utils.createFile(text);
                System.exit(0);
            }
        }
    }


    public static void setupGame() throws Exception {
        Scanner myObj = new Scanner(System.in); // Create a Scanner object
        printToTerminal("Enter board dimensions for even number of tiles. Sample entry: 4x4");
        String d = myObj.nextLine();
        int[] wh = Utils.parseBoardDimentions(d,"x",true);
        width = wh[1];
        height = wh[0];
    }


    public static ArrayList<List<String>> createGameBoard(int width, int height,Boolean isDisplay)
    {
        ArrayList<List<String>> board = new ArrayList<List<String>>(height);

        List<String> allSymbols = Arrays.asList("!", "*", "$", "+", "-", "·", "%", "&", "/", "(", ")", "=", "¡", "?", "¿", "<", ">", "^", "{", "[", "]", "}", "¨", ";", ":", ".", "@", "_", "#", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
        List<String> selectedSymbolPairs = new ArrayList<String>();

        //add symbol pairs to array
        while(selectedSymbolPairs.size() < (height*width)){
            int randomNumber = (int) (Math.random()*(allSymbols.size()-0)) + 0;
            if(!selectedSymbolPairs.contains(allSymbols.get(randomNumber))) {
                selectedSymbolPairs.add(allSymbols.get(randomNumber));
                selectedSymbolPairs.add(allSymbols.get(randomNumber));
            }
        }
        //shuffle pairs
        Collections.shuffle(selectedSymbolPairs);

        //counter to get symbols from shuffled array
        int cont = 0;
        for(int i=0; i<height; i++)
        {
            List<String> tmp = new ArrayList<String>();
            for(int j=0; j<width; j++)
            {
                if(isDisplay)
                {
                    tmp.add("X");
                }
                else
                {
                    tmp.add(selectedSymbolPairs.get(cont));
                }
                cont++;
            }
            board.add(tmp);
        }
        return board;
    }


    public static void printBoard(ArrayList<List<String>> board)
    {
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                System.out.print(board.get(i).get(j));
            }
            System.out.print('\n');
        }
    }

    public static void checkAnswer(ArrayList<String> guesses) {
            attempts ++;
            String[] guess1 = guesses.get(0).split(",");
            String[] guess2 = guesses.get(1).split(",");
            String symbol1 = "";
            String symbol2 = "";

            //get symbols i locations
            for (int i = 0; i < height; i++){
                for (int j = 0; j < width; j++){
                    if(Integer.parseInt(guess1[0])-1 == i && Integer.parseInt(guess1[1])-1 == j){
                        symbol1 = masterBoard.get(i).get(j);
                    }else if(Integer.parseInt(guess2[0])-1 == i && Integer.parseInt(guess2[1])-1 == j){
                        symbol2 = masterBoard.get(i).get(j);
                    }
                    if(symbol1 != "" && symbol2 != ""){
                        break;
                    }
                }
            }

            //update board if match
            if(symbol1 == symbol2){
                System.out.print("MATCH\n");
                ArrayList<List<String>> board = new ArrayList<List<String>>(height);
                for (int i = 0; i < height; i++){
                    List<String> row = new ArrayList<String>();
                    for (int j = 0; j < width; j++) {
                        if ((Integer.parseInt(guess1[0])-1 == i && Integer.parseInt(guess1[1])-1 == j) || (Integer.parseInt(guess2[0])-1 == i && Integer.parseInt(guess2[1])-1 == j)) {
                            row.add(masterBoard.get(i).get(j));
                        } else {
                            row.add(displayBoard.get(i).get(j));
                        }
                    }
                    board.add(row);
                }
                displayBoard = board;
            }else{
                System.out.print("FAIL\n");
            }

    }

    public static boolean win(){
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if(displayBoard.get(i).get(j) == "X"){
                   return false;
                }
            }
        }
        return true;
    }


    public static void resetDisplayBoard()
    {
        displayBoard = createGameBoard(4,4,true);
    }


    public static void printToTerminal(String str)
    {
        System.out.print("\r" + str  + "\n");
    }

}