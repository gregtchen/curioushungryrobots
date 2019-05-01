/*
Bot.java

Mike Barnes
4/19/2018
*/

// comment out the next line to use w/o package
//package Comp182ImageWindow; 

import java.awt.*;
import java.util.ArrayList;


/**
Bot is a class for any "player" or "robot" subclass
that will be controlled by a path-finding and/or a moving  algorithm().
<p>
Bot UML class diagram 
<div>
<Img alt="" src="../../UML/Bot.png">
</div>

@since 5/3/2018
@author G. M. Barnes
*/

public class Bot extends Drawable {
   /** bot's path:  a list of Point */
   protected ArrayList<Point> path; 


/** 
Make a Bot
@param pt position of Bot
@param colorValue for bot's color
@param name for Window title
*/

	public Bot(Point pt, Color colorValue, String name) {
      super(pt, colorValue, name);
      path = new ArrayList<Point>();
		}

/** 
@return the Bot's point location. 
*/
    public Point getPoint() { return point; }


/**
Move to a location for the bot relative to current position.
@param offset add the offset point's x, y values to current location.
*/
	public void moveBy(Point offset) {
		point.setLocation(point.x + offset.x, point.y + offset.y);
      path.add(new Point(point));
		}  
      
/**
Move to a location for the bot relative to its current position.
@param xOffset distance for horizontal movement
@param yOffset distance for vertical movement
*/
	public void moveBy(int xOffset, int yOffset) {
		point.setLocation(point.x + xOffset, point.y + yOffset);
      path.add(new Point(point));
		}   

/**
Display string for bot's location
@return printable location string formatted "(x, y)"
*/
   public String locationString() {
      return String.format("(%3d, %3d)", point.x, point.y);   
      }
      
/** Draw the marker on the simulation's canvas 
@param g Window graphics object
@param scale to draw with
@param offset for margins (whitespace)
*/
	public void draw(Graphics g, int scale, int offset) {
      Color tColor;
      Point lineStart, lineStop;
		tColor = g.getColor();  // save exisiting color
      if (path.size() < 2)  return;  // need at least two points for a line
      for(int i = 0; i < path.size() - 1; i++) {
         g.setColor(color);
         lineStart = translateToAWT(path.get(i));
         lineStop  = translateToAWT(path.get(i+1));       
         g.drawLine( offset + lineStart.x * scale, offset + lineStart.y * scale, 
                     offset + lineStop.x * scale, offset + lineStop.y * scale);
         }
      g.setColor(tColor); // restore existing color 
      }              
      
	}