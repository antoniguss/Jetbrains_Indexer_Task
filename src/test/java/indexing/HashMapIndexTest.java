package indexing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HashMapIndexTest {

    private HashMapIndex index;
    private File file1;
    private File file2;
    private File file3;

    @BeforeEach
    void setUp() throws IOException {
        index = new HashMapIndex();

        // Create temporary files for testing
        file1 = Files.createTempFile("testFile1", ".txt").toFile();
        file2 = Files.createTempFile("testFile2", ".txt").toFile();
        file3 = Files.createTempFile("testFile3", ".txt").toFile();

        // Clean up temporary files on exit
        file1.deleteOnExit();
        file2.deleteOnExit();
        file3.deleteOnExit();
    }

    @Test
    void testAddToIndex() {
        index.addToIndex("token1", file1);
        index.addToIndex("token1", file2);
        index.addToIndex("token2", file3);

        Set<File> filesForToken1 = index.search("token1");
        assertTrue(filesForToken1.contains(file1));
        assertTrue(filesForToken1.contains(file2));
        assertEquals(2, filesForToken1.size());

        Set<File> filesForToken2 = index.search("token2");
        assertTrue(filesForToken2.contains(file3));
        assertEquals(1, filesForToken2.size());
    }

    @Test
    void testSearchNonExistentToken() {
        Set<File> result = index.search("nonExistentToken");
        assertTrue(result.isEmpty());
    }

    @Test
    void testRemoveFileFromIndex() {
        index.addToIndex("token1", file1);
        index.addToIndex("token1", file2);
        index.addToIndex("token2", file3);

        // Remove file1 from the indexed files
        index.removeFileFromIndex(file1);

        // Search for token1
        Set<File> result = index.search("token1");
        assertTrue(result.contains(file2));
        assertFalse(result.contains(file1)); // file1 should not be found
    }

    @Test
    void testClearIndex() {
        index.addToIndex("token1", file1);
        index.addToIndex("token2", file2);
        index.clearIndex();

        // After clearing, the index should be empty
        assertTrue(index.getIndexedFiles().isEmpty());
        assertTrue(index.search("token1").isEmpty());
        assertTrue(index.search("token2").isEmpty());
    }

    @Test
    void testGetIndexedFiles() {
        index.addToIndex("token1", file1);
        index.addToIndex("token2", file2);

        Set<File> indexedFiles = index.getIndexedFiles();
        assertEquals(2, indexedFiles.size());
        assertTrue(indexedFiles.contains(file1));
        assertTrue(indexedFiles.contains(file2));
    }

}
