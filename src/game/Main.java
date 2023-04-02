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


    // Check to determine if the player wants to replay the most recent attempt
    public static boolean isReplay()
    {
        String input = Utils.getPlayerInput("Do you wish to replay the last attempt. Valid entries Yes or No",
             "Replay");

        if(input.matches("Yes|yes|y")){
            return true;
        }else{
            return false;
        }
    }


    // Get the dimensions of the board from user input
    public static int[] getBoardDimensions(boolean isReplay) throws Exception
    {
      //  if(isReplay){
          //  width = Integer.valueOf(Utils.extractDims("width"));
          //  height = Integer.valueOf(Utils.extractDims("height"));
     //   }else{
            String input = Utils.getPlayerInput("Enter board dimensions for even number of tiles. Sample entry: 4x4", "Dimensions");
            int[] dimensions = Utils.parseDimensions(input);
            return dimensions;
   //     }
    }
}
