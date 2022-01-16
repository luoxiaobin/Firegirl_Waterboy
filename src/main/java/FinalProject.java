import java.util.Scanner; 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.PrintWriter; 
  
public class FinalProject{ 
 
    final static int MAX_LINE_LENGTH = 120; 
     
    public static void main(String [] args) throws Exception{ 
        makePanel(); 
        Game game = new Game(); 
        game.setUp(); 
        game.runGameLoop();

        //this is a test
    } 
     
    public static void makePanel() { 
        try { 
            File PanelLayout = new File("./PlatformLayout.dat");
            Scanner input = new Scanner(PanelLayout); 
             
            int rowNum = 0; //each line is assigned a row number 
            PlatformArray[] platforms = new PlatformArray[4]; //all the platform arrays combined in another array
             
            while (input.hasNextLine()) { 
                String stringLine = input.nextLine(); //the entire line is placed into a string 
                char[] arrayLine = new char[MAX_LINE_LENGTH]; //the array which will hold one line 
                for (int i=0; i < stringLine.length(); i++) { 
                    arrayLine[i] = stringLine.charAt(i); //adding the characters in the string to the array 
                } 
                platforms[rowNum].setRowNum(rowNum); 
                platforms[rowNum].setLine(arrayLine); 
                rowNum = rowNum + 1; 
            }      
            for (int row=0; row<platforms.length; row++) { 
                /*for (int column=0; column<MAX_LINE_LENGTH; column++) { 
                    Platform currentPlatform = platforms[row]; 
                    System.out.print(currentPlatform.getLine()[column]); 
                } */
                System.out.println(); 
            }
        }
        catch (Exception e) 
        { 
            System.out.println(e); 
        } 
         
    } 
}