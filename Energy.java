/*
Gregory, Chen
P3 Curious Hungry Robots
Comp 182 Spring 2018
*/
//package Comp182ImageWindow;

import java.awt.Point;

import java.awt.*;

public class Energy extends Drawable{
   private Point position;
   private double energy;
   
   	/** marker's radius */
	private int radius;
   /** marker's label */
   private String label;
   private boolean drawLabel;
   
   
   public Energy(Point position, double energy){
      super(position, Color.green, ShapeAttribute.SOLID, ShapeAttribute.OVAL, "energy");
      this.position = position;
      this.energy = energy;
      drawLabel = true;
      radius =2 ;
      label = "energy";
   }
   
   public void draw(Graphics g, int scale, int offset){
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
      if (drawLabel){ g.drawString(label, offset + translated.x * scale + radius, 
         offset + translated.y * scale - radius);
      g.setColor(tColor); // restore existing color
		}
   }
   
   public Point getPosition(){
      return position;
   }
   public void setPosition(Point position){
      this.position = position;
   }
   public double getEnergy(){
      return energy;
   }
   public void setEnergy(double energy){
      this.energy = energy;
   }
   public void subtractEnergy(double energy){
      this.energy -= energy;
   }
   public String toString(){
      return String.format("Energy: x-coord: %d | y-coord: %d | energy: %.2f", (int)position.getX(), (int)position.getY(), energy);
   }
}