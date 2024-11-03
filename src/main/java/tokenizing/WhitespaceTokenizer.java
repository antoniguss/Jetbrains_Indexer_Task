package tokenizing;

import java.util.List;

public class WhitespaceTokenizer implements Tokenizer {
    /**
     * Tokenizes a string into a list of tokens. By default, the tokens are changed to lowercase..
     * @param text The text to be tokenized.
     * @return A list of tokens.
     */
    @Override
    public List<String> tokenize(String text) {
        return tokenize(text, true);
    }

    public List<String> tokenize(String text, boolean lowercase) {

        List<String> tokens = List.of(text.split("\\s+"));
        if (lowercase) {
            tokens = tokens.stream().map(String::toLowerCase).toList();
        }

        return tokens;

    }


}
