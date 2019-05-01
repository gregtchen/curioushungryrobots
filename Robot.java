/*
Gregory, Chen
P3 Curious Hungry Robots
Comp 182 Spring 2018
*/

//package Comp182ImageWindow;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;

import java.awt.*;

public class Robot extends Drawable{
   ArrayList<Point> path;

   private Point position;
   private ArrayDeque<Energy> energyAD;
   private int state;   //state; 0-inactive, 1-curious, 2-hungry
   private double robotEnergy;
   private double robotEnergyCapacity;
   private int moveDistance;
   private int snapDistance;
   private int robotDetectionRadius;
   private double distanceTraveled;
   private int lastDirection;
   private ArrayList<Integer> energyDepletedIndex;
   
   
   public Robot(Point position, double robotEnergyCapacity, int moveDistance, int robotDetectionRadius){
      super(position, Color.red, "robot");
      this.position = position;
      this.energyAD = new ArrayDeque<Energy>();
      this.state = 1;
      this.robotEnergyCapacity = robotEnergyCapacity;
      this.robotEnergy = robotEnergyCapacity;
      this.moveDistance = moveDistance;
      this.snapDistance = (((moveDistance * 2)/3)+1);  //2/3 but rounds down so add 1
      this.robotDetectionRadius = robotDetectionRadius;
      this.distanceTraveled = 0.0;
      this.lastDirection = -1;
      this.energyDepletedIndex = new ArrayList<Integer>();
      
      path = new ArrayList<Point>();
      path.add(new Point(position)); //robot trace starts at (0, 0)
   }
   
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
   
   
   public void moveFifo(ArrayList<Energy> energyAL, int direction){
      if(state == 0) return;
      //CURIOUS STATE
      else if(state == 1 || (state == 2 && energyAD.isEmpty())){
         if(state == 1 && stepOpposite(direction)){
            //System.out.println("tried to step backwards");
            return;
         }
         lastDirection = direction;
         switch(direction){
            case 1: moveNorth();
                    break;
            case 2: moveNorthEast();
                    break;
            case 3: moveEast();
                    break;
            case 4: moveSouthEast();
                    break;
            case 5: moveSouth();
                    break;
            case 6: moveSouthWest();
                    break;
            case 7: moveWest();
                    break;
            case 8: moveNorthWest();
                    break;
            default: break;
         }
         boolean haveSeen;
         for(int i = 0; i < energyAL.size(); i++){
            if(!energyAD.contains(energyAL.get(i)) && position.distance(energyAL.get(i).getPosition()) <= robotDetectionRadius){
               haveSeen = false;
               for(int j = 0; j < energyDepletedIndex.size(); j++){
                  if(energyAL.get(energyDepletedIndex.get(j)).equals(energyAL.get(i))){
                     haveSeen = true;
                  }
               }
               
               if(!haveSeen){
                  energyAD.push(energyAL.get(i));
               }
            }
         }
      }
      //HUNGRY STATE
      else if(state == 2 && !energyAD.isEmpty()){
         //System.out.println(energyAD.peek().toString());
         //System.out.println("distance to current energy: " + position.distance(energyAD.peek().getPosition()));
         if(position.distance(energyAD.peek().getPosition()) <= snapDistance){
            //move robot to energy
            Energy tempEnergy = energyAD.removeFirst();
            int indexOfTempEnergyInAL = energyAL.indexOf(tempEnergy);
            double distanceSnapped = position.distance(tempEnergy.getPosition());
            subtractRobotEnergy(distanceSnapped);
            distanceTraveled += distanceSnapped;
            position.setLocation(tempEnergy.getPosition());
            
            //robot interact with energy; deplete energy and put source in front of AD
            double energyNeeded = robotEnergyCapacity - robotEnergy;
            if(tempEnergy.getEnergy() >= energyNeeded){
               tempEnergy.subtractEnergy(energyNeeded);
               robotEnergy += energyNeeded;
               energyAD.addFirst(tempEnergy);
            }
            else if(tempEnergy.getEnergy() < energyNeeded){
               double energyTaken = energyNeeded - tempEnergy.getEnergy();
               robotEnergy += energyTaken;
               energyDepletedIndex.add(indexOfTempEnergyInAL);
            }
         }
         //if hungry and no food in snap range, move towards food
         else{
            if(position.getY() < energyAD.peek().getPosition().getY() && position.getX() == energyAD.peek().getPosition().getX()){
               moveNorth();
            }
            else if(position.getY() < energyAD.peek().getPosition().getY() && position.getX() < energyAD.peek().getPosition().getX()){
               moveNorthEast();
            }
            else if(position.getX() < energyAD.peek().getPosition().getX() && position.getY() == energyAD.peek().getPosition().getY()){
               moveEast();
            }
            else if(position.getX() < energyAD.peek().getPosition().getX() && position.getY() > energyAD.peek().getPosition().getY()){
               moveSouthEast();
            }
            else if(position.getY() > energyAD.peek().getPosition().getY() && position.getX() == energyAD.peek().getPosition().getX()){
               moveSouth();
            }
            else if(position.getY() > energyAD.peek().getPosition().getY() && position.getX() > energyAD.peek().getPosition().getX()){
               moveSouthWest();
            }
            else if(position.getX() > energyAD.peek().getPosition().getX() && position.getY() == energyAD.peek().getPosition().getY()){
               moveWest();
            }
            else if(position.getX() > energyAD.peek().getPosition().getX() && position.getY() < energyAD.peek().getPosition().getY()){
               moveNorthWest();
            }
         }
      }
      refreshState();
      //System.out.println(this + "\n");
      
      path.add(new Point(position));
   }
   
   
   //fifo implementation
   public void moveLifo(ArrayList<Energy> energyAL, int direction){
      if(state == 0) return;
      //CURIOUS STATE
      else if(state == 1 || (state == 2 && energyAD.isEmpty())){
         if(state == 1 && stepOpposite(direction)){
            //System.out.println("tried to step backwards");
            return;
         }
         lastDirection = direction;
         switch(direction){
            case 1: moveNorth();
                    break;
            case 2: moveNorthEast();
                    break;
            case 3: moveEast();
                    break;
            case 4: moveSouthEast();
                    break;
            case 5: moveSouth();
                    break;
            case 6: moveSouthWest();
                    break;
            case 7: moveWest();
                    break;
            case 8: moveNorthWest();
                    break;
            default: break;
         }
         //check if any energy is in detection range only if curious and not already in
         //if in detection range then add to list
         boolean haveSeen;
         for(int i = 0; i < energyAL.size(); i++){
            if(!energyAD.contains(energyAL.get(i)) && position.distance(energyAL.get(i).getPosition()) <= robotDetectionRadius){
               haveSeen = false;
               for(int j = 0; j < energyDepletedIndex.size(); j++){
                  if(energyAL.get(energyDepletedIndex.get(j)).equals(energyAL.get(i))){
                     haveSeen = true;
                  }
               }
               
               if(!haveSeen){
                  energyAD.addLast(energyAL.get(i));
               }
            }
         }
      }
      //HUNGRY STATE
      else if(state == 2 && !energyAD.isEmpty()){
         //if hungry, and food in snap range, and robot has equal or more energy to energy source, snap to
         //if snap, take off energy from AD and subtract energy from it, if energy left then put it back on
         //System.out.println(energyAD.peek().toString());
         //System.out.println("distance to current energy: " + position.distance(energyAD.peek().getPosition()));
         if( (position.distance(energyAD.peek().getPosition()) <= snapDistance)){
            if(true/*robotEnergy <= energyAD.peek().getEnergy()*/){
            //System.out.println("stuck 4");
               //move robot to energy
               Energy tempEnergy = energyAD.removeFirst();
               int indexOfTempEnergyInAL = energyAL.indexOf(tempEnergy);
               int indexEnergyAL = energyAL.indexOf(tempEnergy);
               double distanceSnapped = position.distance(tempEnergy.getPosition());
               subtractRobotEnergy(distanceSnapped);
               distanceTraveled += distanceSnapped;
               position.setLocation(tempEnergy.getPosition());
               
               //robot interact with energy; deplete energy and put source on end of AD               
               double energyNeeded = robotEnergyCapacity - robotEnergy;
               if(tempEnergy.getEnergy() >= energyNeeded){
                  tempEnergy.subtractEnergy(energyNeeded);
                  robotEnergy += energyNeeded;
                  energyAD.addLast(tempEnergy);
               }
               else if(tempEnergy.getEnergy() < energyNeeded){
                  double energyTaken = energyNeeded - tempEnergy.getEnergy();
                  robotEnergy += energyTaken;
                  energyDepletedIndex.add(indexOfTempEnergyInAL);
               }
               
            }
         
         }
         //if hungry and no food in snap range, move towards food
         else{
         //System.out.println("stuck 5");
            if(position.getY() < energyAD.peek().getPosition().getY() && position.getX() == energyAD.peek().getPosition().getX()){
               moveNorth();
            }
            else if(position.getY() < energyAD.peek().getPosition().getY() && position.getX() < energyAD.peek().getPosition().getX()){
               moveNorthEast();
            }
            else if(position.getX() < energyAD.peek().getPosition().getX() && position.getY() == energyAD.peek().getPosition().getY()){
               moveEast();
            }
            else if(position.getX() < energyAD.peek().getPosition().getX() && position.getY() > energyAD.peek().getPosition().getY()){
               moveSouthEast();
            }
            else if(position.getY() > energyAD.peek().getPosition().getY() && position.getX() == energyAD.peek().getPosition().getX()){
               moveSouth();
            }
            else if(position.getY() > energyAD.peek().getPosition().getY() && position.getX() > energyAD.peek().getPosition().getX()){
               moveSouthWest();
            }
            else if(position.getX() > energyAD.peek().getPosition().getX() && position.getY() == energyAD.peek().getPosition().getY()){
               moveWest();
            }
            else if(position.getX() > energyAD.peek().getPosition().getX() && position.getY() < energyAD.peek().getPosition().getY()){
               moveNorthWest();
            }
         }
      }
      refreshState();
      //System.out.println(this + "\n");
      
      path.add(new Point(position));
   }
   
