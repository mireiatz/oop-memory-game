package game;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Board {
    public int width;
    public int height;
    public boolean isReplay;
    private static ArrayList<List<String>> board; //face up board configuration
    private static ArrayList<List<String>> displayBoard; //face down board configuration to keep track of matched guesses
    public int attempts;

    private ArrayList<String> allGuesses;
    

    public Board(int height, int width, boolean isReplay) {
        this.width = width;
        this.height = height;
        this.isReplay = isReplay;
        board = createBoard("up");
        displayBoard = createBoard("down");
        this.allGuesses = new ArrayList<>();
    }


    //create face up and down boards in play initiation
    public ArrayList<List<String>> createBoard(String type)
    {
        ArrayList<List<String>> board = new ArrayList<>(height);
        List<String> selectedSymbolPairs = new ArrayList<>();

        if(isReplay)
        {
            String[] replayBoard = Utils.getReplayInfo("lastgame.txt", 1).replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "").split(",");
            Collections.addAll(selectedSymbolPairs, replayBoard);
        }
        else
        {
            //list of available symbols and list of symbols that will be selected based on board size
            List<String> allSymbols = Arrays.asList("!", "*", "$", "+", "-", "·", "%", "&", "/", "(", ")", "=", "~", "|", "¡", "?", "¿", "<", ">", "^", "{", "}", "¨", ";", ":", ".", "@", "_", "#", "€");

            //add random symbol pairs to list
            while(selectedSymbolPairs.size() < (height*width)){
                int randomNumber = (int) (Math.random() * allSymbols.size());
                if(!selectedSymbolPairs.contains(allSymbols.get(randomNumber))) {
                    selectedSymbolPairs.add(allSymbols.get(randomNumber));
                    selectedSymbolPairs.add(allSymbols.get(randomNumber));
                }
            }
            //shuffle pairs
            Collections.shuffle(selectedSymbolPairs);
        }
        //counter to get symbols from shuffled list
        int cont = 0;

        //add elements to board
        for(int i=0; i < height; i++)
        {
            List<String> rows = new ArrayList<>();

            for(int j=0; j < width; j++)
            {
                //add all X for down facing board
                if(Objects.equals(type, "down")){
                    rows.add("X");
                }else {
                    //add symbols for face up board
                    rows.add(selectedSymbolPairs.get(cont));
                }
                cont++;
            }
            board.add(rows);
        }
        return board;
    }


    //check if all cards are up
    public boolean win(){
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                //if any card is down the game continues
                if(Objects.equals(displayBoard.get(i).get(j), "X")){
                    return false;
                }
            }
        }
        return true;
    }


    //game playing loop
    public void play() throws Exception
    {
        int guessLine = 2;
        printBoard(board);

        //continue as long as game has not been won
        while(!win()) {
            //print board configuration so far
            System.out.println("Board configuration:");
            printBoard(displayBoard);
            System.out.print("\n");
            String guess;

            //get guesses and parse them
            if(isReplay)
            {
                guess = Utils.getReplayInfo("lastgame.txt", guessLine);
                System.out.println("Guessed pair:");
                System.out.println(guess);
                guessLine++;
            }else{
                guess = Utils.getPlayerInput("Enter pair to reveal (row,column) (row,column) (e.g. (1,2) (2,2)):", "Guess");
            }
            int[] locations = Utils.parseGuess(guess);

            //save guesses for replay file
            allGuesses.add(guess);

            //if the guessed locations are within the board size check if corresponding cards match
            if(checkBounds(locations)){
                String result;
                if(checkAnswer(locations)){
                    result = "MATCH\n\n";
                }else{
                    result = "FAIL\n\n";
                }
                if(!isReplay){
                    Utils.clearConsole();
                }else{
                    System.out.print("\n");
                }
                System.out.print(result);
            }
        }
        endGame();
    }


    //check if the input exceeds the boundaries of a given board
    public boolean checkBounds(int[] locations)
    {
        if(locations[1] > width || locations[0] > height || locations[3] > width || locations[2] > height)
        {
            System.out.println("The locations provided are outside the bounds of the board.");
            return false;
        }
        return true;
    }


    //check if a valid user input matches
    public boolean checkAnswer(int[] guesses) throws Exception  {
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
                if(!Objects.equals(symbol1, "") && !Objects.equals(symbol2, "")){
                    break;
                }
            }
        }

        //configure new board with guessed cards up
        ArrayList<List<String>> newBoard = new ArrayList<>(height);
        for (int i = 0; i < height; i++){
            List<String> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                if ((guesses[0]-1 == i && guesses[1]-1 == j) || (guesses[2]-1 == i && guesses[3]-1 == j)) {
                    row.add(board.get(i).get(j));
                } else {
                    row.add(displayBoard.get(i).get(j));
                }
            }
            newBoard.add(row);
        }

        //if symbols match update the face down board
        if(symbol1.equals(symbol2)){
            displayBoard = newBoard;
            return true;
        }else{
            //if symbols do not match print board with guessed cards up
            printBoard(newBoard);
            if(!isReplay){
                TimeUnit.SECONDS.sleep(3);
            }
            return false;
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


    //save game into replay file
    public void updateLastGame() {
        String info = height + "x" + width + "\n";
        info += displayBoard + "\n";

        for(String guesses : allGuesses){
            info += guesses + "\n";
        }

        Utils.writeFile("lastgame.txt", info);
    }


    //update the leaderboard file with this play's results
    public void updateLeaderboard() {
        //read leaderboard file
        List<String> leaderboard = Utils.readFile("leaderboard.txt");
        String newLeaderboard = "";
        boolean added = false;

        //update board
        if(leaderboard.size() > 0){
            for (String line : leaderboard){
                if(!Objects.equals(line, "") && line.contains(",")){
                    String[] parts = line.split(",");
                    String[] d = parts[1].split("x");
                    int h = Integer.parseInt(d[0]);
                    int w = Integer.parseInt(d[1]);
                    //sort leaderboard - ascending amount of attempts and board difficulty (boards with most cards first)
                    if(attempts < Integer.parseInt(parts[0])){
                        if(!added){
                            newLeaderboard += attempts + "," + height + "x" + width + "\n";
                            added = true;
                        }
                        newLeaderboard += line + "\n";
                    }else if(attempts == Integer.parseInt(parts[0])){
                        if(height * width > h * w) {
                            if(!added){
                                newLeaderboard += attempts + "," + height + "x" + width + "\n";
                                added = true;
                            }
                            newLeaderboard += line + "\n";
                        }else{
                            newLeaderboard += line + "\n";
                            if(!added){
                                newLeaderboard += attempts + "," + height + "x" + width + "\n";
                                added = true;
                            }
                        }
                    }else{
                        newLeaderboard += line + "\n";
                    }
                }
            }
        }
        if(!added){
            newLeaderboard += attempts + "," + height + "x" + width + "\n";
        }

        //upload only 5 highest scores
        String[] lines = newLeaderboard.split("\n");
        String highestScores = "";
        int cont = 1;
        for(String line : lines){
            if(cont <= 5){
                highestScores += line + "\n";
                cont++;
            }
        }

        //overwrite new leaderboard on file
        Utils.writeFile("leaderboard.txt", highestScores);
    }


    //print lines in leaderboard file
    public void printLeaderboard() {
        List<String> leaderboard = Utils.readFile("leaderboard.txt");
        int cont = 1;

        //print only first 5 lines
        System.out.print("Top 5 High Scores:\n");
        for (String line : leaderboard){
            if(cont <= 5){
                if(!Objects.equals(line, "") && line.contains(",")) {
                    String[] parts = line.split(",");
                    System.out.print(cont + ". " + parts[0] + " flips on " + parts[1] + " board\n");
                    cont++;
                }
            }
        }
    }

    public void endGame() {
        //print the final board configuration
        System.out.println("Board configuration:");
        printBoard(displayBoard);
        System.out.println("YOU WIN");
        System.out.print("\n");

        if(!isReplay){
            //update and print the 5 high scores board
            updateLeaderboard();
            printLeaderboard();

            //update the last game file
            updateLastGame();
        }

        //end game
        System.exit(0);
    }
}
