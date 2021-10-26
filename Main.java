import java.io.*;
import java.util.*;
import java.lang.Math;
import Java.util.ArrayList;
import Java.util.LinkedList;
/*
Group 8
Sophia S
Jessica B
Alex M
George O
*/

public class main{
    public static void main(String[] args){
        /**Sophia S. Last Modified Date: 10/25/21 */
        /*Process p0 = new Process(1, 5, 10, 0, 0, 1);
        p0.printProcess();*/
        
        for(int i = 0; i < 4; i++){
            int arrival = (int)(Math.random()*6);
            int burst = (int)(Math.random()*10) + 1;
            int iburst = (int)(Math.random()*20) + 1;
            //System.out.println("Arrival: "+ arrival);
            //System.out.println("burst: "+ burst);
            //System.out.println("I/O burst: "+iburst);
        }
        






        /**Step 1: Create Processes */
        /**CPU Burst times should be in range 1-10 */
        /**I/O Burst times should be in range 11-21 */

        /**Step 2: Sort processes based on arrival times 
            -If two processes come in at the same time, whichever
            PID is closer to 0 will be put in front
            -Round robin priority is based off of this sort*/

        /**Step 3: Enter the ready queue */
        /**We need to determine the size of the ready queue*/

        /**Step 4: The first process in line will get the CPU */

        /**Step 5: Run the process in the time quantum.
            Finished? Move to terminated queue
            Still working? Move to waiting queue to process I/O wait */
        
        /**New list:
        All newly SORTED processes will go here. */

        /**Ready list:
        All CPU bound processess will be in this queue. The head of this list will be moved into 
        the running queue. */

        /**Running list:
        This is where the process burst time will be decremented. If the time quantum is 4, then the 
        process will have (burst time - 4) 
        Finished? Record exit time*/

        /**Waiting list:
        This is where the process will go when it's not finished executing. It will stay in the wait queue
        until it's I/O burst time variable has been decrememented to 0. */

        /**Terminated list:
        To enter this list, process burst time must be 0 and the process exit time must be recorded */

        /**Notes:
        This model will continue running until the size of the terminated list is equal to the number of 
        processes created.  */
    }
}