   public void refreshState(){
      state = (robotEnergy > robotEnergyCapacity/2) ? 1 : robotEnergy <= robotEnergyCapacity/2 && robotEnergy > 0 ? 2 : 0;
   }
   
   private void moveNorth(){
      subtractRobotEnergy(moveDistance);
      distanceTraveled += moveDistance;
      position.setLocation(position.getX(), position.getY() + moveDistance);
   }
   private void moveNorthEast(){
      subtractRobotEnergy(position.distance(position.getX() + moveDistance, position.getY() + moveDistance));
      distanceTraveled += position.distance(position.getX() + moveDistance, position.getY() + moveDistance);
      position.setLocation(position.getX() + moveDistance, position.getY() + moveDistance);
   }
   private void moveEast(){
      subtractRobotEnergy(moveDistance);
      distanceTraveled += moveDistance;
      position.setLocation(position.getX() + moveDistance, position.getY());
   }
   private void moveSouthEast(){
      subtractRobotEnergy(position.distance(position.getX() + moveDistance, position.getY() - moveDistance));
      distanceTraveled += position.distance(position.getX() + moveDistance, position.getY() - moveDistance);
      position.setLocation(position.getX() + moveDistance, position.getY() + moveDistance);
   }
   private void moveSouth(){
      subtractRobotEnergy(moveDistance);
      distanceTraveled += moveDistance;
      position.setLocation(position.getX(), position.getY() - moveDistance);
   }
   private void moveSouthWest(){
      subtractRobotEnergy(position.distance(position.getX() - moveDistance, position.getY() - moveDistance));
      distanceTraveled += position.distance(position.getX() - moveDistance, position.getY() - moveDistance);
      position.setLocation(position.getX() - moveDistance, position.getY() - moveDistance);
   }
   private void moveWest(){
      subtractRobotEnergy(moveDistance);
      distanceTraveled += moveDistance;
      position.setLocation(position.getX() - moveDistance, position.getY());
   }
   private void moveNorthWest(){
      subtractRobotEnergy(position.distance(position.getX() - moveDistance, position.getY() + moveDistance));
      distanceTraveled += position.distance(position.getX() - moveDistance, position.getY() + moveDistance);
      position.setLocation(position.getX() - moveDistance, position.getY() + moveDistance);
   }
   
