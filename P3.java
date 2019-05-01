/*
Gregory, Chen
P3 Curious Hungry Robots
Comp 182 Spring 2018
*/

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

//import Comp182ImageWindow.*;

public class P3 extends Comp182ImageFrame{
   private static final long serialVersionUID = 42L;

   Robot robot;
   private Random rnd;
   private ArrayList<Energy> energyAL;
   private final int planeEnergyRadius = 200;
   private final int energyLocations = 30;
   private final int energyIntervalDistance = 20;
   private final double energyInitialCapacity = 200;
   private final int robotDetectionRadius = 40;
   private final int moveDistance = 13;
   private Sample fifoImplementation;
   private Sample lifoImplementation;
   
   public P3(String title){
      super(title);
      
      
      
      
      rnd = new Random();
      energyAL = new ArrayList<Energy>();
      
      //place energy on plane
      for(int i = 0; i < energyLocations; i++){
         Energy tempEnergy;
         do{
            //GENERATE ENERGIES BETTER, NEED TO HAVE NEGATIVE
            int x = (int)(Math.random() * (planeEnergyRadius + planeEnergyRadius) - planeEnergyRadius);
            int y = (int)(Math.random() * (planeEnergyRadius + planeEnergyRadius) - planeEnergyRadius);
            tempEnergy = new Energy(new Point(x, y), energyInitialCapacity);
         }while(!canPlace(tempEnergy));
         energyAL.add(tempEnergy);
         addDrawable(tempEnergy);
      }
      
      //print out energy positions
      for(int i = 0; i < energyAL.size(); i++){
         //System.out.println("" + i + ": " + energyAL.get(i));
      }
      
      
      
      
      //FIFO & LIFO SIMULATION 
      System.out.println("------- FIFO ---------------------");
      fifoImplementation = new Sample();
      for(int i = 0; i < 1000; i++){
         robot = new Robot(new Point(0, 0), energyInitialCapacity, moveDistance, robotDetectionRadius);
         while(!robot.isInactive()){
            robot.moveFifo(energyAL, (rnd.nextInt(8) + 1));
         }
         fifoImplementation.fillData(robot.getDistanceTraveled());
      }
      fifoImplementation.computeStats();
      System.out.println(fifoImplementation);
      
      System.out.println("------- LIFO ---------------------");
      lifoImplementation  = new Sample();
      for(int i = 0; i < 1000; i++){
         robot = new Robot(new Point(0, 0), energyInitialCapacity, moveDistance, robotDetectionRadius);
         while(!robot.isInactive()){
            robot.moveLifo(energyAL, (rnd.nextInt(8) + 1));
         }
         lifoImplementation.fillData(robot.getDistanceTraveled());
      }
      lifoImplementation.computeStats();
      System.out.println(lifoImplementation);
      
      
      //reset robot and uncomment one of the lines to select between visualizing FIFO and LIFO
      robot = new Robot(new Point(0, 0), energyInitialCapacity, moveDistance, robotDetectionRadius);
      paths.add(robot);
      while(!robot.isInactive()){
       //robot.moveFifo(energyAL, (rnd.nextInt(8) + 1));
       robot.moveLifo(energyAL, (rnd.nextInt(8) + 1));
      }
      repaint();
      saveImage("demo-final", "Final");
      
      
   }

   public boolean canPlace(Energy e){
      if(energyAL == null)return false;
      for(int i = 0; i < energyAL.size(); i++){
         if(e.getPosition().distance(energyAL.get(i).getPosition()) <= energyIntervalDistance){
            return false;
         }
      }
      return true;
   }
   
   public static void main(String[] args){
      P3 demo = new P3("Curious Hungry Robots - Gregory Chen");
   }
}