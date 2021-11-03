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
        /**Initializations */
        List<Process> hold = new ArrayList<Process>(); //new processes stored here
        List<Process> ihandle = new ArrayList<Process>(); //this is the interrupt handler
        LinkedList<Process> newq = new LinkedList<Process>(); //final sorted processes go here
        LinkedList<Process> runq = new LinkedList<Process>(); //running queue
        LinkedList<Process> waitq = new LinkedList<Process>(); //waiting queue
        
        int priority = 0;
        int count = 0;
        int ctime = 0; //current time
        int quantum = 4; //time quantum
        /**------------------------------------------------------------------------ */
        /**Step 1: Create Processes */
        /**CPU Burst times should be in range 1-10 */
        /**I/O Burst times should be in range 11-21 */
        /**Sophia S. Last Modified Date: 10/26/21 */
        /**Generates the random arrival/burst/io burst */
        for(int i = 0; i < 4; i++){
            int arrival = (int)(Math.random()*6);
            int burst = (int)(Math.random()*10) + 1;
            int iburst = (int)(Math.random()*20) + 1;
            Process p = new Process(i+1, burst, iburst, arrival, 0, 0);
            hold.add(p);
        }
        /**This is strictly for testing------------------------- */
        /*Process p1 = new Process(1, 4, 10, 0, 0, 0);
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
            System.out.println("Process "+ newq.get(i).getPid() + " Pri: " + newq.get(i).getPri() +" B: " + newq.get(i).getBurst() +" ");
        }
        /**------------------------------------------------------------------------------ */
        /**Step 3: Enter the ready queue */
        /**We need to determine the size of the ready queue*/

        ArrayList<Process> ready = new ArrayList<Process>(); // ready queue
        //System.out.println("ready");
        for (int i = 0; i < newq.size(); i++) {
            ready.add(newq.get(i));
            // this is for testing only
            //System.out.println("Process "+ ready.get(i).getPid() + " Pri: " + ready.get(i).getPri() +" B: " + ready.get(i).getBurst() +" ");
        }
        // once a process has entered the ready queue
        // either move the head into the running queue (step 5)
        // or enter the waiting queue
        runq.add(ready.get(0));
        ready.remove(0);

        /**Step 4: The first process in line will get the CPU */

        /**----------------------------------------------------------------- */
        /**Step 5: Run the process in the time quantum.
         Finished? Move to terminated queue
         Still working? Move to waiting queue to process I/O wait (lines 82-122)*/
        System.out.println("Running queue implementation: ");
        //ihandle.add(readyq.getFirst()); //uncomment when readyq is done
        /*ihandle.add(newq.getFirst()); //testing
        runq.add(ihandle.get(0));
        System.out.println("Before: " + runq.getFirst().getBurst());
        int burst = runq.getFirst().getBurst();
        while(count < quantum){
            if(burst == 0){
                count = quantum;
            }
            else{
                burst -= 1;
                ctime += 1;
            }
            count += 1;
        }
        if(burst == 0){
            System.out.println("burst is 0");
            runq.getFirst().setExit(ctime);
            //termq.add(runq.getFirst()); //uncomment when terminated queue is made
            //System.out.println("Termq: " + termq.getFirst().getPid());
            runq.getFirst().setBurst(burst);
            System.out.println("After: " + runq.getFirst().getBurst() + " Exit: " + runq.getFirst().getExit());
            System.out.println("ctime: " + ctime);
        }
        else{
            System.out.println("burst is not 0");
            runq.getFirst().setBurst(burst);
            System.out.println("After: " + runq.getFirst().getBurst() + " Exit: " + runq.getFirst().getExit());
            System.out.println("ctime: " + ctime);
            /*if(runq.getFirst().getiBurst() == 0){
                //CPU bound
                readyq.addLast(runq.getFirst());
                runq.clear(); //clears the running queue
            }
            else{
                //io bound
                waitq.addLast(runq.getFirst());
                runq.clear(); //clears the running queue
            }
        }*/
        
        /**Waiting Queue: This is where the process will go when it's not finished executing. 
         * It will stay in the wait queue
         until it's I/O burst time variable has been decrememented to 0. 
         -Jessica's */
 
        System.out.println("Before: " + waitq.getFirst().getiBurst());
        int iburst = waitq.getFirst().getiBurst();
        while(iburst > 0){
                iburst = iburst - 1;
        }
        System.out.println("After: " + waitq.getFirst().getiBurst()); //should be zero
        
    	//when i/o burst time is down to zero (has finished its waiting period)
        System.out.println("iburst is 0");
        readyq.add(waitq.getFirst()); //go back to the ready queue after done waiting for i/o
        waitq.removeFirst();//get the waitq item that finished off the waitq so another waitq item can run

        
        /**----------------------------------------------------------------- */
        /**New list: Completed
         All newly SORTED processes will go here. */

        /**Ready list: In progress
         This will be done by George
         All CPU bound processess will be in this queue. The head of this list will be moved into
         the running queue. */

        /**Running list: In progress
         Sophia
         This is where the process burst time will be decremented. If the time quantum is 4, then the
         process will have (burst time - 4)
         Finished? Record exit time*/

        /**Waiting list: In progress
         This is where the process will go when it's not finished executing. It will stay in the wait queue
         until it's I/O burst time variable has been decrememented to 0. 
         Jessica
         */

        /**Terminated list: Incomplete
         * 
         To enter this list, process burst time must be 0 and the process exit time must be recorded */

        /**Notes:
         This model will continue running until the size of the terminated list is equal to the number of
         processes created.  */
    }
}
