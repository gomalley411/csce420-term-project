//Term Project
import java.io.*;
import java.util.*;

public class Process{
    /**Sophia S. Last Modified Date: 10/25/21 */
   private int pid; //process number
   private int burst;
   private int iburst; //i/o burst time
   private int arrival;
   private int exit; //exit time
   private int pri; //priority

   public Process(){
       /**Default constructor */
       /**Sophia S. Last Modified Date: 10/26/21 */
       this.pid = 0;
       this.burst = 0;
       this.iburst = 0;
       this.arrival = 0;
       this.exit = 0;
       this.pri = 0;
   }

   public Process(int p, int b, int i, int a, int e, int c){
       /**Sophia S. Last Modified Date: 10/26/21 */
       this.pid = p;
       this.burst = b;
       this.iburst = i;
       this.arrival = a;
       this.exit = e;
       this.pri = c; //i ran out of variables lol
   }
   /**All gets and sets:
   /**Sophia S. Last Modified Date: 10/26/21 */ 
   public int getPid(){
       return pid;
   }
   public int getBurst(){
       return burst;
   }
   public int getiBurst(){
       return iburst;
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
   public void setPid(int p){
       this.pid = p;
   }
   public void setBurst(int b){
       this.burst = b;
   }
   public void setiBurst(int i){
       this.iburst = i;
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
       System.out.println("Process "+pid+": ");
       System.out.println("Arrival: "+arrival);
       System.out.println("Burst: "+burst);
       System.out.println("I/O Burst: "+iburst);
       System.out.println("Exit: "+exit);
       System.out.println("Priority: "+pri);
   }
}
