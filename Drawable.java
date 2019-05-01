//package Comp182ImageWindow;

import java.awt.*;

// Instance variables and methods common to all drawables
/**
 Drawable is an abstract class
 for use by Comp 182 students in assignments.
 *
 All points are converted from origin centered in image to
 awt.Frame's window where origin is image's upper left corner.
<p>
Drawable UML class diagram 
<div>
<Img alt="" src="../../UML/Drawable.png">
</div>

@since 5/3/2018
@author G. M. Barnes
*/
abstract class Drawable{
   // if you change WIDTH and HEIGHT here also change in Comp182ImageFrame
   static private final int WIDTH = 512, HEIGHT = 512;
   protected Color color;
   protected Point point;
   protected ShapeAttribute fill, shape;
   protected String name = null;

/**
Helper method, called by constructor to initialize:
@param aPoint Drawable's location
@param aColor Drawable's color
@param aName Drawable's name
*/
   public void init(Point aPoint, Color aColor, String aName) {
      point = aPoint;
      color =  aColor;
      name = aName;    
      }

/**
Make a Drawable
@param aPoint Drawable's location
@param aColor Drawable's color
@param surface Drawable's fill: SOLID or WIRE
@param shapeType Drawable's geometry:  BOX or OVAL
@param aName Drawable's name
*/

   public Drawable(Point aPoint, Color aColor, ShapeAttribute surface, ShapeAttribute shapeType, String aName){
      init(aPoint, aColor, aName);     
      fill = surface;
      shape = shapeType;
      }
      
/**
Make a Drawable
@param aPoint Drawable's location
@param aColor Drawable's color
@param shapeType Drawable's geometry:  BOX or OVAL
@param aName Drawable's name
*/

   public Drawable(Point aPoint, Color aColor, ShapeAttribute shapeType, String aName){
      init(aPoint, aColor, aName); 
      shape = shapeType;
      }
      
/**
Make a Drawable
@param aPoint Drawable's location
@param aColor Drawable's color
@param aName Drawable's name
*/
     
   public Drawable(Point aPoint, Color aColor, String aName) {
      init(aPoint, aColor, aName);
      }

/**
@return Printable description of Drawable
*/      
   
   public String toString() { return name + " (" + point.x + ", " + point.y + ") "
       + color;}

/**
Simulation locations use a "center origin (0,0)" coordinate system, where the origin is 
in the middle of a +255 to -255 plane.  
Java's AWT graphics uses a coordinate system where (0,0) is the windows upper left corner.  
@param pt "center origin" coordinates
@return the Drawable's point translated from "origin" to  AWT's coordinates.
*/
        
   protected Point translateToAWT(Point pt) {
      return new Point(pt.x + WIDTH/2,  HEIGHT/2 - pt.y); 
      }

/**
Translate an "AWT coordinate" point location to AWT coordinates.
@param pt "AWT" coordinates
@return the Drawable's point translated from "AWT" to "origin" coordinates
*/
      
   protected Point translateToOrigin(Point pt) {
      return new Point(pt.x - WIDTH/2, HEIGHT/2 - pt.y);
      }
      
/**
Abstract method to enable polymorphic "drawing" of all Drawables.
@param g Window's graphic context object
@param scale to apply when drawing
@param offset for images border frame
*/

   public abstract void draw(Graphics g, int scale, int offset);
 
   }