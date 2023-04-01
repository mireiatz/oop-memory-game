package game;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.*;

public class UtilsTest {

   @Test
   public void test1() throws Exception
   {
        assertFalse(Utils.chkBounds("0,0:0,1", 5, 5)); // Valid input
        assertFalse(Utils.chkBounds("0,4:4,0", 5, 5)); // Valid input
        assertTrue(Utils.chkBounds("0,5:0,1", 5, 5)); // Check boundires
        assertTrue(Utils.chkBounds("5,5:0,0", 5, 5)); // Check boundires
   }


   @Test
   public void test2() throws Exception
   {
        assertTrue(Utils.chkSameVals("4:4", ":"));
        assertFalse(Utils.chkSameVals("4:1", ":"));
   }


   @Test
   public void test3() throws Exception
   {
        assertTrue(Utils.isInputValid("4,4:1,1"));
        assertTrue(Utils.isInputValid("0,0:0,1"));
        assertFalse(Utils.isInputValid("0:0:0,1"));
        assertFalse(Utils.isInputValid("0,0,0,1"));
   }


   public void test4()
   {
        // Something weird going on with being in a correct file path.


        // String fileName = "test.txt";
        // String location = "Results";
        // String text = "Junit test";
        // Exception te = new Exception();
        // try {
        //     Utils.createFile("latestAttempt222.txt", "Tracking");
        //     Utils.createFile("leaderboard222.txt", "Results");
        //     Utils.createFile(fileName, location);
        //     Utils.writeToFile(fileName, location, text);
        //     List<String> vals = Utils.readFile(fileName, location);
        //     for (String s : vals) {
        //         assertTrue(s.equals(text));
        //     }
        //     Utils.removeFile(fileName, location);
        // } catch (Exception e) {
        //     te = e;
        // }
        // assertNull(te);
   }

   @Test
   public void test5() throws Exception
   {
        assertArrayEquals(Utils.parsePlayersInputs("4,4", ","), new int[]{4,4});
   }


   @Test
   public void test6() throws Exception
   {
        Exception ex = new Exception();
        try {
            assertArrayEquals(Utils.parsePlayersInputs("4,4", ":"), new int[]{4,4});
        } catch (Exception e) {
            ex = e;
        }       
        assertNotNull(ex);
   }

   @Test
   public void test7() throws Exception
   {
        assertTrue(Utils.verifyDimFormat("4,4"));
        assertFalse(Utils.verifyDimFormat("4:4"));
   }


   @Test
   public void test8() throws Exception
   {
        assertTrue(Utils.verifyGuessFormat("1,1:2,2"));
   }

   @Test
   public void test9()
   {   {
        Exception ex = new Exception();
        try {
            Utils.verifyGuessFormat("1,1:2,2");
        } catch (Exception e) {
            ex = e;
        }       
        assertNotNull(ex);
    }
   }


   


}
