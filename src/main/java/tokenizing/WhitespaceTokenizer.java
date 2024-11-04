package tokenizing;

import java.util.List;

public class WhitespaceTokenizer implements Tokenizer {
    /**
     * Tokenizes a string into a list of tokens. By default, the tokens are changed to lowercase.
     * @param text The text to be tokenized.
     * @return A list of tokens.
     */
    @Override
    public List<String> tokenize(String text) {
        return tokenize(text, true);
    }

    /**
     * Tokenizes a string into a list of tokens. If the lowercase parameter is set to true, the tokens are changed to lowercase.
     * @param text The text to be tokenized.
     * @param lowercase Whether to lowercase the tokens.
     * @return A list of tokens.
     */
    public List<String> tokenize(String text, boolean lowercase) {

        if (text.isEmpty()) {
            return List.of();
        }

        List<String> tokens = List.of(text.split("\\s+"));
        if (lowercase) {
            tokens = tokens.stream().map(String::toLowerCase).toList();
        }

        return tokens;

    }


}
