package tokenizing;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WhitespaceTokenizerTest {

    @Test
    void tokenize() {
        WhitespaceTokenizer indexer = new WhitespaceTokenizer();
        String input = "Hello, world!\tThis is a test.\n";


        // Test basic tokenization with default behavior (lowercase)
        assertEquals(List.of("hello,", "world!", "this", "is", "a", "test."), indexer.tokenize(input));
        assertEquals(List.of("hello,", "world!", "this", "is", "a", "test."), indexer.tokenize(input, true));

        // Test basic tokenization without changing the case
        assertEquals(List.of("Hello,", "world!", "This", "is", "a", "test."), indexer.tokenize(input, false));

    }
}