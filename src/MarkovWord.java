import java.util.ArrayList;

/**
 * MarkovWord.java
 *
 * This class was added to increase the coder-friendliness of my MarkovDict class. It assists in sorting out repeat
 * words and adds other quality of code improvements.
 *
 * Author: Jack Hughes
 * Date: 1-25-21
 * -JBH
 */
public class MarkovWord {
    //INSTANCE-DATA
    //int dictIndex--the index this word is in the dictionary
    private int dictIndex;
    //String word--the word that this class contains
    private String word;
    //ArrayList<String> followers--the words that follow this word in the training file
    private ArrayList<String> followers;

    //CONSTRUCTOR
    public MarkovWord(String word, String firstFollower, int index){
        this.word = word;
        this.dictIndex = index;
        this.followers = new ArrayList<String>();
        this.followers.add(firstFollower);
    }

    //METHODS
    public int getDictIndex() {
        return dictIndex;
    }
    public String getWord() {
        return word;
    }
    public void addFollower(String follower){
        this.followers.add(follower);
    }
    public ArrayList<String> getFollowers(){
        return this.followers;
    }
    public String getRandomFollower(){
        return this.followers.get((int)(Math.random() * this.followers.size()));
    }
}
