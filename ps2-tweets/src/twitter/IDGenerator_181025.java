package twitter;

import java.util.Random;
import java.io.File;
import java.time.Instant;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * This class Produces random ID between 0 < id <50
 * and stores it in a HashSet 
 * */
public class IDGenerator_181025 {

    private final static int CAPACITY = 50;
    private static final Set<Long> reg = new HashSet<Long>((CAPACITY), 1.0f);
    private long id;

    private enum Direction {
        LEFT, RIGHT
    };

    //
    IDGenerator_181025(boolean bo) {
        if (bo)
            makeIDfast();
        else
            makeID();
    }

    IDGenerator_181025() {
        makeID();
    }

    /*
     * A straightforward way to obtain an unique number between 0 < id < CAPACITY
     * which could be used for a reasonably small / short sets, up to CAPACITY / 2;
     * Otherwise it becomes inefficient with regard to the time.
     */
    private void makeID() {

        while (true) {
            Instant toSeed = Instant.now();
            long seedRandom = toSeed.getEpochSecond();
            Random numSpitter = new Random(seedRandom);
            long idMould = numSpitter.nextLong();
            long idCandidate = Math.abs(idMould % CAPACITY);

            if (!reg.contains(idCandidate)) {
                reg.add(idCandidate);
                id = idCandidate;
                return;
            }
        }
    }

    /*
     * A method to efficienly distribute the numbers (IDs) over the whole capacity
     * of the Registry
     */

    private void makeIDfast ()  {         
       
        while(true) {
           // if (checkSize())    { return; }    
            Instant toSeed = Instant.now();
            long seedRandom = toSeed.getEpochSecond();
            Random numSpitter = new Random(seedRandom);
            long idMould = numSpitter.nextLong();
            long idCandidate = Math.abs( idMould  % CAPACITY);
          //  System.out.println("# idCandi" + idCandidate);
            if (idCandidate == 0) idCandidate = (long) CAPACITY;
             
            if (!reg.contains(idCandidate) ) {
               addCandidate(idCandidate);
               return;
            } 
           
            else {
                Direction iterate = Math.random() <= 0.5 ? Direction.LEFT : Direction.RIGHT;
                long idx = idCandidate;
                while (idx == idCandidate) {
                  //  if (checkSize())    { return; }    
                    switch (iterate) { 
                    case LEFT: 
                            idx = idx -2;
                           while (reg.contains(idx) && idx  > 0) {                           
                               idx--;                               
                           }
                           if (idx <= 0) {
                               if ((CAPACITY -reg.size() <= 2)
                                       && !reg.contains(idCandidate - 1)) {
                                       if(!reg.contains(idCandidate + 1)) {
                                   idx = idCandidate +1;
                                   addCandidate(idx);
                                   break;
                                   }
                                      
                               }
                               else if ((CAPACITY -reg.size() ==1) 
                                       && !reg.contains(idCandidate + 1)) {
                                   idx = idCandidate +1;
                                   addCandidate(idx);  
                                   
                               }
                               iterate = Direction.RIGHT;
                               idx = idCandidate;                             
                           }
                           if (idx!= idCandidate) {addCandidate(idx);   }                       
                           break;
                    case RIGHT:
                        idx = idx + 2;
                        while (reg.contains(idx) && idx <= CAPACITY) {                           
                            idx++;                           
                        }
                        if (idx > CAPACITY) {
                            if ((CAPACITY -reg.size() <= 2)
                                && !reg.contains(idCandidate + 1)) {
                                if ( !reg.contains(idCandidate - 1)) {
                                idx = idCandidate - 1;
                                addCandidate(idx);
                                break;
                                }
                            }
                            else if ((CAPACITY -reg.size() ==1) 
                                    && !reg.contains(idCandidate - 1)) {
                                idx = idCandidate -1;
                                addCandidate(idx);  
                                
                            }
                            iterate = Direction.LEFT;
                            idx = idCandidate;                          
                        }
                        if (idx!= idCandidate)  { addCandidate(idx);    }                   
                        break;                     
                  
                    } 
                 //   System.out.println(idx + " " + idCandidate);
                 //   System.out.println("# " + IDGenerator.size());
                    writeToFile(idx + " " + "idCandi " + idCandidate + " size : " + size());
                   // return;
                }
              //  return;
            } 
            return;
        }
        
      //  return;
    }

    /*
     * Adds a (long) to the HashSet
     */

    private void addCandidate(long idValue) {

        if (checkSize()) {
            return;
        }
        if (!reg.contains(idValue) && idValue > 0 && idValue <= CAPACITY) {
            reg.add(idValue);
            writeToFile("# " + idValue + " |" + reg.size());
            id = idValue;
            return;
        } else {
            return;
        }
    }

    /*
     * 
     * */

    private void roll(long proMember) {

    }

    /*
     * no adding if it would exceed the CAPACITY. if TRUE, CAPACITY is reached => =>
     * there is no more place !
     */
    private boolean checkSize() {

        if (reg.size() == CAPACITY) {
            System.out.println("Registry is full");
            System.out.println("Tomorrow we will deliver more IDs.");
            return true;
        } else {
            return false;
        }
    }

    /*
     * returns the id of the current class instance
     */
    public long getID() {
        return id;
    }

    /*
     * returns the size of the set
     */
    public static int size() {

        return reg.size();
    }

    private void writeToFile(String content) {

        try {
            String pathTo = new File(".").getCanonicalPath();
            // !!!
            // Path adjustment might be necessary on other platform
            File input = new File(pathTo + "\\src\\twitter\\debug.txt");
            // If file doesn't exists, then create it
            if (!input.exists()) {
                input.createNewFile();
            }
            FileWriter fw = new FileWriter(input, true);
            BufferedWriter bw = new BufferedWriter(fw);
            // Write in file
            bw.append(content + "\r");
            bw.close();
            return;
        } catch (IOException e) {
            return;
        }

    }

    /*
     * Iterates over the set
     */
    public static void list() {

        Iterator<Long> iter = reg.iterator();
        while (iter.hasNext()) {
            System.out.print(" || " + iter.next());
        }
    }

    public static void main(String[] args) {

        for (int i = 1; i <= 50; i++) {
            IDGenerator_181025 idTweet = new IDGenerator_181025(true);
            System.out.println("# " + i + "| " + idTweet.getID() + " * " + reg.size() + " || ");
        }
        IDGenerator_181025.list();
        System.out.println("# " + IDGenerator_181025.size());
    }
}
