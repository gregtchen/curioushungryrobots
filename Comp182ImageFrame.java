/*
Comp182ImageFrame is a GUI Window with a BufferedImage example for Comp182.

Assume for paths below that CSUN represents some
C:/....../CSUN path on a windows system, or
/....../CSUN path on a unix/linux system

On my system I have this file at the relative location:
CSUN/cs182/packages/Comp182ImageWindow

And in JGrasp I have the following classpath setting.
CLASSPATH = CSUN/cs182/packages

Alternately one could set the environment variable PATH or CLASSPATH
to search CSUN/cs182/packages

Mike Barnes
4/19/2018*/

// comment out the next line to use w/o package
//package Comp182ImageWindow; 

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;

// to not see compiler warnings about serializable class; synchronize
// @SuppressWarnings("serial")  
// commented out as it interfers with javadoc, used serialVersionUID below instead

/**
Comp182ImageWindowFrame uses a Frame and Image to display
screen entities:  Markers, Connectors, and the paths of Bots.
For use by Comp 182 students in assignments.
<p>
Comp182ImageWindowFrame UML class diagram 
<div>
<Img alt="" src="../../UML/Comp182ImageFrame.png">
</div>
@since 5/3/2018
@author Mike Barnes
*/
public class Comp182ImageFrame extends Frame {
	// eliminate warning @ serialVersionUID
	private static final long serialVersionUID = 42L;
   // if you change WIDTH and HEIGHT here also change in Drawable
   static private final int WIDTH = 512, HEIGHT = 512, SCALE = 1;
   static private final Color PATHCOLOR = Color.red;
   private int scale;
   protected int width, height;
   protected int offset;
   protected BufferedImage image; 
   protected int imageCount; // for image file names
   protected ArrayList<Drawable> scene; 
   protected ArrayList<Drawable> paths;  
  

   /**
   Initialize the window's common attributes width and height
   @param w window's width
   @param h window's height
   @param s scale for drawing scene
   @param pathColor color for bot paths
   */
   private void initComp182ImageFrame(int w, int h, int s, Color pathColor) {
      width  = w;
      height = h;
      scale = s;
      offset =  w / 10;
      imageCount = 0;
      image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      setSize(width + 2 * offset, height + 2 * offset);
      // code below handles "X" window close button events.
      addWindowListener ( new WindowAdapter() {
      public void windowClosing (WindowEvent e) {
         System.exit (0); }});
      setVisible(true);
      scene = new ArrayList<Drawable>();
      paths = new ArrayList<Drawable>();
      }
        
   /**
   Default constructor
   */
   public Comp182ImageFrame() {
      super("Comp182ImageFrame");
      initComp182ImageFrame(WIDTH, HEIGHT, SCALE, PATHCOLOR);
      }

   /**
   Constructor with title
   @param title Window's title
   */
   public Comp182ImageFrame(String title) {
      super(title);
      initComp182ImageFrame(WIDTH, HEIGHT, SCALE, PATHCOLOR);
      }

   /**
   Constructor with title, width, and height
   @param title Window's title
   @param width Window's  width
   @param height Window's height
   @param scale for drawing scene
   @param pathColor color for bot paths
   */
   public Comp182ImageFrame(String title, int width, int height, int scale, Color pathColor) {
      super(title);
      initComp182ImageFrame(width, height, scale, pathColor);
      }      
      
   /**
    Add a drawable to ArrayList &gt;scene&lt; 
    @param d drawable to add
   
    Synchronized methods force all threads to stop for the action to occur.  Useful in
    multi-threaded environments (java w/ awt) where you may want to minimize
    non -fatal configure-management exceptions.
   
    You may infrequently encounter thread "AWT-EventQueue-0" related exceptions 
    (eg, pumpEvents, concurrentModification). Ignore these non-fatal exceptions with this
    implementation.
   */
   protected synchronized void addDrawable(Drawable d) { scene.add(d); }    
   

   /**
   paint is automatically called by the java GUI (AWT) whenever the
   windows needs to be "repainted".
   For example: when window is initially setVisible, when restored,
   when uncovered.  
   @param g The AWT system provides the Graphics object argument.
   */   
   public synchronized void paint(Graphics g) {
      g.drawImage(image, 0, 0, width, height, 0, 0, width, height, null);
      // draw "frame
      g.setColor(Color.black);
      g.drawRect(offset, offset, width, height);
      // draw "grids"
      g.drawLine(offset + width / 2, offset, offset + width /2, offset + height);
      g.drawLine(offset, offset + height / 2, offset + width, offset + height / 2); 
      // draw scene
      for(Drawable d : scene) d.draw(g, scale, offset);  
      for(Drawable b : paths) b.draw(g, scale, offset);
      }      

   /**
    Make an image of the current scene and write it to the current directory.
    @param fileName name of png image file
    @param imageTitle informational string to print on image
    Create an image, "paint" the current scene into the image, and save
    the image with an appended image count.  This creates a set of "flip book"
    images of your program while it is running.
    Useful for understanding -- debugging.  
   */      
   protected void saveImage(String fileName, String imageTitle) {
      String imageFile = fileName + "_" + imageCount + ".png";
      imageTitle += ("_" + imageCount); 
      imageCount++;      
      // create temporary BufferedImage to draw Frame's component int
      BufferedImage bi = new BufferedImage(getWidth(), 
         getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = bi.createGraphics();

      
      g2d.setBackground(Color.white);
      g2d.clearRect(0, 0, getWidth(), getHeight());
      paint(g2d);  // draw into image
      // draw the title
      g2d.setColor(Color.black);
      g2d.drawString(imageTitle, 10, offset/2);
      try { // write temporary BufferedImage to File 
         File outputfile = new File(imageFile);
         ImageIO.write(bi, "PNG", outputfile);
         } 
      catch (IOException e) {
         System.out.println("error writing image to " + imageFile);      
         } 
      // System.out.println("saved image " + imageFile); 
      }
            
   }

