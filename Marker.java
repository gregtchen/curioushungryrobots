/*
Marker.java

Mike Barnes
4/19/2018
*/

// comment out the next line to use w/o package
//package Comp182ImageWindow; 

import java.awt.*;

/**
Marker is a class for box or oval scene entities with solid or wireframe surfaces. 

<p>
Marker UML class diagram 
<div>
<Img alt="" src="../../UML/Marker.png">
</div>

@since 5/3/2018
@author G. M. Barnes
*/
public class Marker extends Drawable {

	/** marker's radius */
	private int radius;
   /** marker's label */
   private String label;
   private boolean drawLabel;


/**
Make a Marker
@param x  marker's horizontal screen position
@param y  marker's vertical screen position
@param colorValue Marker's color
@param surface either solid or wireframe
@param shape either box or oval
@param aLabel string for label
*/
	public Marker(int x, int y, Color colorValue, ShapeAttribute surface, ShapeAttribute shape, String aLabel) {
      super( new Point(x, y), colorValue, surface, shape, "Marker");
		radius = 2;
      drawLabel = true;
      label = aLabel;
		}

/**
Make a Marker
@param x  marker's horizontal screen position
@param y  marker's vertical screen position
@param colorValue Marker's color
@param surface either solid or wireframe
@param shape either box or oval
@param ptSize marker's radius
@param aLabel string for label
*/
	public Marker(int x, int y, Color colorValue, ShapeAttribute surface, ShapeAttribute shape,
      int ptSize, String aLabel) {
      super( new Point(x, y), colorValue, surface, shape, "Marker");
		radius = ptSize;
      drawLabel = true;
      label = aLabel;
		}

/**
Make a Marker
@param x  marker's horizontal screen position
@param y  marker's vertical screen position
@param colorValue Marker's color
@param surface either solid or wireframe
@param shape either box or oval
@param ptSize marker's radius
*/
   public Marker(int x, int y, Color colorValue, ShapeAttribute surface, ShapeAttribute shape,
      int ptSize) {
      super( new Point(x, y), colorValue, surface, shape, "Marker");
		radius = ptSize;
      drawLabel = false;
		}
      
/**
Make a Marker
@param pt position for Marker
@param colorValue Marker's color
@param surface either solid or wireframe
@param shape either box or oval
@param ptSize marker's radius
*/      
   public Marker(Point pt, Color colorValue, ShapeAttribute surface, ShapeAttribute shape, int ptSize) {
      super(pt, colorValue, surface, shape, "Marker");
		radius = ptSize;
      drawLabel = false;
		}   
   
       
/**
Make a Marker
@param pt position for Marker
@param colorValue Marker's color
@param surface either solid or wireframe
@param shape either box or oval
@param aLabel string for label
*/
	public Marker(Point pt, Color colorValue, ShapeAttribute surface, ShapeAttribute shape, String aLabel) {
      super(pt, colorValue, surface, shape, "Marker");
		radius = 2;
      drawLabel = true;
      label = aLabel;
		}
      
/**
Make a Marker
@param pt position for Marker
@param colorValue Marker's color
@param surface either solid or wireframe
@param shape either box or oval
@param ptSize marker's radius
@param aLabel string for label
*/
	public Marker(Point pt, Color colorValue, ShapeAttribute surface, ShapeAttribute shape,
      int ptSize, String aLabel) {
      super(pt, colorValue, surface, shape, "Marker");     
		radius = ptSize;
      drawLabel = true;
      label = aLabel;
		}
/**
Make a Marker
@param pt position for Marker
@param colorValue Marker's color
@param shape either box or oval
@param aLabel string for label
*/
	public Marker(Point pt, Color colorValue,  ShapeAttribute shape, String aLabel) {
      super(pt, colorValue, ShapeAttribute.SOLID, shape, "Marker");     
      radius = 2;
      drawLabel = true;
      label = aLabel;
		}	
/**
Make a Marker
@param m  Marker to copy values for new Marker from
*/
	public Marker(Marker m) {
      super( m.getPoint(), m.getColor(), m.getFill(), m.getShape(), "Marker");
		radius = m.getSize();
      drawLabel = m.getDrawLabel();
      label = m.getLabel();
		}
		
/** @return the marker's color */
	public Color getColor() {
		return color; }

/** @return the marker's label */
   public String getLabel() {
      return label; }      
      
   public boolean getDrawLabel() {
      return drawLabel; }
      
/** @return the marker's position */
	public Point getPoint() {
		return point; }
		
/** @return the marker's radius */
	public int getSize() {
		return radius; }		

/** @return the marker's fill  */      
   public ShapeAttribute getFill() { return fill; }
   
/** @return the marker's shape */
   public ShapeAttribute getShape() { return shape; }
				
/** @param aColor Marker's new color */
	public void setColor(Color aColor) { color = aColor; }

/** @param aLabel string for Marker's new drawLabel*/
   public void setLabel(String aLabel) {
      drawLabel = true;
      label = aLabel; }
	
/** @param s size for Marker's radius  */
	public void setSize(int s) { radius = s; }	
	

/** Draw the marker on the simulation's canvas
@param g system's graphic object
@param scale to size the drawing -- usually 1
@param offset margins (whitespace) around drawing 
*/
	public void draw(Graphics g, int scale, int offset) {
		Color tColor;
		tColor = g.getColor();  // save exisiting color
		g.setColor(color);
      Point translated = translateToAWT(point);
      if (fill == ShapeAttribute.SOLID && shape == ShapeAttribute.BOX)
		      g.fillRect( offset + translated.x * scale - radius, 
                        offset + translated.y * scale - radius, 
                        scale * 2 *  radius, scale * 2 * radius);
      else if (fill == ShapeAttribute.SOLID && shape == ShapeAttribute.OVAL)
            g.fillOval( offset + translated.x * scale - radius, 
                        offset + translated.y * scale - radius, 
                        scale * 2 * radius, scale * 2 * radius);
      else if (fill == ShapeAttribute.WIRE && shape == ShapeAttribute.BOX)
 		      g.fillRect( offset + translated.x * scale - radius, 
                        offset + translated.y * scale - radius, 
                        scale * 2 *  radius, scale * 2 * radius);     
      else if (fill == ShapeAttribute.WIRE && shape == ShapeAttribute.OVAL)
            g.fillOval( offset + translated.x * scale - radius, 
                        offset + translated.y * scale - radius, 
                        scale * 2 * radius, scale * 2 * radius);      
      else // shape == dot
            g.drawLine( offset + translated.x * scale, 
                        offset + translated.y * scale, 
                        offset + translated.x * scale, 
                        offset + translated.y * scale);
		g.setColor(Color.black);   // set label color
      if (drawLabel) g.drawString(label, offset + translated.x * scale + radius, 
         offset + translated.y * scale - radius);
      g.setColor(tColor); // restore existing color
		}

	}