/*
Gregory, Chen
P3 Curious Hungry Robots
Comp 182 Spring 2018
*/

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Sample{
   private ArrayList<Double> data;
   private double min;
   private double mean;
   private double risk;
   private double max;
      
   public Sample(){
      data = new ArrayList<Double>();
   }
   
   public Sample(double min, double mean, double risk, double max, ArrayList<Double> data){
      this.min = min;
      this.mean = mean;
      this.risk = risk;
      this.max = max;
      this.data = data;
   }
   
   public void fillData(File file) throws FileNotFoundException{
      Scanner input = new Scanner(file);
      while(input.hasNextLine()){
         String s = input.nextLine();
         data.add(Double.parseDouble(s));
      }
   }
   
   public void fillData(double d){
      this.data.add(d);
   }
   
   public void computeStats(){
      if(data == null){
         return;
      }
      
      //min
      min = data.get(0);
         for(int i = 0; i < data.size(); i++){
            if(data.get(i) < min){
               min = data.get(i);
            }
         }
      
      
      //mean;
      mean = 0.0;
      for(int i = 0; i < data.size(); i++){
         mean += data.get(i);
      }
      mean /= data.size();
      
      //risk
      //Work out the Mean (the simple average of the numbers)
      //Then for each number: subtract the Mean and square the result
      //Then work out the mean of those squared differences.
      //Take the square root of that and we are done!
      risk = 0.0;
      for(int i =0; i < data.size(); i++){
         risk += Math.pow((data.get(i)-mean), 2);
      }
      risk /= data.size();
      risk = Math.sqrt(risk);

      //max
      max = data.get(0);
      for(int i = 0 ; i < data.size(); i++){
         if(data.get(i) > max){
            max = data.get(i);
         }
      }
   }

   
   public double getMin(){
      return min;
   }
   public void setMin(double min){
      this.min = min;
   }
   public double getMax(){
      return max;
   }
   public void setMax(double max){
      this.max = max;
   }
   public double getMean(){
      return mean;
   }
   public void setMean(double mean){
      this.mean = mean;
   }
   public double getRisk(){
      return risk;
   }
   public void setRisk(){
      this.risk = risk;
   }
   public ArrayList<Double> getData(){
      return data;
   }
   
   public void setData(ArrayList<Double> data){
      this.data = data;
   }
   
   public String toString(){
      String retval = String.format("Sample: size = %1$d, min = %2$.3f, mean = %3$.3f, risk = %4$.3f, max = %5$.3f", data.size(), min, mean, risk, max);
      return retval;
   }
   
}