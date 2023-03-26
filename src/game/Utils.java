package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.io.File;
import java.sql.Timestamp;
import java.io.FileWriter;

public class Utils {

    public static void createFile(String text) throws Exception
    {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String file = "files/" + timestamp.getTime() + ".txt";
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.close();
            System.out.println("Game file saved.");
        } catch (Exception e) {
            throw new Exception("Error saving file." + e);
        }
    }



    public static int[] parseBoardDimentions(String guess, String delim, boolean isDim) throws Exception
    {
        // Confirm the correct delimitor was used 
        if(!guess.contains(delim)){
            throw new Exception("Incorrect delimitor used.");
        }

        String[] g = guess.split(delim);
        int[] arr = Stream.of(g).mapToInt(Integer::parseInt).toArray();

        if(isDim)
        {
                if((arr[0] * arr[1]) % 2 != 0)
                {
                    throw new Exception(g + "Board dimentions must result in an even number of tiles.");
                }
        }
        return arr;
    }

}
