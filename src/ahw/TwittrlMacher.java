/*The "HelperFile.txt", used in the class below.
Enumeration names = {

Andy,
Bertold,
Ceco,
Darius,
Emil,
Farad,
Gerhard,
Hares,
Ivory,
Jasper
}

Enumeration secondPlace = {

is,
has,
visits,
buys,
sells,
feels like,
tolks to
}

Enumeration thirdPlace_2345 = {

haus,
car, 
cat,
dress,
notebook,
phone,
cupboard,
wardrobe,
garrage,
book
}

Enumeration thirdPlace_1 = {

beautiful,
ugly,
smart,
stupid,
fluffy,
fleecy,
well-dressed,
free
}

Enumeration fourthPlace = {

says,
thinks,
admits,
confesses,
deems,
takes into consideration, 
reveals
}
 * */

package ahw;
import java.io.*;

/*
 * This class produces Tweets.
 * */
public class TwittrlMacher {

    private  FileReader Groups;
    private BufferedReader Stringify; 
    private enum Mood {SOCIAL, THINGS, QUALITIES, UNDEFINED};
    private enum Wordy {LONG, SHORT};
    private Mood mood = Mood.UNDEFINED;
    private Wordy wordy;
    
    
  TwittrlMacher()
  {
      
      System.out.println(jetztTwitten());
  }   
    
 /*
  *  A helper method, that chooses one word for every place in the sentence 
  * and takes as a basis the "HelperFile.txt" file, 
  * which contains "Enumerations" -
  * sets of appropriate words for each place in the sentence.
  *  !!!!! on other platform requires an adjustment of the path to the 
  * "HelperFile.txt"  file !
  */  
    
    public  String getWord(int n) {
        
        
        String buff = "";
        String[] PartOfSentence ;
        int place = 0;    
        
        try {      
            String pathTo = new File(".").getCanonicalPath();    
            
            // !!!
            //Path adjustment might be necessary on other platform
            File input = new File(pathTo + "\\src\\ahw\\HelperFile.txt");    
            Groups = new FileReader(input);
        }        
        catch(FileNotFoundException e) {            
            System.out.println(e);
            return "";
        }
        catch(IOException e) {            
            System.out.println(e);
            return "";
        }
       
        Stringify = new BufferedReader(Groups);
       
        try {
        while (((buff = Stringify.readLine()) != null) )
        {   
            if (buff.contains("Enumeration")) {
                place++;
                if (place == n) {
                    do {
                    buff += Stringify.readLine();
                       } 
                    while(!buff.contains("}"));
                           PartOfSentence  = buff.split(",");
                               for  ( String word : PartOfSentence) {
                                      }
                                    int idx = PartOfSentence[0].indexOf("{");
                                    PartOfSentence[0] = PartOfSentence[0].substring(idx+1);
                                    idx =  PartOfSentence[PartOfSentence.length-1].indexOf("}");
                                    PartOfSentence[PartOfSentence.length-1] = 
                                    PartOfSentence[PartOfSentence.length-1].substring(0, idx);
                                    
                                    idx = (int) Math.ceil(Math.random() * PartOfSentence.length);
                                    Stringify.close();
                                return PartOfSentence[idx-1];            
                        }
                else {                    
                        continue;
                }
            }
        }
        }
        catch(IOException e) {            
            System.out.println(e);
            return "";
        }
   
        return "";
        }
    
    /*
     * A helper method, which predefines the construct of the sentence with
     * respect to the "mood" of the twitting person.
     * */
    
    private String nuance() {
        
        String verb = getWord(2);
        verb.trim();
        if (verb.compareTo("visits") == 0 
         || verb.compareTo("tolks to") == 0) {
            mood = Mood.SOCIAL;           
        }
        else if (verb.compareTo("has") == 0 
                || verb.compareTo("buys") == 0
                || verb.compareTo("sells") == 0) {
                   mood = Mood.THINGS;           
               }
        else if ( verb.compareTo("is") == 0) {
                   mood = Mood.QUALITIES;           
               }
        wordy =  Math.random() < 0.35 ? Wordy.SHORT : Wordy.LONG;
        
        return verb;
    }
    
    /*
     * A helper method, which actually constructs the Tweet
     * */
    private String jetztTwitten()  {
         String myTweet ;
         myTweet = "@" + getWord(1) + " " + nuance() + " ";
         switch(mood) {
            case SOCIAL:
                myTweet += "@" + getWord(1);
                break;
            case THINGS : 
                myTweet += getWord(3);
                break;
            case QUALITIES :
                myTweet += getWord(4);
                break;
            case UNDEFINED : 
                int idx = (int) Math.ceil(Math.random() * 5);
                myTweet += getWord(idx);
            }  
        
            if (wordy == Wordy.LONG) {
                myTweet += ", " + getWord(5) + " @" + getWord(1); 
            }
        return myTweet;
    }
    
    public static void main(String[] args) {
        
        for (int i = 0; i < 10; i++) {
      TwittrlMacher twm = new TwittrlMacher();
        }
    }
}


