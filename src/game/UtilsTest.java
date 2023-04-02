package game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class UtilsTest {
    static Board testBoard;

    //verifyDimensionsFormat()
   @Test
   public void test1()
   {
       //wrong delimiter
       assertFalse(Utils.verifyDimensionsFormat("2,3"));

       //not numbers
       assertFalse(Utils.verifyDimensionsFormat("ax4"));

       //uneven number of cards
       assertFalse(Utils.verifyDimensionsFormat("3x3"));

       //exceeds maximum number of cards
       assertFalse(Utils.verifyDimensionsFormat("2x60"));

       //right format
       assertTrue(Utils.verifyDimensionsFormat("3x4"));
   }


    //verifyGuessFormat()
    @Test
    public void test2()
    {
        //wrong delimiter between locations
        assertFalse(Utils.verifyGuessFormat("(2,3)(3,4)"));

        //wrong delimiter between locations
        assertFalse(Utils.verifyGuessFormat("(2x3)(3.4)"));

        //no parentheses
        assertFalse(Utils.verifyGuessFormat("2,3 3,4"));
    }


    //parseDimensions()
   @Test
   public void test3()
   {
       assertArrayEquals(Utils.parseDimensions("2x3"), new int[]{2,3});
   }


   //parseGuess()
   @Test
   public void test4()
   {
       assertArrayEquals(Utils.parseGuess("(2,3) (4,3)"), new int[]{2, 3, 4, 3});
   }


   //checkBounds()
   @Test
   public void test5()
   {
       testBoard = new Board(5, 4, false);
       String[] numbers1 = "4,4,8,10".split(",");
       String[] numbers2 = "4,4,2,3".split(",");

       int[] outsideBounds = Stream.of(numbers1).mapToInt(Integer::parseInt).toArray();
       int[] insideBounds = Stream.of(numbers2).mapToInt(Integer::parseInt).toArray();

       //outside of bounds
       assertFalse(testBoard.checkBounds(outsideBounds));

       //inside of bounds
       assertTrue(testBoard.checkBounds(insideBounds));
   }


    //verifyReplayFormat()
   @Test
   public void test6()
   {
       //wrong format
       assertFalse(Utils.verifyReplayFormat(""));

       //right format
       assertTrue(Utils.verifyReplayFormat("Y"));
   }


    //getReplayInfo()
    @Test
    public void test7()
    {
        //dimensions
        Assert.assertEquals("second line", Utils.getReplayInfo("test.txt", 1));
    }


   //getReplayInfo()
   @Test
   public void test8()
   {
       //dimensions
       assertTrue(Utils.verifyDimensionsFormat(Utils.getReplayInfo("lastgame.txt", 0)));
   }


    //getReplayInfo()
    @Test
    public void test9()
    {
        //guesses
        assertTrue(Utils.verifyGuessFormat(Utils.getReplayInfo("lastgame.txt", 2)));
    }

   //read file
   @Test
   public void test10()
   {
       List<String> expected = new ArrayList<>();
       expected.add("first line");
       expected.add("second line");
       expected.add("third line");

       Assert.assertEquals(Utils.readFile("test.txt"), expected);
   }
}
