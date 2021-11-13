import java.io.*;
import java.util.*;
import java.lang.Math;
/*
Group 8
Sophia S.
Jessica B.
Alex M.
George O.
*/

public class Main{
    public static void main(String[] args) {
        /**Initializations */
        LinkedList<Process> hold = new LinkedList<Process>(); //new processes stored here
        List<Process> ihandle = new ArrayList<Process>(); //this is the interrupt handler
        List<Process> ihandle2 = new ArrayList<Process>();
        List<Process> pcb = new ArrayList<Process>(); //process control block
        LinkedList<Process> obuffer = new LinkedList<Process>(); //overflow buffer for waitq is here
        LinkedList<Process> newq = new LinkedList<Process>(); //final sorted processes go here
        LinkedList<Process> runq = new LinkedList<Process>(); //running queue
        LinkedList<Process> ready = new LinkedList<Process>(); // ready queue
        LinkedList<Process> waitq = new LinkedList<Process>();
        LinkedList<Process> termq = new LinkedList<Process>(); //terminated queue

        int priority = 0;
        int count = 0;
        int ctime = 0; //current time
        int quantum = 4; //time quantum
        int pronum = 36; //number of processes
        int cs = 0; //context switches
        int qsize = 4; //size of ready+waiting queue
        Boolean set;

        /**------------------------------------------------------------------------ */
        /**Step 1: Create Processes */
        /**CPU Burst times should be in range 1-10 */
        /**I/O Burst times should be in range 11-21 */
        /**Created By: Sophia S. Last Modified Date: 11/11/21 */
        /**Generates the random arrival/burst/io burst */
        for (int i = 0; i < pronum; i++) {
            int arrival = (int) (Math.random() * 6);
            int burst = (int) (Math.random() * 10) + 1;
            int iburst = 0;
            if (i % 2 != 0) {
                iburst = (int) (Math.random() * 20) + 10;
            }
            priority += 1;
            Process p = new Process(i + 1, burst, iburst, arrival, 0, priority, 0);
            hold.add(p);
        }
        /**--------------------------------------------------------------------------------------- */
        System.out.println(pronum+ " Process(es) created.\n");
        while (termq.size() < pronum) {
            System.out.println("------------------ctime: " + ctime + "------------------");
            /**------------------------------------------------------------------------------ */
            /**Step 2: Sort processes based on arrival times
             -If two processes come in at the same time, whichever
             PID is closer to 0 will be put in front
             -Round robin priority is based off of this sort*/
            /**Created By: Sophia S. Last Modified Date: 11/11/21 */
            if(hold.size() > 0){
                if(newq.size() < qsize){
                    for (int i = 0; i < qsize; i++) {
                        if(newq.size() == qsize){
                            i = qsize;
                        }
                        else if(hold.size() == 0){
                            i = qsize;
                        }
                        else{
                            newq.add(hold.getFirst());
                            System.out.println("PROCESS "+hold.getFirst().getPid() + " MOVED TO NEW QUEUE.");
                            hold.removeFirst();
                        }
                    }
                }
            }
            System.out.println("Newq: ");
            for (int i = 0; i < newq.size(); i++) {
                System.out.println("Process " + newq.get(i).getPid() + " Pri: " + newq.get(i).getPri() + " B: " + newq.get(i).getBurst() + " IO: " + newq.get(i).getiBurst());
            }

            /**------------------------------------------------------------------------------ */
            /**Step 3: Enter the ready queue */
            // get the first qsize amount of processes and add to ready queue
            for (int a = 0; a < qsize && newq.size() != 0; a++) {
                Process p = newq.get(0);
                if (ready.size() < qsize) {
                    ready.add(p);
                    System.out.println("PROCESS "+p.getPid() + " MOVED TO READY QUEUE.");
                    newq.remove(0);
                }
            }


            Collections.sort(ready, Comparator.comparing(Process::getPri)); // sort the ready queue by priority

            // once a process has entered the ready queue
            // either move the head into the running queue (step 5)
            // or enter the waiting queue

            if (ready.size() > 0) { // now we print out the ready queue
                System.out.println("Ready queue: ");
                for (int i = 0; i < ready.size(); i++) {
                    System.out.println("Process " + ready.get(i).getPid() + " Pri: " + ready.get(i).getPri() + " B: " + ready.get(i).getBurst() + " IO: " + ready.get(i).getiBurst());
                }
            }
            //-------------------------------------------------------------------------*/
            /**Step 4: The first process in line will get the CPU */

            /**Malicious Content - Alex Miller - Last updated 11/10 - Goal: Change the burst time of a process while it's sitting in the readyq
             /** Variable documentation (Sorry, couldn't come up with good variable names, so I wrote 'em out instead)
             * - p: randomly selected process in ready queue. We change the burst time of this selected process (p is the location in ready queue)
             * - check: random int that determines whether we add (check is an even number) or subtract (check is an odd number) from the original burst time for added randomness
             *      note* - check is written with a safe guard (while loop) so it cannot be zero
             * - v: variable that stores the randomly determined value to be added/subtracted from the original Burst time. Neccesary to leave in with a safe guard to make sure the
             *      number subtracted is at least 1 (adding/subtracting 0 kinda ruins the idea of changing the burst time)
             *      note* - It is possible to change the Burst time of a process to 0. See if(Check = ...) below to toggle that on/off
             * Issue (resolved) - If Original Burst Time = 1, we can't subtract anything. If we try we get an infinite loop because of my safeguards (can't subtract 0 and Randomizer
             *      is based on the Burst Time and can't equal the number entered, so it rerolls infinitly as it can only roll 0's). Unfortunatly, due to how the code is written
             *      it is extremely difficult to reproduce as it requires at least one of the randomly generated processes to have a Burst = 1, for that process to be entered into the
             *      ready queue, for that process to be randomly selected to be changed, AND for Check to be odd so it is subtracting. That's a lot of random chance. The fact that I happened
             *      upon this error within 30 minutes of testing is kinda incredible. I want to fix it, and I have some ideas to do it, but I'm not sure if they'll work and testing this thing
             *      (short of building a bogus ready queue full of custom-built Burst = 1 Processes) is gonna be a pain -- Note* -- wasn't actually that hard, just needed some creativity
             */
            /*int p = (int)(Math.random()*ready.size());
            System.out.println("Process: " + p); // testing
            System.out.println("Original Burst: " + ready.get(p).getBurst());
            int v;

            int check = (int)(Math.random()*50);
            //System.out.println("Check: " + check); //testing
            while(check == 0) {
                check = (int)(Math.random()*50); // keep rerolling so check is at least 1 (we want to make sure the burst time changes so if we roll a zero, it kinda defeats the point)
            }
            //System.out.println("Check value: " + check); // testing
            if(check % 2 == 1) { // check is an odd number, subtract from burst time
                // check size of burst time prior to subvtracting so we don't go negative
                //System.out.println("Old Burst Time: " + ready.get(p).getBurst()); // testing
                if(ready.get(p).getBurst() == 1) {
                    v = 0; // ------------------------------------------------------           If we want Burst Time to be reducable to zero, set v = 1 and uncomment v++
                }                                                               // |           If we don't want Burst Time to be reducible to zero, set v = 0 and comment
                else {                                                          // | --------- out v++. (if(...) {v=0} is intended to prevent a rare error from occuring. In
                    v = (int)(Math.random()*ready.get(p).getBurst());           // |           the event of the error occuring, the original Burst time and new Burst time
                    //v++; // ------------------------------------------------------           will both = 1. Error is detailed more in the Variable Documentation above)
                    //System.out.println("first v: " + v); // testing
                    while(v == 0) {
                        v = (int)(Math.random()*ready.get(p).getBurst()); // reroll in the event of rolling a zero
                    }
                }
                //System.out.println("final v: " + v); // testing
                ready.get(p).setBurst(ready.get(p).getBurst() - v); // subtract v from original burst time: (New burst = old burst - [random value that is less than old burst])
            }
            else if(check % 2 == 0) { // check is an even number, add to burst time
                v = (int)(Math.random()*10); // 10 is arbitrary and can be changed, I just thought more than 10 added to Burst time would be kinda insane
                //System.out.println("First v: " + v); // testing
                while(v == 0) {
                    v = (int)(Math.random()*10); // reroll in the event of getting a zero
                }
                //System.out.println("Final v: " + v); // testing
                ready.get(p).setBurst(ready.get(p).getBurst() + v); // add v to original Burst time: (New burst = old burst + [random value 1-10])
            }
            System.out.println("New burst time: " + ready.get(p).getBurst()); // testing


            /**----------------------------------------------------------------- */
            /**Step 5: Run the process in the time quantum.
             Finished? Move to terminated queue
             Still working? Move to waiting queue to process I/O wait*/
            /**Created By: Sophia S. Last Modified Date: 11/11/21 */
            if (ready.size() > 0) {
                if(runq.size() == 0) {
                    System.out.println("Interrupt issued.");
                    ihandle.add(ready.getFirst()); //interrupt handle
                    runq.add(ihandle.get(0)); //add to running queue
                    cs = runq.getFirst().getCS() + 1; //add context switch
                    set = false; //has not been set in pcb list yet
                    if(pcb.size() > 0) { //PCB implementation
                        for (int i = 0; i < pcb.size(); i++) {
                            if (pcb.get(i).getPid() == runq.getFirst().getPid()) {
                                pcb.get(i).setBurst(runq.getFirst().getBurst());
                                pcb.get(i).setiBurst(runq.getFirst().getiBurst());
                                pcb.get(i).setArr(runq.getFirst().getArr());
                                pcb.get(i).setExit(runq.getFirst().getExit());
                                pcb.get(i).setPri(runq.getFirst().getPri());
                                pcb.get(i).setCS(cs);
                                set = true; //the state is saved and the switch is made
                            }
                        }
                        System.out.println(set);
                        if(set == false) { //new process is created if process isn't in pcb already
                            System.out.println("NEW PCB PROCESS STATE SAVED");
                            Process w = new Process(runq.getFirst().getPid(), runq.getFirst().getBurst(), runq.getFirst().getiBurst(), runq.getFirst().getArr(), runq.getFirst().getExit(), runq.getFirst().getPri(), cs);
                            pcb.add(w);
                        }
                    }
                    else { //the first process state is saved and created
                        System.out.println("NEW PCB PROCESS STATE SAVED");
                        Process w = new Process(runq.getFirst().getPid(), runq.getFirst().getBurst(), runq.getFirst().getiBurst(), runq.getFirst().getArr(), runq.getFirst().getExit(), runq.getFirst().getPri(), cs);
                        pcb.add(w);
                    }
                    System.out.println("-1ms added for context switch.");
                    ctime += 1;
                    System.out.println("------------------ctime: " + ctime + "------------------");
                    runq.getFirst().setCS(cs); //update the new context switch number
                    ready.remove(0);
                    System.out.println("PROCESS "+ihandle.get(0).getPid() + " MOVED TO RUNNING QUEUE.");
                    ihandle.clear(); //clear interrupt handler
                    count = 0;
                }
            }
            if(runq.size() > 0) {
                System.out.println("runq: ");
                for (int i = 0; i < runq.size(); i++) {
                    System.out.println("Process " + runq.get(i).getPid() + " Pri: " + runq.get(i).getPri() + " B: " + runq.get(i).getBurst() + " IO: " + runq.get(i).getiBurst());
                }
                int burst = runq.getFirst().getBurst();
                if (count < quantum) { //decrement burst while counter is less than quantum
                    System.out.println("count: " + count);
                    if (burst == 0) {
                        System.out.println("-process burst is 0");
                        System.out.println("PROCESS " + runq.getFirst().getPid() + " MOVED TO TERMINATED QUEUE.");
                        runq.getFirst().setExit(ctime);
                        termq.add(runq.getFirst());
                        runq.remove(0);
                    }
                    else {
                        System.out.println("-process burst is decremented");
                        burst -= 1;
                        runq.getFirst().setBurst(burst);
                    }
                    count += 1;
                    if (waitq.size() == 0) { //wait also increases time, this prevents +2
                        System.out.println("-time increased 1ms");
                        ctime += 1;
                    }
                }
                else { //allotted time is up
                    if (burst == 0) {
                        System.out.println("-process burst is 0");
                        System.out.println("PROCESS " + runq.getFirst().getPid() + " MOVED TO TERMINATED QUEUE");
                        runq.getFirst().setExit(ctime);
                        termq.add(runq.getFirst());
                        runq.remove(0);
                    }
                    else {
                        System.out.println("-burst is not 0 but time is up");
                        //context switch from running -> ready or running -> waiting
                        cs = runq.getFirst().getCS() + 1; //add context switch
                        if (pcb.size() > 0) { //PCB implementation
                            for (int i = 0; i < pcb.size(); i++) {
                                if (pcb.get(i).getPid() == runq.getFirst().getPid()) {
                                    pcb.get(i).setBurst(runq.getFirst().getBurst());
                                    pcb.get(i).setiBurst(runq.getFirst().getiBurst());
                                    pcb.get(i).setArr(runq.getFirst().getArr());
                                    pcb.get(i).setExit(runq.getFirst().getExit());
                                    pcb.get(i).setPri(runq.getFirst().getPri());
                                    pcb.get(i).setCS(cs);
                                }
                            }
                        }
                        System.out.println("-1ms added for context switch.");
                        ctime += 1;
                        System.out.println("------------------ctime: " + ctime + "------------------");
                        ihandle.add(runq.getFirst());
                        runq.clear(); //clears the running queue
                        ihandle.get(0).setCS(cs); //update the new context switch number
                        if(ihandle.get(0).getiBurst() == 0){
                            //CPU bound
                            System.out.println("PROCESS " + ihandle.get(0).getPid() + " IS CPU BOUND." + "\n-MOVING TO READY QUEUE.");
                            if(ready.size() == qsize){
                                System.out.println("interrupt issued.");
                                ihandle2.add(ready.getFirst());
                                System.out.println("PROCESS "+ihandle2.get(0).getPid() + " MOVED TO RUNNING QUEUE.");
                                runq.add(ihandle2.get(0)); //add to running queue
                                //context switch from ready -> running
                                cs = runq.getFirst().getCS() + 1; //add context switch
                                set = false;
                                if (pcb.size() > 0) { //PCB implementation
                                    for (int i = 0; i < pcb.size(); i++) {
                                        if (pcb.get(i).getPid() == runq.getFirst().getPid()) {
                                            pcb.get(i).setBurst(runq.getFirst().getBurst());
                                            pcb.get(i).setiBurst(runq.getFirst().getiBurst());
                                            pcb.get(i).setArr(runq.getFirst().getArr());
                                            pcb.get(i).setExit(runq.getFirst().getExit());
                                            pcb.get(i).setPri(runq.getFirst().getPri());
                                            pcb.get(i).setCS(cs);
                                            set = true;
                                        }
                                    }
                                    //this conditional is here bc it may be going ready -> running for the first time
                                    if(set == false){ 
                                       //the first process state is saved and created
                                        System.out.println("NEW PCB PROCESS STATE SAVED");
                                        Process w = new Process(runq.getFirst().getPid(), runq.getFirst().getBurst(), runq.getFirst().getiBurst(), runq.getFirst().getArr(), runq.getFirst().getExit(), runq.getFirst().getPri(), cs);
                                        pcb.add(w);
                                    }
                                }
                                System.out.println("-1ms added for context switch.");
                                ctime += 1;
                                System.out.println("------------------ctime: " + ctime + "------------------");
                                ready.removeFirst();
                                ihandle2.clear();
                                count = 0;
                            }
                            ready.addLast(ihandle.get(0));
                            ihandle.clear(); //clears interrupt handler
                        }
                        else {
                            //io bound
                            System.out.println("PROCESS " + ihandle.get(0).getPid() + " IS I/O BOUND." + "\n-MOVING TO WAITING QUEUE.");
                            if (waitq.size() == 0) {
                                waitq.addFirst(ihandle.get(0));
                                ihandle.clear(); //clears the interrupt handler
                            }
                            else {
                            	if(waitq.size() < qsize){
                            		waitq.addLast(ihandle.get(0));
                            		ihandle.clear(); //clears the interrupt handler
                            	}
                            	else { //if waitq is full, try a little overflow buffer
                            		obuffer.addLast(ihandle.get(0)); //holds overflow so it doesn't run again while waiting to be put in wait
                            	}
                            }
                        }
                        if(obuffer.size() != 0) { //try to put overflow into waitq at end of every round if there is any
                        	if(waitq.size() < qsize){
                        		waitq.addLast(obuffer.get(0));
                        		obuffer.clear(); //clears overflow buffer
                        	}
                        }
                    }
                }
            }
            /**----------------------------------------------------------------------------------------*/
            /**Waiting Queue: This is where the process will go when it's not finished executing.
             * It will stay in the wait queue
             until it's I/O burst time variable has been decrememented to 0.
             -Jessica's */
            if (waitq.isEmpty() != true) {
                int iburst = waitq.getFirst().getiBurst();
                if (iburst > 0) {
                    iburst = iburst - 1;
                    System.out.println("-I/O burst decremented." + "\n-time increased 1ms");
                    waitq.getFirst().setiBurst(iburst);
                    ctime += 1;
                }
                else {
                    //when i/o burst time is down to zero (has finished its waiting period)
                    System.out.println("iburst is 0");
                    System.out.println("Waitq Interrupt issued.");
                    ihandle.add(waitq.getFirst()); //interrupt handle
                    ready.add(ihandle.get(0)); //add to ready queue
                    waitq.removeFirst();//get the waitq item that finished off the waitq so another waitq item can run
                    ihandle.clear(); //clears interrupt handler
                }
                System.out.println("waitq: ");
                if (waitq.size() > 0) {
                    for (int i = 0; i < waitq.size(); i++) {
                        System.out.println("Process " + waitq.get(i).getPid() + " Pri: " + waitq.get(i).getPri() + " B: " + waitq.get(i).getBurst() + " IO: " + waitq.get(i).getiBurst());
                    }
                }
            }
        }
        System.out.println("\n---------SIMULATION COMPLETE. ALL PROCESSES TERMINATED---------\n");
        System.out.println("-----------termq: ------------");
        if(termq.size() > 0){
            for(int i = 0; i < termq.size(); i++){
                System.out.println("Process "+ termq.get(i).getPid() + " Pri: " + termq.get(i).getPri() +" B: " + termq.get(i).getBurst() +" Exit: " + termq.get(i).getExit());
            }
        }
        System.out.println("-----------pcb: ------------");
        for(int i = 0; i < pcb.size(); i++){
            System.out.println("Process "+ pcb.get(i).getPid() + " CS: "+ pcb.get(i).getCS());
        }
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
