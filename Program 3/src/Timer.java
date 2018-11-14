/*  Timer.java
    A sample class to assist in performing empirical timing tests
    for your project #3.
    You may need to reduce the value of iterations to a lower count
    depending on the abilities of the machine used for timing.  If
    timing takes too long, try ierations = 7
    
    CS310 Spring 2018
    Alan Riggins
*/    

import data_structures.*;

public class Timer {
    public static void main(String [] args) {
        int iterations = 10;  // number of timing tests
        int workSize = 10000;   // size of add/remove cycle loop
        int structureSize = 100;  // initial size of PQ, doubles with each iteration
        int loopStructureSize = structureSize; // helper var
        
        long start = 0, stop = 0;
        long [] insertTimes = new long[iterations]; // arrays to hold results
        long [] cycleTimes = new long[iterations];
        long [] removeTimes = new long[iterations];
                        
        PriorityQueue<Integer> pq = 
                //new OrderedArrayPriorityQueue<Integer>(512000);
                //new UnorderedArrayPriorityQueue<Integer>(512000);
                //new OrderedLinkedListPriorityQueue<Integer>();
                //new UnorderedLinkedListPriorityQueue<Integer>();   
        		    new BinaryHeapPriorityQueue<Integer>();
        
        for(int i=0; i < iterations; i++) {
            // build structure first
            pq.clear();
            start = System.currentTimeMillis();   // capture time to build       
            for(int j=0; j < structureSize; j++) {
                int x = (int) (Integer.MAX_VALUE * Math.random());
                pq.insert(x);
                }
            stop = System.currentTimeMillis();
            insertTimes[i] = (stop-start);                
                
            // time for add/remove cycles   
            start = System.currentTimeMillis();  
            for(int j=0; j < workSize; j++) {
                int x = (int) (Integer.MAX_VALUE * Math.random());
                pq.insert(x);            
                pq.remove();
                }
            stop = System.currentTimeMillis();
            cycleTimes[i] = (stop-start);
            
            // time for removal (dequeue) operations
            start = System.currentTimeMillis();
            while(!pq.isEmpty())
                pq.remove();                
            stop = System.currentTimeMillis();
            removeTimes[i] = (stop-start);
            structureSize <<= 1;            
            System.out.println("Loop " + (i+1) + " of " + iterations + " finished.");
            }
         
        // print results   
        int tmp = loopStructureSize;
        System.out.println("\nINSERTION TIMES:");
        for(int i=0; i < iterations; i++) {
            System.out.println("n=" + tmp + "  Time: " + insertTimes[i]);
            tmp <<= 1;
            }
        tmp = loopStructureSize;    
        System.out.println("\nADD/DELETE CYCLE TIMES:");
        for(int i=0; i < iterations; i++) {
            System.out.println("n=" + tmp + "  Time: " + cycleTimes[i]);
            tmp <<= 1;
            }
        tmp = loopStructureSize;   
        System.out.println("\nRemoval TIMES:");
        for(int i=0; i < iterations; i++) {
            System.out.println("n=" + tmp + "  Time: " + removeTimes[i]);  
            tmp <<= 1;
            }                                         
        }
    }