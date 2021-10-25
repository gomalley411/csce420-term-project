//Term Project
import java.io.*;
import java.util.*;

public class Process{
    /**Sophia S. Last Modified Date: 10/25/21 */
   private int pnum; //process number
   private int burst;
   private int arrival;
   private int exit; //exit time
   private int pri; //priority

   public Process(){
       /**Default constructor */
       /**Sophia S. Last Modified Date: 10/22/21 */
       this.pnum = 0;
       this.burst = 0;
       this.arrival = 0;
       this.exit = 0;
       this.pri = 0;
   }

   public Process(int p, int b, int a, int e, int c){
       /**Sophia S. Last Modified Date: 10/25/21 */
       this.pnum = p;
       this.burst = b;
       this.arrival = a;
       this.exit = e;
       this.pri = c; //i ran out of variables lol
   }
   /**All gets and sets:
   /**Sophia S. Last Modified Date: 10/25/21 */ 
   public int getPnum(){
       return pnum;
   }
   public int getBurst(){
       return burst;
   }
   public int getArr(){
       return arrival;
   }
   public int getExit(){
       return exit;
   }
   public int getPri(){
       return pri;
   }
   public void setPnum(int p){
       this.pnum = p;
   }
   public void setBurst(int b){
       this.burst = b;
   }
   public void setArr(int a){
       this.arrival = a;
   }
   public void setExit(int e){
       this.exit = e;
   }
   public void setPri(int p){
       this.pri = p;
   }
   public void printProcess(){
       System.out.println("Process"+pnum+": ");
       System.out.println("Arrival: "+arrival);
       System.out.println("Burst: "+burst);
       System.out.println("Exit: "+exit);
       System.out.println("Priority: "+pri);
   }
}
