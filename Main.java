import java.io.*;
import java.util.*;
import java.lang.Math;
/*
Group 8
Sophia S
Jessica B
Alex M
George O
*/

public class main{
    public static void main(String[] args){
        /**Step 1: Create Processes */
        /**CPU Burst times should be in range 1-10 */
        /**I/O Burst times should be in range 11-21 */
        /**Sophia S. Last Modified Date: 10/26/21 */
        List<Process> hold = new ArrayList<Process>(); //new processes stored here
        LinkedList<Process> newq = new LinkedList<Process>(); //final sorted processes go here
        int priority = 0;
        /**Generates the random arrival/burst/io burst */
        for(int i = 0; i < 4; i++){
            int arrival = (int)(Math.random()*6);
            int burst = (int)(Math.random()*10) + 1;
            int iburst = (int)(Math.random()*20) + 1;
            Process p = new Process(i+1, burst, iburst, arrival, 0, 0);
            hold.add(p);
        }
        /**This is strictly for testing------------------------- */
        /*Process p1 = new Process(1, 5, 10, 0, 0, 0);
        Process p2 = new Process(2, 5, 10, 0, 0, 0);
        Process p3 = new Process(3, 6, 14, 3, 0, 0);
        Process p4 = new Process(4, 2, 15, 2, 0, 0);
        hold.add(p1);
        hold.add(p2);
        hold.add(p3);
        hold.add(p4);*/
        /**----------------------------------------------------- */
        System.out.println("Hold: ");
        for(int i = 0; i < hold.size(); i++){
            System.out.print("Process "+ hold.get(i).getPid());
            System.out.print(" Arrival: "+ hold.get(i).getArr());
            System.out.println(" Burst: "+ hold.get(i).getBurst());
        }
        /**--------------------------------------------------------------------------------------- */
        /**Step 2: Sort processes based on arrival times 
            -If two processes come in at the same time, whichever
            PID is closer to 0 will be put in front
            -Round robin priority is based off of this sort*/
        /**Sophia S. Last Modified Date: 10/26/21 */
        while(newq.size() < hold.size()){
            for(int i = 0; i < 6; i++){
                for(int j = 0; j < hold.size(); j++){
                    if(hold.get(j).getArr() == i){
                        hold.get(j).setPri(priority += 1);
                        newq.add(hold.get(j));
                    }
                }
            }
        }
        System.out.println("Newq: ");
        for(int i = 0; i < newq.size(); i++){
                System.out.println("Process "+ newq.get(i).getPid() + " Pri: " + newq.get(i).getPri() +" ");
        }
        /**------------------------------------------------------------------------------ */
        /**Step 3: Enter the ready queue - George */
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
