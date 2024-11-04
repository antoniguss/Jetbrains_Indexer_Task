package tokenizing;

import java.util.List;

/**
 * A simple interface outlining the required functionality of a tokenizer.
 * While the interface is very simple,
 * it can be implemented to support various more or less complex tokenization algorithms
 */
public interface Tokenizer {
    /**
     * Tokenizes a given text string into a list of tokens.
     * @param text The text to tokenize.
     * @return A list of string tokens.
     */
    List<String> tokenize(String text);

}
