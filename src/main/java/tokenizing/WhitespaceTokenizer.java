package tokenizing;

import java.util.List;

public class WhitespaceTokenizer implements Tokenizer {
    @Override
    public List<String> tokenize(String text) {
        return List.of(text.split("\\s+"));
    }


}
