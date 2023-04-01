package game;



public class Main {
    static int width;
    static int height;
    static Board board;

    public static void main(String[] args) throws Exception {
        boolean isReplay = isReplay();
        getBoardDim(isReplay);
        board = new Board(width, height, false, isReplay);
        board.createBoard();
        board.printBoard(); // Only print for debugging remove before commiting
        board.play();
    }


    // Check to determine if the player wants to replay the most recent attempt
    public static boolean isReplay()
    {
        String inp = Utils.getUserInput(
            "Do you wish to replay the last attempt. Valid entries Yes or No",
             "Replay");

        if(inp.matches("Yes|yes|y"))
            return true;
        else
            return false; 
    }


    // Get the dimentions of the board from user input
    public static void getBoardDim(boolean isReplay) throws Exception
    {
        if(isReplay)
        {
            width = Integer.valueOf(Utils.extractDims("width"));
            height = Integer.valueOf(Utils.extractDims("height"));
        }
        else
        {
            String input = Utils.getUserInput("Please enter the width and height of the game board. Must be in the format of `width,height`. E.g. 4,4", "Dim");
            int[] in = Utils.parsePlayersInputs(input, ",");
            width = in[0];
            height = in[1];
        }
    }
}
