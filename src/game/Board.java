package game;

import java.io.IOException;
import java.util.*;

public class Board {
    public int width;
    public int height;
    public boolean isReplay;
    private static ArrayList<List<String>> board; //face up board configuration
    private ArrayList<List<String>> displayBoard; //face down board configuration to keep track of matched guesses
    public int attempts;

    private ArrayList<List<String>> allGuesses;
    List<String> userInput = new ArrayList<String>();
    

    public Board(int width, int height, boolean isReplay) throws IOException {
        this.width = width;
        this.height = height;
        this.isReplay = isReplay;
        this.board = createBoard("up");
        this.displayBoard = createBoard("down");
        if(!isReplay)
        {
           // Utils.removeFile("latestAttempt.txt", "Tracking");
            //Utils.createFile("latestAttempt.txt", "Tracking");
            //Utils.createFile("leaderboard.txt", "Results");
        }
    }


    // create face up and down boards in play initiation
    public ArrayList<List<String>> createBoard(String type)
    {
        ArrayList<List<String>> board = new ArrayList<List<String>>(height);

        if(isReplay)
        {
            board = Utils.getReplayBoard();
        }
        else
        {
            //list of available symbols and list of symbols that will be selected based on board size
            List<String> allSymbols = Arrays.asList("!", "*", "$", "+", "-", "·", "%", "&", "/", "(", ")", "=", "¡", "?", "¿", "<", ">", "^", "{", "[", "]", "}", "¨", ";", ":", ".", "@", "_", "#", "€");
            List<String> selectedSymbolPairs = new ArrayList<String>();

            //add random symbol pairs to list
            while(selectedSymbolPairs.size() < (height*width)){
                int randomNumber = (int) (Math.random()*(allSymbols.size()-0)) + 0;
                if(!selectedSymbolPairs.contains(allSymbols.get(randomNumber))) {
                    selectedSymbolPairs.add(allSymbols.get(randomNumber));
                    selectedSymbolPairs.add(allSymbols.get(randomNumber));
                }
            }
            //shuffle pairs
            Collections.shuffle(selectedSymbolPairs);

            //counter to get symbols from shuffled list
            int cont = 0;

            //add elements to board
            for(int i=0; i<height; i++)
            {
                List<String> rows = new ArrayList<String>();

                for(int j=0; j<width; j++)
                {
                    //add all X for down facing board
                    if(type == "down"){
                        rows.add("X");
                    }else { //add symbols
                        rows.add(selectedSymbolPairs.get(cont));
                    }
                    cont++;
                }
                board.add(rows);
            }
        }
        return board;
    }


    //check if all cards are up
    public boolean win(){
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                // if any card is down the game continues
                if(displayBoard.get(i).get(j) == "X"){
                    return false;
                }
            }
        }
        return true;
    }


    // game playing loop
    public void play() throws Exception
    {
        boolean cm = false;
        List<String> p = new ArrayList<>();
        int rc = 0;
        if(isReplay) {
            p = Utils.getPlayerGuesses();
        }
        printBoard(board);

        while(!cm)
        {
            //continue as long as game has not been won
            if(!win()) {
                String g = "";
                if(isReplay)
                {
                    g = p.get(rc);
                    rc++;
                }

                //print board configuration so far
                System.out.println("Board configuration:");
                printBoard(displayBoard);

                //get guesses and parse
                String guesses = Utils.getPlayerInput("Enter pair to reveal (row,column) (row,column) (e.g. (1,2) (2,2)):", "Guess");
                int[] locations = Utils.parseGuesses(guesses);

                //if the guessed locations are within the board size check if corresponding cards match
                if(checkBounds(locations)){
                    checkAnswer(locations);
                }
            }else{
                //if the game has ended print the final board configuration
                System.out.println("Board configuration:");
                printBoard(displayBoard);
                System.out.println("YOU WIN");

                //update and print the 5 high scores board
                updateLeaderboard();
                printLeaderboard();

                //end game
                System.exit(0);
            }
        }
    }


    // Checks if the input exceeds the boundies of a given board
    public boolean checkBounds(int[] locations)
    {
        if(locations[1] > width || locations[0] > height || locations[3] > width || locations[2] > height)
        {
            System.out.println("The locations provided are outside the bounds of the board.");
            return false;
        }
        return true;
    }


    // check if a valid user input matches
    public void checkAnswer(int[] guesses) throws InterruptedException, IOException
    {
        attempts++;
        String symbol1 = "";
        String symbol2 = "";

        //get symbols in locations
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if(guesses[0]-1 == i && guesses[1]-1 == j){
                    //symbol in first location
                    symbol1 = board.get(i).get(j);
                }else if(guesses[2]-1 == i && guesses[3]-1 == j){
                    //symbol in second location
                    symbol2 = board.get(i).get(j);
                }
                if(symbol1 != "" && symbol2 != ""){
                    break;
                }
            }
        }

        //if symbols match update the face down board
        if(symbol1 == symbol2){
            ArrayList<List<String>> newBoard = new ArrayList<List<String>>(height);
            for (int i = 0; i < height; i++){
                List<String> row = new ArrayList<String>();
                for (int j = 0; j < width; j++) {
                    if ((guesses[0]-1 == i && guesses[1]-1 == j) || (guesses[2]-1 == i && guesses[3]-1 == j)) {
                        row.add(board.get(i).get(j));
                    } else {
                        row.add(displayBoard.get(i).get(j));
                    }
                }
                newBoard.add(row);
            }
            displayBoard = newBoard;
            Utils.clearConsole();
            System.out.print("MATCH\n");
        }else{
            //if symbols do not match continue the game
            Utils.clearConsole();
            System.out.print("FAIL\n");
        }
    }


    //print the board passed as a parameter
    public void printBoard(ArrayList<List<String>> board)
    {
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                System.out.print(board.get(i).get(j));
            }
            System.out.print('\n');
        }
    }


    //update the leaderboard file with this play's results
    public void updateLeaderboard() throws IOException {
        //read leaderboard file
        List<String> leaderboard = Utils.readFile("leaderboard.txt", "results");
        String newLeaderboard = "";
        boolean added = false;

        //update board
        if(leaderboard.size() > 0){
            for (String line : leaderboard){
                    if(line != "" && line.contains(",")){
                        String[] parts = line.split(",");

                        //sort leaderboard
                        if(attempts <= Integer.parseInt(parts[0])){
                            if(!added){
                                newLeaderboard += attempts + "," + height + "x" + width + "\n";
                                added = true;
                            }
                            newLeaderboard += line + "\n";
                        }else{
                            newLeaderboard += line + "\n";
                        }
                    }
            }
        }
        if(!added){
            newLeaderboard += attempts + "," + height + "x" + width + "\n";
        }

        //overwrite new leaderboard on file
        Utils.writeToFile("leaderboard.txt", "results", newLeaderboard, "overwrite");
    }


    //print lines in leaderboard file
    public void printLeaderboard(){
        List<String> leaderboard = Utils.readFile("leaderboard.txt", "results");
        String newLeaderboard = "";
        int cont = 1;

        //print only first 5 lines
        System.out.print("Top 5 High Scores:\n");
        for (String line : leaderboard){
            if(cont <= 5){
                if(line != "" && line.contains(",")) {
                    String[] parts = line.split(",");
                    System.out.print(cont + ". " + parts[0] + " flips on " + parts[1] + " board\n");
                    cont++;
                }
            }
        }
    }
}
