package tokenizing;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WhitespaceTokenizerTest {

    private final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
    private final String input = "Hello, world!\tThis is a test.\n";

    @Test
    void tokenize_lowercase() {

        // Test basic tokenization with default behavior (lowercase)
        assertEquals(List.of("hello,", "world!", "this", "is", "a", "test."), tokenizer.tokenize(input));
        assertEquals(List.of("hello,", "world!", "this", "is", "a", "test."), tokenizer.tokenize(input, true));

        // The punctuation isn't removed
        assertNotEquals("hello", tokenizer.tokenize(input).getFirst());

    }

    @Test
    void tokenize_keepCase() {

        // Test basic tokenization without changing the case
        assertEquals(List.of("Hello,", "world!", "This", "is", "a", "test."), tokenizer.tokenize(input, false));
    }


    @Test
    void tokenize_emptyString() {
        // Test tokenization of an empty string
        assertTrue(tokenizer.tokenize("").isEmpty());
        assertTrue(tokenizer.tokenize("   ", true).isEmpty());
    }


    @Test
    void tokenize_multipleSpaces() {
        // Test tokenization of multiple spaces
        assertEquals(List.of("hello", "world", "this", "is", "a", "test"), tokenizer.tokenize("hello  world  this  is  a  test"));

        // Test tokenization with lowercase
        assertEquals(List.of("hello", "world", "this", "is", "a", "test"), tokenizer.tokenize("hello  world  this  is  a  test", true));
    }


}