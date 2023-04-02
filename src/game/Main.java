package game;


import java.util.List;

public class Main {
    static Board board;

    public static void main(String[] args) throws Exception {
        //give the option to replay last game
        boolean isReplay = isReplay();

        //set up the game
        int[] dimensions = getBoardDimensions(isReplay);
        board = new Board(dimensions[0], dimensions[1], isReplay);
        board.play();
    }


    // Check to determine if the player wants to replay the most recent attempt
    public static boolean isReplay()
    {
        String input = Utils.getPlayerInput("Replay last game? Y/N",
             "Replay");

        if(input.matches("Yes|yes|Y|y")){
            return true;
        }else{
            return false;
        }
    }


    // get the dimensions of the board from user input
    public static int[] getBoardDimensions(boolean isReplay) throws Exception
    {
        int[] dimensions;
        if(isReplay){
            String dimensionsString = Utils.getReplayInfo(0);
            dimensions = Utils.parseDimensions(dimensionsString);
        }else{
            String input = Utils.getPlayerInput("Enter board dimensions for even number of tiles. Sample entry: 4x4", "Dimensions");
            dimensions = Utils.parseDimensions(input);
        }
        return dimensions;
    }
}
