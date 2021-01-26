import java.io.IOException;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

/**
 * MarkovMatrixDict
 *
  * This is a more advanced Markov Dictionary algorithm that uses more than one word as a k length to determine the
 * resultant next word, resulting in better sentence structure and slightly more follow-able stories. (Hopefully)...
 *
 * Author: JH
 * Date: 1-25-21
 * -JBH
 */
public class MarkovMatrixDict {
    //INSTANCE-DATA
    private static Random r = new Random();

    //CONSTRUCTOR
    //default-constructor

    //METHODS
    private static String markov(String filePath, int numWordsInKey, int outputSize, double alpha) throws IOException {
        int numAlphas = 0;
        int numShifts = 0;
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        String[] words = new String(bytes).trim().split(" ");
        Map<String, ArrayList<String>> dict = new HashMap<>();
        System.out.println("Number of Words: " + words.length);
        for (int i = 0; i < (words.length - numWordsInKey); i++) {
            //This loop builds the keys
            StringBuilder curKey = new StringBuilder(words[i]);
            for (int j = i + 1; j < i + numWordsInKey; j++) {
                curKey.append(" ").append(words[j]);
            }
            //This one builds the values
            String value = (i + numWordsInKey < words.length) ? words[i + numWordsInKey] : "";
            if (!dict.containsKey(curKey.toString())) {
                //NEW KEY/PREFIX
                ArrayList<String> valueList = new ArrayList<>();
                valueList.add(value);
                dict.put(curKey.toString(), valueList);
            } else {
                //Add value to suffix list
                dict.get(curKey.toString()).add(value);
            }
        }
        System.out.println("Dict size: "+ dict.size());
        int numOutputed = 0;
        int randomInt = r.nextInt(dict.size());
        //Get a random starting prefix
        String prefix = "";
        int iterations = 0;
        while(!prefix.startsWith("Act ")) {
            prefix = (String) dict.keySet().toArray()[randomInt];
            randomInt = r.nextInt(dict.size());
            if(iterations % 10000 == 0){
                System.out.println("Loading...");
            }
            iterations++;
        }
        //init capacity = # of items in randomly selected prefix
        ArrayList<String> output = new ArrayList<>(Arrays.asList(prefix.split(" ")));
        boolean alphaEngaged = false;
        String tempPrefix = "";
        while(true) {
            alphaEngaged = (r.nextDouble() <= alpha);
            //If it's an alpha, a random suffix is chosen rather than a plausible one
            List<String> suffixList = dict.get(prefix);
            if (alphaEngaged) {
                tempPrefix = (String) dict.keySet().toArray()[r.nextInt(dict.size())];
                String[] prefixSeparated = tempPrefix.split(" ");
                numOutputed += numWordsInKey;
                for (int i = 0; i < numWordsInKey; i++) {
                    output.add(prefixSeparated[i]);
                }
                numAlphas++;
            } else if (suffixList.size() == 1) {
                //Only one possible suffix
                if (Objects.equals(suffixList.get(0), "")){
                    System.out.println("Number of Alphas: " + numAlphas);
                    System.out.println("Number of dream-shifts: " + numShifts);
                    System.out.println("\n\n-BEGIN GENERATED DOCUMENT-\n\n");
                    return output.stream().reduce("", (a, b) -> a + " " + b);
                }
                output.add(suffixList.get(0));
            } else {
                //More than one possible suffix
                randomInt = r.nextInt(suffixList.size());
                output.add(suffixList.get(randomInt));
                numShifts++;
            }
            if (output.size() >= outputSize) {
                System.out.println("Number of Alphas: " + numAlphas);
                System.out.println("Number of dream-shifts: " + numShifts);
                System.out.println("\n\n-BEGIN GENERATED DOCUMENT-\n\n");
                return output.stream().limit(outputSize).reduce("", (a, b) -> a + " " + b);
            }
            //Update counter
            if(!alphaEngaged){
                numOutputed++;
                prefix = output.stream().skip(numOutputed).limit(numWordsInKey).reduce("", (a, b) -> a + " " + b).trim();
            }
            else{
                prefix = tempPrefix;
            }
        }
    }
    public static void main(String[] args) throws IOException {
        System.out.println(markov("SpanishTrainer.txt",2,200,0.0));
    }
}