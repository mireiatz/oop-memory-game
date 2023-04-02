package game;

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


    //determine if the player wants to replay the most recent game
    public static boolean isReplay()
    {
        String input = Utils.getPlayerInput("Replay last game? Y/N",
             "Replay");
        return input.matches("Yes|yes|Y|y");
    }


    //get the dimensions of the board from user input
    public static int[] getBoardDimensions(boolean isReplay) {
        int[] dimensions;
        if(isReplay){
            String dimensionsString = Utils.getReplayInfo("lastgame.txt", 0);
            dimensions = Utils.parseDimensions(dimensionsString);
        }else{
            String input = Utils.getPlayerInput("Enter board dimensions for even number of cards. Sample entry: 4x4", "Dimensions");
            dimensions = Utils.parseDimensions(input);
        }
        return dimensions;
    }
}
