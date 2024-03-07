
import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). 
     * @throws Exception */
	public void train(String fileName) throws Exception {
           In reader = new In(fileName); 
            CharDataMap = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
            throw new Exception("Error reading the file");
    }
    
    private void processLine(String line) {
        for (int i = 0; i <= line.length() - windowLength; i++) {
            String window = line.substring(i, i + windowLength);
            char nextChar = (i + windowLength < line.length()) ? line.charAt(i + windowLength) : '\0';
    
            // Update CharDataMap based on the window and nextChar
            // You will need to handle cases where the window is seen for the first time or not
        }
    }

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {
        if (probs == null || probs.getFirstNode() == null) {
            throw new IllegalArgumentException("The probability list is null or empty");
        }
        // Calculate the total number of characters
        int totalCounter = 0;
        Node current = probs.getFirstNode();
        while (current != null) {
            totalCounter += current.cp.count;
            current = current.next;
        }
    
        // Calculate and set the probabilities (p and cp fields) of all the characters in the list
        current = probs.getFirstNode();
        double cumulativeProbability = 0.0;
        while (current != null) {
            // Calculate probability (p field)
            current.cp.p = (double) current.cp.count / totalCounter;
    
            // Calculate cumulative probability (cp field)
            cumulativeProbability += current.cp.p;
            current.cp.cp = cumulativeProbability;
    
            current = current.next;
        }
    }
    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
        if (probs == null || probs.getFirstNode() == null) {
            throw new IllegalArgumentException("The probability list is null or empty");
        }
    
        double randomValue = randomGenerator.nextDouble();
        char result = 0;  // default value
    
        Node current = probs.getFirstNode();
        while (current != null) {
            if (randomValue < current.cp.cp) {
                result = current.cp.chr;
                break;
            }
            current = current.next;
        }
    
        if (result == 0) {
            throw new IllegalStateException("Unable to determine a random character");
        }
    
        return result;
    }

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
        StringBuilder generatedText = new StringBuilder(initialText);
        for (int i = 0; i <textLength; i++) {
            String window = generatedText.substring(generatedText.length() - windowLength);
            
            if (CharDataMap.containsKey(window)) {
                List charList = CharDataMap.get(window);

                if (charList!=null) {
                    char nextChar = getRandomChar(charList);
                    generatedText.append(nextChar);
                } else {
                    // Handle the case where the list is empty
                    break;
                }
            } else {
                // Handle the case where the window is not found in the map
                break;
            }
        }

        return generatedText.toString();
    }


    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
		List mylist =new List ();
        mylist.addFirst(' ');
        mylist.addFirst('e');
        mylist.addFirst('e'); 
        mylist.addFirst('t'); 
        mylist.addFirst('t');
        mylist.addFirst('i');
        mylist.addFirst('m'); 
        mylist.addFirst('m'); 
        mylist.addFirst('o');
        mylist.addFirst('c');
    }
}
