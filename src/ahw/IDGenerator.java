package ahw;

import java.util.Random;
import java.time.Instant;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * This class Produces random ID between 0 < id < CAPACITY
 * and stores it in a HashSet .
 * The ID is initially proposed through Math.random(), and, when already available in Set,
 * is being iterated to a vacant hole .  
 * It also gives the possibility to keep track of the records in a ".txt" file.
 * */
public class IDGenerator {

    private final static int CAPACITY = 50;
    private static final Set<Long> reg = new HashSet<Long>((CAPACITY), 1.0f);
    private long id;

    enum Direction { LEFT (-1), RIGHT (1) ;
    private final int increment;
    Direction(int increment) { this.increment = increment; } 
    // public oder private
    private int increase() {   return increment; }
    private Direction swap() { 
        if (this == Direction.LEFT) {  return Direction.RIGHT ;   }   
        return Direction.LEFT; 
        }
    };
   
    /*
     * when the initial random Candidate for ID field happens to be already part
     * of the Set, instead of taking up the place immediately next to the Candidate,
     * we can bounce a little bit, so that better "randomness" to be achieved .
     * This enum determines how long the bounce will be.
     * */
       enum Distance { LONG (17) , MIDDLE (13), SHORT(7) , NONE(2)    ;
         private final int jump;
         Distance(int num) { this.jump =  num;  }
         int getJump() {  return jump; }
        static Distance spring() {
            for (Distance x : Distance.values()) {
                if ((CAPACITY - reg.size()) > x.getJump() ) {
                   return x;
                }
            }
            return NONE;
        }
    };

    IDGenerator() {
        makeID();
    }
    
    /*
     * For debugging or test purposes only .
     * */
    IDGenerator(long x) {
        this.id = x;
        reg.add(x);
    }
    
    /*
     * For debugging or test purposes only .
     * */
    public static void setReg(long[] vals) {
        
        for(long x : vals)
        new IDGenerator(x);
        return;
    }

    /*
     * A method which distributes the numbers (IDs) over the full length
     * of the Registry   
     * */
    private void makeID ()  {         
        
        while(true) {
            if (checkSize())    { return; }    
            Instant toSeed = Instant.now();
            long seedRandom = toSeed.getEpochSecond();
            Random numSpitter = new Random(seedRandom);
            long idMould = numSpitter.nextLong();
            long idCandidate = Math.abs( idMould  % CAPACITY);

            if (idCandidate == 0) idCandidate = (long) CAPACITY;
             
            if (!reg.contains(idCandidate) ) {
               addCandidate(idCandidate);
               return;
            } 
           
            else {
                Direction iterate = Math.random() <= 0.5 ? Direction.LEFT 
                        : Direction.RIGHT;
                long idx = idCandidate;
                bounce(idx, iterate);
                return;
            }            
        }
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
            id = idValue;
            writeToFile(" #  ID " + getID() + 
            " added. There are " + reg.size() + " IDs in total. ");            
            return;
        } else {
            return;
        }
    }

    /*
     * If the intial Candidate has already been added to the Set, then we "roll" it
     * downward and upward, to find a free value;
     * Here it is done by recursion;
     * */
    private boolean roll(long proMember, Direction towards) {
        // if (checkSize())    { return false; }
         
         if (proMember  <= 0 
             || (proMember ) > CAPACITY ) {
             
             return false;
            }
          if(!reg.contains(proMember )) {
              addCandidate(proMember );
             return true;
         }
         else {
            
             while(reg.contains(proMember)) {
                 return  roll(proMember + towards.increase(), towards);
             }
         }
          return false;
     }
    
    /*
     * Provides for more versatile distribution of the IDs.
     * */
    public void bounce(long proMember, Direction towards) {
        
        Long prM = proMember;
        Direction dir = towards;
        Distance offset; 
        offset = Distance.spring();
       
        if(!roll(prM + dir.increase() * offset.getJump(), dir)) {
            dir = dir.swap();
            if (!roll(prM + dir.increase() * offset.getJump(), dir)) {
                if (prM - dir.increase() * offset.getJump() <= 0) {
                    prM = CAPACITY - (long) offset.getJump();
                }
                if (prM - dir.increase() * offset.getJump() > CAPACITY) {
                    prM = (long) offset.getJump();
                }
                roll(prM - dir.increase() * offset.getJump(), dir);
            }
        }
       return ;
    }
    
    /*
     * no adding if it would exceed the CAPACITY. if TRUE, CAPACITY is reached => =>
     * there is no more place !
     */
    private boolean checkSize() {

        if ((reg.size() + 1) > CAPACITY) {
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
            File input = new File(pathTo + "\\src\\ahw\\debug.txt");
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
        int i = 0;
        while (iter.hasNext()) {
            i++;
            System.out.println(" # The " + i + ". ID is : " + iter.next());
        }
    }

    public static void main(String[] args) {
    
        for (int i = 1; i <= 60; i++) {
            IDGenerator idTweet = new IDGenerator();
            System.out.println(" # On the " + i + ".  iteration : ID " + idTweet.getID() + 
                    " added \r\t\t" + reg.size() + " IDs in total. ");
         }
        IDGenerator.list();
        System.out.print("\r\r Set contains # " + IDGenerator.size() + " # IDs . ");
    }   
}
