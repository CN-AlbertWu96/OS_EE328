/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author AlbertWu
 */

class OlympicFrame extends JFrame {
    private int[] x; //The x-coordinates of the upper left corner of the circles
    private int[] y; //The y-coordinates of the upper left corner of the circles
    private int d; //Diameter
    private Color[] color; //Color
    
    public OlympicFrame() {
        this.x = new int[]{130, 202, 274, 166, 238};
        this.y = new int[]{208, 208, 208, 270, 270};
        this.d = 80;
        this.color = new Color[]{Color.blue, Color.black, Color.red, Color.yellow, Color.green};
	this.setTitle("Olympic"); //The name of the window
        this.setSize(500,500); //The size of the window
        this.setDefaultCloseOperation(OlympicFrame.EXIT_ON_CLOSE); //For exit
    }
    
    @Override
    public void paint(Graphics g) {
        Font font = new Font("楷体", Font.PLAIN, 60); //Set the font of the String "USA"
        BasicStroke bs = new BasicStroke(3.0f); //Overstriking
        ((Graphics2D)g).setStroke(bs); //Turn on the overstriking
        
        for(int i=0; i<5; i++) { //Draw the circle with different colors
            ((Graphics2D)g).setColor(color[i]);
            ((Graphics2D)g).drawOval(x[i], y[i], d, d);
        }
        
        g.setColor(Color.black); //Set the color of the String "USA"
        g.setFont(font); //Turn on the font of the String "USA"
        g.drawString("天   朝", 125, 190); //Also the coordinates is its upper left corner
    }
}

public class project5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        OlympicFrame f = new OlympicFrame();
	f.setVisible(true); //Display
    }
}
