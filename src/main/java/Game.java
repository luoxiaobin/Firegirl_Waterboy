import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 

import java.util.Scanner; 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.PrintWriter; 
 
public class Game{ 
    JFrame gameFrame; 
    GamePanel gamePanel;    
    MyKeyListener keyListener;  
    //game objects 
    Jumper jumper; 
    Platform platform; 
    
    final static int MAX_LINE_LENGTH = 120;
    final static int MAX_ROW_LENGTH = 4;
    PlatformArray[] platformsArray = new PlatformArray[MAX_ROW_LENGTH]; //all the platform arrays combined in another array
    Layout[][] layout = new Layout[MAX_ROW_LENGTH][MAX_LINE_LENGTH]; //holds all the platforms (that will be displayed)
    
//------------------------------------------------------------------------------ 
    Game(){ 
        gameFrame = new JFrame("Game Window"); 
        gamePanel = new GamePanel(); 
        keyListener = new MyKeyListener(); 
         
        int jumperW = 20; 
        int jumperH = 20; 
        int jumperX = Const.WIDTH/2; 
        int jumperY = Const.GROUND - jumperH; 
        jumper = new Jumper(jumperX, jumperY, jumperW, jumperH); 
         
        int platformW = 100; 
        int platformH = 20; 
        int platformX = 250; 
        int platformY = 400; 
        platform = new Platform(platformX, platformY, platformW, platformH); 

        makePlatformArrays();
    } 
//------------------------------------------------------------------------------ 
//set up the game platform 
    public void setUp(){ 
        gameFrame.setSize(Const.WIDTH,Const.HEIGHT); 
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        gameFrame.setResizable(false); 
        gamePanel.addKeyListener(keyListener); 
        gameFrame.add(gamePanel);  
        gameFrame.setVisible(true);     
    } 
    public void makePlatformArrays() {
        try { 
            File PanelLayout = new File("./PlatformLayout.dat");
            Scanner input = new Scanner(PanelLayout); 
             
            int rowNum = 0; //each line is assigned a row number 
             
            while (input.hasNextLine()) { 
                String stringLine = input.nextLine(); //the entire line is placed into a string 
                char[] arrayLine = new char[MAX_LINE_LENGTH]; //the array which will hold one line 
                for (int i=0; i < stringLine.length(); i++) { 
                    arrayLine[i] = stringLine.charAt(i); //adding the characters in the string to the array 
                } 
                platformsArray[rowNum].setRowNum(rowNum); 
                platformsArray[rowNum].setLine(arrayLine); 
                rowNum = rowNum + 1; 
            }    
        }
        catch (Exception e) 
        { 
            System.out.println(e); 
        } 
    }
    public void makePlatforms() {
        for (int row=0; row<platformsArray.length; row++) {
            for (int column=0; column<MAX_LINE_LENGTH; column++) { 
                int platformWidth = 0;
                if (Character.toString(platformsArray[row].getLine()[column]) == "_") {
                    Platform currentPlatform = new Platform(column, row, 1, 1);
                    layout[row][column] = currentPlatform;
                }
                else if (Character.toString(platformsArray[row].getLine()[column]) == "x") {
                    GreenGoo currentGreenGoo = new GreenGoo(column, row, 1, 1);
                    layout[row][column] = currentGreenGoo;
                }
                else if (Character.toString(platformsArray[row].getLine()[column]) == "D") {
                    Door currentDoor = new Door(column, row, 1, 1);
                    layout[row][column] = currentDoor;
                }
                else if (Character.toString(platformsArray[row].getLine()[column]) == " ") {
                    
                }
            }
        }
    }
//------------------------------------------------------------------------------   
//    main game loop 
    public void runGameLoop(){ 
        while (true) { 
            gameFrame.repaint(); 
            try  {Thread.sleep(Const.FRAME_PERIOD);} catch(Exception e){} 
 
            jumper.accellerate(); 
            jumper.moveX(); 
            jumper.moveY(Const.GROUND); 
            //if the object is moving down and collides with the platform 
            if (jumper.getVy()>0 && jumper.collides(platform)){ 
                jumper.setY(platform.getY() - jumper.getHeight()); 
                jumper.setVy(0); 
            }  
            //if the object is moving up and collides with the platform 
            else if (jumper.getVy()<0 && jumper.collides(platform)){ 
                jumper.setY(platform.getY() + platform.getHeight()); 
                jumper.setVy(0); 
            } 
        } 
    }   
//------------------------------------------------------------------------------   
    //act upon key events 
    public class MyKeyListener implements KeyListener{    
        public void keyPressed(KeyEvent e){ 
            int key = e.getKeyCode(); 
            if (key == KeyEvent.VK_LEFT){ 
                jumper.setVx(-Const.RUN_SPEED); 
            } else if (key == KeyEvent.VK_RIGHT){ 
                jumper.setVx(Const.RUN_SPEED); 
            } else if (key == KeyEvent.VK_UP && jumper.isOnLevel(Const.GROUND)){ 
                jumper.setVy(Const.JUMP_SPEED); 
            } else{ 
                jumper.setVx(0); //stop if any other key is pressed 
            }              
        } 
        public void keyReleased(KeyEvent e){  
        }    
        public void keyTyped(KeyEvent e){ 
        }            
    }     
//------------------------------------------------------------------------------ 
    //draw everything 
    public class GamePanel extends JPanel{ 
        GamePanel(){ 
            setFocusable(true); 
            requestFocusInWindow(); 
        } 
         
        @Override 
        public void paintComponent(Graphics g){  
            super.paintComponent(g); //required 
            jumper.draw(g); 
            int numRows = layout.length;
            int numColumns = layout[0].length;
            for (int row=0; row<layout.length; row++) { //drawing each row in platforms (an array of platforms)
                for (int column=0; column<numColumns; column++) {
                    layout[row][column].draw(g);
                }
            }
        }     
    }     
}