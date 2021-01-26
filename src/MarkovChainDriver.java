import java.io.FileNotFoundException;

/**
 * MarkovChainDriver.java
 *
 * This is a driver file for my AI script creation algorithm.
 *
 * Author: Jack Hughes
 * Date: 1-25-21
 * -JBH
 */
public class MarkovChainDriver {
    public static void main(String[] args) throws FileNotFoundException {
        MarkovChain mc = new MarkovChain("SpanishTrainer.txt");
        System.out.println("The training file is: " + mc.getFilepath());
        System.out.println("\n\n-BEGIN GENERATED DOCUMENT-\n\n");
        System.out.println(mc.generateText(1000));
    }
}
