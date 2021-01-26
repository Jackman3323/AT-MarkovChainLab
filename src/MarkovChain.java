import java.io.FileNotFoundException;

/**
 * MarkovChain.java
 *
 * This is my version of a Markov Chain. It is an AI algorithm that trains itself with an inputted file and learns what
 * words typically follow any given word, then it chooses a starting word and at random chooses a following word, so on
 * and so forth until the desired length story is achieved.
 *
 * Author: Jack Hughes
 * Date: 1-21-21
 * -JBH
 */
public class MarkovChain {
    //INSTANCE-DATA
    //An object that organizes the many word objects
    private MarkovDict dict;
    //The path to the training file
    private String filepath;

    //CONSTRUCTORS
    //String filepath: the path to the training .txt file
    public MarkovChain(String filepath){
        this.filepath = filepath;
        this.dict = new MarkovDict(filepath);
    }
    //METHODS
    public String generateText(int desiredNumWords) throws FileNotFoundException {
        return this.dict.outputString(desiredNumWords);
    }

    public String getFilepath() {
        return filepath;
    }
}