   private boolean stepOpposite(int direction){
      switch(direction){
         case 1: if(lastDirection == 5) return true;
                 break;
         case 2: if(lastDirection == 6) return true;
                 break;
         case 3: if(lastDirection == 7) return true;
                 break;
         case 4: if(lastDirection == 8) return true;
                 break;
         case 5: if(lastDirection == 1) return true;
                 break;
         case 6: if(lastDirection == 2) return true;
                 break;
         case 7: if(lastDirection == 3) return true;
                 break;
         case 8: if(lastDirection == 4) return true;
                 break;
         default: break;
      }
      return false;
   }
   
   public boolean isHungry(){
      refreshState();
      if(state == 2) return true;
      return false;
   }
   
   public boolean isCurious(){
      refreshState();
      if(state == 1) return true;
      return false;
   }
   
   public boolean isInactive(){
      refreshState();
      if(state == 0) return true;
      return false;
   }
   
   private Point getPosition(){
      return position;
   }
   private void setPosition(Point position){
      this.position = position;
   }
   public ArrayDeque<Energy> getEnergyAD(){
      return energyAD;
   }
   private void setEnergyAD(ArrayDeque<Energy> energyAD){
      this.energyAD = energyAD;
   }
   private int getState(){
      return state;
   }
   private void setState(int state){
      this.state = state;
   }
   private double getRobotEnergy(){
      return robotEnergy;
   }
   private void setRobotEnergy(double robotEnergy){
      this.robotEnergy = robotEnergy;
   }
   private void subtractRobotEnergy(double robotEnergy){
      this.robotEnergy -= robotEnergy;
   }
   private double getRobotEnergyCapacity(){
      return robotEnergyCapacity;
   }
   private void setRobotEnergyCapacity(double robotEnergyCapacity){
      this.robotEnergyCapacity = robotEnergyCapacity;
   }
   private int getMoveDistance(){
      return moveDistance;
   }
   private void setMoveDistance(int moveDistance){
      this.moveDistance = moveDistance;
   }
   private int getSnapDistance(){
      return snapDistance;
   }
   private void setSnapDistance(int snapDistance){
      this.snapDistance = snapDistance;
   }
   private int getRobotDetectionRadius(){
      return robotDetectionRadius;
   }
   private void setRobotDetectionRadius(int robotDetectionRadius){
      this.robotDetectionRadius = robotDetectionRadius;
   }
   public double getDistanceTraveled(){
      return distanceTraveled;
   }
   
   public String stateToString(){
      return state == 1 ? "curious" : state == 2 ? "hungry" : "inactive";
   }
   public String toString(){
      return String.format("Robot: x-coord: %d | y-coord: %d | state: %s | energy: %.2f", (int)position.getX(), (int)position.getY(), stateToString(), robotEnergy);
   }
}