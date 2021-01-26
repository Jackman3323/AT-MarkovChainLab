import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * MarkovDict.java
 *
 * This is a class that stores words and organizes them for use by the main class
 *
 * Author: Jack Hughes
 * Date: 1-21-21
 * -JBH
 */
public class MarkovDict {
    //INSTANCE-DATA
    //Number of words in the dictionary
    private int numWords;
    //Path to the training file
    private File trainingFile;
    //ArrayList of all the values to simplify initialization
    private ArrayList<String> values;
    //ArrayList of all keys to simplify returning a random one
    private ArrayList<String> keys;
    //ArrayList of all the MarkovWords (the final dictionary)
    private ArrayList<MarkovWord> finalDict;

    //CONSTRUCTORS
    //String filepath: the path to the training file
    public MarkovDict(String filepath){
        this.trainingFile = new File(filepath);
        this.numWords = 0;
        this.values = new ArrayList<String>();
        this.keys = new ArrayList<String>();
        this.finalDict = new ArrayList<MarkovWord>();
    }
    
    //METHODS
    private void parseTrainingFile() throws FileNotFoundException {
        Scanner fileScan = new Scanner(System.in);
        try {
            fileScan = new Scanner(this.trainingFile);
        } catch(FileNotFoundException fileDoesNotExist){
            System.out.println("ERROR: THE GIVEN FILEPATH DID NOT PRODUCE A FILE");
            System.exit(0);
        }
        String cur = "";
        fileScan.useDelimiter(" ");
        String previous = fileScan.next();
        while(fileScan.hasNext()){
            cur = fileScan.next();
            this.values.add(cur);
            this.keys.add(previous);
            previous = cur;
        }
    }
    private void compileDictionary(){
        ArrayList<String> seenKeys = new ArrayList<String>();
        for(int i = 0; i < this.keys.size(); i++){
            if(this.keys.get(i).equals("SKIP_ONE_CYCLE")){
                //Do nothing, this is for putting multiple scripts in the same file.
            }
            else if(!seenKeys.contains(this.keys.get(i))){
                //New word detected!
                this.finalDict.add(new MarkovWord(this.keys.get(i),this.values.get(i),this.finalDict.size()+1));
                seenKeys.add(this.keys.get(i));
                this.numWords++;
//                System.out.print("NEW WORD: " + this.keys.get(i));
//                System.out.println(". FOLLOWER: " + this.values.get(i));
            }
            else{
//                System.out.print("REPEAT WORD: " + this.keys.get(i));
                //System.out.println(". FOLLOWER: " + this.values.get(i));
                //Repeat word! Add this instance's follower word
                int index = seenKeys.indexOf(this.keys.get(i));
                this.finalDict.get(index).addFollower(this.values.get(i));
            }
        }
    }

    private MarkovWord getRandomWord(){
        return this.finalDict.get((int)(Math.random() * numWords));
    }

    private MarkovWord findWordObject(String word){
        for(int i = 0; i < this.numWords; i++){
            if(this.finalDict.get(i).getWord().equals(word)){
                //System.out.println("TEST" + this.finalDict.get(i).getWord());
                return this.finalDict.get(i);
            }
        }
        return null;
    }

    public String outputString(int desiredNumWords) throws FileNotFoundException {
        this.parseTrainingFile();
        this.compileDictionary();
        MarkovWord cur = this.getRandomWord();
        String follower;
        StringBuilder finalOutput = new StringBuilder();
        for(int i = 1; i < desiredNumWords; i++){
            finalOutput.append(cur.getWord());
            finalOutput.append(" ");
            follower = cur.getRandomFollower();
            cur = this.findWordObject(follower);
        }
        return finalOutput.toString();
    }
    public int getNumWords(){
        return this.numWords;
    }
}
