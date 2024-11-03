package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlingTest {

    private File tempDir;
    private File tempDirNested;
    private File nestedTextFile1;
    private File nestedTextFile2;
    private File textFile;
    private File nonTextFile;
    private File emptyDir;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary directory for testing
        tempDir = Files.createTempDirectory("testDir").toFile();
        tempDir.deleteOnExit();


        // Create a nested directory with multiple files inside
        tempDirNested = new File(tempDir, "nestedDir");
        tempDirNested.mkdir();
        tempDirNested.deleteOnExit();

        // Create 2 text files in the nested directory
        nestedTextFile1 = new File(tempDirNested, "nestedTest1.txt");
        Files.writeString(nestedTextFile1.toPath(), "Nested text file 1.");
        nestedTextFile1.deleteOnExit();
        nestedTextFile2 = new File(tempDirNested, "nestedTest2.txt");
        Files.writeString(nestedTextFile2.toPath(), "Nested text file 2.");
        nestedTextFile2.deleteOnExit();

        // Create a temporary text file
        textFile = new File(tempDir, "test.txt");
        Files.writeString(textFile.toPath(), "This is a test text file.");
        textFile.deleteOnExit();

        // Create a temporary PNG image file (1x1 pixel)
        nonTextFile = new File(tempDir, "test.png");
        createSmallImage(nonTextFile);
        nonTextFile.deleteOnExit();

        // Create an empty directory
        emptyDir = new File(tempDir, "emptyDir");
        emptyDir.mkdir();
        emptyDir.deleteOnExit();
    }

    private void createSmallImage(File file) throws IOException {
        // Create a 1x1 pixel image
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.RED); // Fill the image with red color
        g.fillRect(0, 0, 1, 1);
        g.dispose();

        // Write the image to the file
        ImageIO.write(img, "png", file);
    }

    @Test
    void testReadTextFile_validFile() throws IOException {
        String content = FileHandling.readTextFile(textFile);
        assertEquals("This is a test text file.", content);
    }

    @Test
    void testReadTextFile_fileDoesNotExist() {
        File nonExistentFile = new File("nonExistentFile.txt");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> FileHandling.readTextFile(nonExistentFile));
        assertEquals(nonExistentFile.getAbsolutePath() + " doesn't exist.", exception.getMessage());
    }

    @Test
    void testReadTextFile_nonTextFile() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> FileHandling.readTextFile(nonTextFile));
        assertEquals(nonTextFile.getAbsolutePath() + " is not a text file.", exception.getMessage());
    }

    @Test
    void testIsTextFile_validTextFile() {
        assertTrue(FileHandling.isTextFile(textFile));
    }

    @Test
    void testIsTextFile_nonTextFile() {
        assertFalse(FileHandling.isTextFile(nonTextFile));
    }

    @Test
    void testIsTextFile_fileDoesNotExist() {
        File nonExistentFile = new File("nonExistentFile.txt");
        assertFalse(FileHandling.isTextFile(nonExistentFile));
    }

    @Test
    void testGetTextFiles_singleTextFile() {
        List<File> textFiles = FileHandling.getTextFiles(textFile, false);
        assertNotNull(textFiles);
        assertEquals(1, textFiles.size());
        assertTrue(textFiles.contains(textFile));
    }

    @Test
    void testGetTextFiles_nonRecursive() {
        List<File> textFiles = FileHandling.getTextFiles(tempDir, false);
        assertNotNull(textFiles);
        assertEquals(1, textFiles.size());
        assertTrue(textFiles.contains(textFile));
    }

    @Test
    void testGetTextFiles_directoryContainsNonTextFile() {
        List<File> textFiles = FileHandling.getTextFiles(tempDir, false);
        assertNotNull(textFiles);
        assertEquals(1, textFiles.size());
        assertTrue(textFiles.contains(textFile));
        assertFalse(textFiles.contains(nonTextFile));
    }

    @Test
    void testGetTextFiles_emptyDirectory() {
        List<File> textFiles = FileHandling.getTextFiles(emptyDir, false);
        assertNotNull(textFiles);
        assertEquals(0, textFiles.size());
    }

    @Test
    void testGetTextFiles_nonExistentDirectory() {
        File nonExistentDir = new File("nonExistentDir");
        List<File> textFiles = FileHandling.getTextFiles(nonExistentDir, true);
        assertNull(textFiles);
    }

    @Test
    void testGetTextFiles_recursiveSearch() {
        // Create a nested directory structure
        List<File> textFiles = FileHandling.getTextFiles(tempDir, true);
        assertNotNull(textFiles);
        assertEquals(3, textFiles.size());
        assertTrue(textFiles.contains(textFile));
        assertTrue(textFiles.contains(nestedTextFile1));
        assertTrue(textFiles.contains(nestedTextFile2));
    }
}
