package tokenizing;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WhitespaceTokenizerTest {

    @Test
    void tokenize() {
        WhitespaceTokenizer indexer = new WhitespaceTokenizer();
        String input = "Hello, world!\tThis is a test.\n";
        List<String> tokens = indexer.tokenize(input);
        assertEquals(List.of("Hello,", "world!", "This", "is", "a", "test."), tokens);
    }
}