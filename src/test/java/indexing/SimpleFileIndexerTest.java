package indexing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.FileHandling;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SimpleFileIndexerTest {

    private SimpleFileIndexer indexer;
    private File textFile1;
    private File textFile2;
    private File nonTextFile;

    @BeforeEach
    void setUp() throws IOException {
        indexer = new SimpleFileIndexer();

        // Create temporary files for testing
        textFile1 = Files.createTempFile("testFile1", ".txt").toFile();
        Files.writeString(textFile1.toPath(), "Hello, world! This is a test file.");
        textFile1.deleteOnExit();

        textFile2 = Files.createTempFile("testFile2", ".txt").toFile();
        Files.writeString(textFile2.toPath(), "Hello, world! This is a different test file.");
        textFile2.deleteOnExit();

        // Create a temporary non-text file for testing
        nonTextFile = Files.createTempFile("testImage", ".png").toFile();
        // Optionally, write some bytes to simulate a non-text file
        Files.write(nonTextFile.toPath(), new byte[]{1, 2, 3});
        nonTextFile.deleteOnExit();
    }

    @Test
    void testIndexFile_validFile() {
        assertTrue(indexer.indexFile(textFile1));
        Set<File> result = indexer.search("hello,");
        assertTrue(result.contains(textFile1));
        assertEquals(1, result.size());
    }

    @Test
    void testIndexFile_nonTextFile() {
        // We make sure the file is not a text file
        assertFalse(FileHandling.isTextFile(nonTextFile));

        assertFalse(indexer.indexFile(nonTextFile));
        // Ensure that the index is still empty
        assertTrue(indexer.search("hello,").isEmpty());
    }


    @Test
    void testIndexFile_withNonExistentFile() {
        assertFalse(indexer.indexFile(new File("nonExistentFile.txt")));
        // Ensure that the index is cleared after failure
        assertTrue(indexer.search("hello,").isEmpty());
    }

    @Test
    void testIndexFiles_multipleFiles() throws IOException {
        File anotherTextFile = null;
        try {
            anotherTextFile = Files.createTempFile("anotherTestFile", ".txt").toFile();
            Files.writeString(anotherTextFile.toPath(), "Another test file.");
            anotherTextFile.deleteOnExit();

            assertTrue(indexer.indexFiles(textFile1, anotherTextFile));
            Set<File> result = indexer.search("another");
            assertTrue(result.contains(anotherTextFile));
            assertEquals(1, result.size());
        } finally {
            if (anotherTextFile != null) {
                anotherTextFile.delete();
            }
        }
    }

    @Test
    void testIndexFiles_withNonTextFile() {
        // We make sure the file is not a text file
        assertFalse(FileHandling.isTextFile(nonTextFile));

        assertFalse(indexer.indexFiles(textFile1, nonTextFile));
        // Ensure that the index is cleared after failure
        assertTrue(indexer.search("hello,").isEmpty());
    }

    @Test
    void testClearIndex() {
        indexer.indexFile(textFile1);
        assertFalse(indexer.search("hello,").isEmpty());
        indexer.clearIndex();
        assertTrue(indexer.search("hello,").isEmpty());
    }

    @Test
    void testSearch_nonExistentKeyword() {
        indexer.indexFile(textFile1);
        Set<File> result = indexer.search("nonExistent");
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetIndexedFiles() {
        // Index the first file
        indexer.indexFile(textFile1);
        // Index the second file
        indexer.indexFile(textFile2);

        // Retrieve indexed files
        Set<File> indexedFiles = indexer.getIndexedFiles();

        // Verify that both files are indexed
        assertEquals(2, indexedFiles.size());
        assertTrue(indexedFiles.contains(textFile1));
        assertTrue(indexedFiles.contains(textFile2));
    }

    @Test
    void testRemoveFileFromIndex() {
        // Index the file first
        indexer.indexFile(textFile1);

        // Verify the file is indexed
        assertTrue(indexer.getIndexedFiles().contains(textFile1));

        // Remove the file from the index
        indexer.removeFileFromIndex(textFile1);

        // Verify that the file has been removed
        assertFalse(indexer.getIndexedFiles().contains(textFile1));
    }

    @Test
    void testUpdateFileInIndex() throws IOException {
        // Index the original file
        indexer.indexFile(textFile1);

        // Verify the original content is indexed
        assertTrue(indexer.getIndexedFiles().contains(textFile1));

        // Update the contents of the file
        Files.writeString(textFile1.toPath(), "Updated content for the test file.");

        // Update the file in the index
        assertTrue(indexer.updateFileInIndex(textFile1));

        // Verify that the updated file is still indexed
        assertTrue(indexer.getIndexedFiles().contains(textFile1));

        // Verify that the search for the new content works
        Set<File> result = indexer.search("updated");
        assertTrue(result.contains(textFile1));

        // Verify that the old content of the file is not indexed
        Set<File> resultOld = indexer.search("hello,");
        assertFalse(resultOld.contains(textFile1));
    }

    @Test
    void testUpdateFileInIndex_nonIndexedFile() throws IOException {
        // Create a new file that hasn't been indexed
        File nonIndexedFile = Files.createTempFile("nonIndexedFile", ".txt").toFile();
        Files.writeString(nonIndexedFile.toPath(), "This file has not been indexed.");
        nonIndexedFile.deleteOnExit();

        // Update the non-indexed file in the index
        assertTrue(indexer.updateFileInIndex(nonIndexedFile));

        // Verify that the file also gets indexed
        assertTrue(indexer.getIndexedFiles().contains(nonIndexedFile));
    }
}
