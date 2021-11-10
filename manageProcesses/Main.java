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

public class Main{
    public static void main(String[] args){
        /**Initializations */
        List<Process> hold = new ArrayList<Process>(); //new processes stored here
        List<Process> ihandle = new ArrayList<Process>(); //this is the interrupt handler
        List<Process> pcb = new ArrayList<Process>(); //process control block
        LinkedList<Process> newq = new LinkedList<Process>(); //final sorted processes go here
        LinkedList<Process> runq = new LinkedList<Process>(); //running queue
        LinkedList<Process> waitq = new LinkedList<Process>(); //waiting queue
        LinkedList<Process> termq = new LinkedList<Process>(); //terminated queue
        
        int priority = 0;
        int count = 0;
        int ctime = 0; //current time
        int quantum = 4; //time quantum
        int pronum = 4; //number of processes
        int cs = 0; //contex switches
        int qsize = 4; //size of ready+waiting queue
        Boolean set;
        /**------------------------------------------------------------------------ */
        /**Step 1: Create Processes */
        /**CPU Burst times should be in range 1-10 */
        /**I/O Burst times should be in range 11-21 */
        /**Sophia S. Last Modified Date: 11/9/21 */ 
        /**Generates the random arrival/burst/io burst */
        for(int i = 0; i < pronum; i++){
            int arrival = (int)(Math.random()*6);
            int burst = (int)(Math.random()*10) + 1;
            int iburst = 0;
            if(i % 2 != 0){
                iburst = (int)(Math.random()*20) + 10;
            }
            Process p = new Process(i+1, burst, iburst, arrival, 0, 0, 0);
            hold.add(p);
        }
        /**This is strictly for testing------------------------- */
        /*Process p1 = new Process(1, 4, 10, 0, 0, 0, 0);
        Process p2 = new Process(2, 5, 10, 0, 0, 0, 0);
        Process p3 = new Process(3, 6, 14, 3, 0, 0, 0);
        Process p4 = new Process(4, 2, 0, 2, 0, 0, 0);
        hold.add(p1);
        hold.add(p2);
        hold.add(p3);
        hold.add(p4);*/
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
            System.out.println("Process "+ newq.get(i).getPid() + " Pri: " + newq.get(i).getPri() +" B: " + newq.get(i).getBurst() +" IO: " + newq.get(i).getiBurst());
        }
        //pronum = newq.size();
        /**------------------------------------------------------------------------------ */
        /**Step 3: Enter the ready queue */
        /**We need to determine the size of the ready queue*/
        ArrayList<Process> ready = new LinkedList<Process>(); // ready queue
        while(termq.size() < pronum){ //uncomment when ready queue is fixed
            System.out.println("------------------ctime: "+ ctime + "------------------"); //uncomment when ready queue is fixed
            System.out.println("ready");
            for (int i = 0; i < 4; i++) { 
                ready.add(newq.get(i)); 
                // this is for testing only
                //System.out.println("Process "+ ready.get(i).getPid() + " Pri: " + ready.get(i).getPri() +" B: " + ready.get(i).getBurst() +" ");
            } 
            // once a process has entered the ready queue
            // either move the head into the running queue (step 5)
            // or enter the waiting queue
            runq.add(ready.get(0)); 
            ready.remove(0); 
            
            if(ready.size() > 0){ //uncomment when ready queue is fixed
              System.out.println("Ready queue: ");
                for(int i = 0; i < ready.size(); i++){
                    System.out.println("Process "+ ready.get(i).getPid() + " Pri: " + ready.get(i).getPri() +" B: " + ready.get(i).getBurst() +" IO: " + ready.get(i).getiBurst());
                }  
            }
            if(newq.size() > 0){
               System.out.println("Newq: ");
                for(int i = 0; i < newq.size(); i++){
                    System.out.println("Process "+ newq.get(i).getPid() + " Pri: " + newq.get(i).getPri() +" B: " + newq.get(i).getBurst() +" IO: " + newq.get(i).getiBurst());
                } 
            }
            //-------------------------------------------------------------------------*/
            /**Step 4: The first process in line will get the CPU */

            /**----------------------------------------------------------------- */
            /**Step 5: Run the process in the time quantum.
            Finished? Move to terminated queue
            Still working? Move to waiting queue to process I/O wait*/
            /**Sophia S. Last Modified Date: 11/9/21 */ 
            if(ready.size() > 0){ //uncomment 114-216 when ready queue is fixed
                if(runq.size() == 0){
                    ihandle.add(ready.getFirst()); //interrupt handle
                    runq.add(ihandle.get(0)); //add to running queue
                    cs = runq.getFirst().getCS() + 1; //add context switch
                    set = false; //has not been set in pcb list yet
                    if(pcb.size() > 0){ //PCB implementation
                        for(int i = 0; i < pcb.size(); i++){
                            if(pcb.get(i).getPid() == runq.getFirst().getPid()){
                                pcb.get(i).setBurst(runq.getFirst().getBurst());
                                pcb.get(i).setiBurst(runq.getFirst().getiBurst());
                                pcb.get(i).setArr(runq.getFirst().getArr());
                                pcb.get(i).setExit(runq.getFirst().getExit());
                                pcb.get(i).setPri(runq.getFirst().getPri());
                                pcb.get(i).setCS(cs);
                                set = true; //the state is saved and the switch is made
                            }
                        }
                        if(set == false){ //new process is created if process isn't in pcb already
                            Process w = new Process(runq.getFirst().getPid(), runq.getFirst().getBurst(), runq.getFirst().getiBurst(), runq.getFirst().getArr(), runq.getFirst().getExit(), runq.getFirst().getPri(), cs);
                            pcb.add(w);
                        }
                    }
                    else{ //the first process state is saved and created
                        Process w = new Process(runq.getFirst().getPid(), runq.getFirst().getBurst(), runq.getFirst().getiBurst(), runq.getFirst().getArr(), runq.getFirst().getExit(), runq.getFirst().getPri(), cs);
                        pcb.add(w);
                    }
                    System.out.println("-1ms added for context switch.");
                    ctime += 1;
                    System.out.println("------------------ctime: "+ ctime + "------------------");
                    runq.getFirst().setCS(cs); //update the new context switch number 
                    ready.remove(0); 
                    ihandle.clear(); //clear interrupt handler
                    count = 0;
                }
            }
            if(runq.size() > 0){
                System.out.println("runq: ");
                for(int i = 0; i < runq.size(); i++){
                    System.out.println("Process "+ runq.get(i).getPid() + " Pri: " + runq.get(i).getPri() +" B: " + runq.get(i).getBurst() +" IO: " + runq.get(i).getiBurst());
                }
                int burst = runq.getFirst().getBurst(); 
                if(count < quantum){ //decrement burst while counter is less than quantum
                    System.out.println("count: " + count);
                    if(burst == 0){
                        System.out.println("-process burst is 0");
                        System.out.println("-Process "+runq.getFirst().getPid()+ " is in termq.");
                        runq.getFirst().setExit(ctime);
                        termq.add(runq.getFirst()); 
                        runq.remove(0);
                        //count = 0; //reset count
                    }
                    else{
                        System.out.println("-process burst is decremented");
                        burst -= 1;
                        runq.getFirst().setBurst(burst);
                    }
                    count += 1;
                    if(waitq.size() == 0){ //wait also increases time, this prevents +2
                        System.out.println("-time increased 1ms");
                        ctime += 1;
                    }
                    
                }
                else{ //allotted time is up
                    if(burst == 0){
                        System.out.println("-process burst is 0");
                        System.out.println("-Process "+runq.getFirst().getPid()+ " is in termq.");
                        runq.getFirst().setExit(ctime);
                        termq.add(runq.getFirst()); 
                        runq.remove(0);
                        //count = 0; //reset count
                    }
                    else{
                        System.out.println("-burst is not 0 but time is up");
                        //count = 0;
                        if(runq.getFirst().getiBurst() == 0){
                            //CPU bound
                            System.out.println("-Process "+runq.getFirst().getPid()+ " is CPU Bound"+"\n-Moving to ready queue.");
                            ihandle.add(runq.getFirst());
                            ready.addLast(ihandle.get(0));
                            runq.clear(); //clears the running queue
                            ihandle.clear(); //clears interrupt handler
                        }
                        else{
                            //io bound
                            System.out.println("-Process "+runq.getFirst().getPid()+ " is I/O Bound"+"\n-Moving to waiting queue."); 
                            ihandle.add(runq.getFirst());
                            if(waitq.size() == 0){
                                waitq.addFirst(ihandle.get(0));
                            }
                            else{
                                waitq.addLast(ihandle.get(0));
                            }
                            runq.clear(); //clears the running queue
                            ihandle.clear(); //clears the interrupt handler
                        }
                    }
                    /*if(count == quantum){
                        count = 0; //reset count
                    }
                }
            }
            /**----------------------------------------------------------------------------------------*/
            /**Waiting Queue: This is where the process will go when it's not finished executing. 
            * It will stay in the wait queue
            until it's I/O burst time variable has been decrememented to 0. 
            -Jessica's */
            //System.out.println("Before decrement (waitq): " + waitq.getFirst().getiBurst());
            if(waitq.isEmpty() != true) {
                int iburst = waitq.getFirst().getiBurst();
                if(iburst > 0){
                    iburst = iburst - 1;
                    System.out.println("-I/O burst decremented."+"\n-time increased 1ms");
                    //System.out.println("After decrement (waitq): " + waitq.getFirst().getiBurst()); //should be zero
                    ctime += 1;
                }
                else {
                    //System.out.println("After iburst = 0 (waitq): " + waitq.getFirst().getiBurst()); //should be zero
                    //when i/o burst time is down to zero (has finished its waiting period)
                    System.out.println("iburst is 0");
                    ready.add(waitq.getFirst()); //go back to the ready queue after done waiting for i/o
                    waitq.removeFirst();//get the waitq item that finished off the waitq so another waitq item can run
                }
                System.out.println("waitq: ");
                if(waitq.size() > 0){
                    for(int i = 0; i < waitq.size(); i++){
                        System.out.println("Process "+ waitq.get(i).getPid() + " Pri: " + waitq.get(i).getPri() +" B: " + waitq.get(i).getBurst() +" IO: " + waitq.get(i).getiBurst());
                    }
                }
            }
        }
        /*System.out.println("Done"); //uncomment 246-256 when ready queue is fixed
        System.out.println("-----------termq: ------------");
        if(termq.size() > 0){
            for(int i = 0; i < termq.size(); i++){
                System.out.println("Process "+ termq.get(i).getPid() + " Pri: " + termq.get(i).getPri() +" B: " + termq.get(i).getBurst() +" Exit: " + termq.get(i).getExit());
            }
        }
        System.out.println("-----------pcb: ------------");
        for(int i = 0; i < pcb.size(); i++){
            System.out.println("Process "+ pcb.get(i).getPid() + " CS: "+ pcb.get(i).getCS());
        }*/
    }
}
/**----------------------------------------------------------------- */
            /**New list: Completed
            All newly SORTED processes will go here. */

            /**Ready list: In progress
            This will be done by George
            All CPU bound processess will be in this queue. The head of this list will be moved into
            the running queue. */

            /**Running list: Semi complete
            Sophia
            This is where the process burst time will be decremented. If the time quantum is 4, then the
            process will have (burst time - 4)
            Finished? Record exit time*/

            /**Waiting list: Semi complete
            This is where the process will go when it's not finished executing. It will stay in the wait queue
            until it's I/O burst time variable has been decrememented to 0. 
            Jessica
            */

            /**Terminated list: Complete
            * Sophia
            To enter this list, process burst time must be 0 and the process exit time must be recorded */

            /**PCB: Process Control Block: Semi complete
            Sophia 
            Stores the state of the process when it's being switched from ready to running */

            /**Notes:
            This model will continue running until the size of the terminated list is equal to the number of
            processes created. 
            */
