package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling file operations, particularly for reading text files
 * and identifying text file types within a given directory structure.
 */
public class FileHandling {

    /**
     * Reads the content of a text file.
     *
     * @param file The text file to read.
     * @return The content of the file as a String.
     * @throws IOException If an I/O error occurs reading from the file.
     * @throws IllegalArgumentException If the file does not exist or is not a text file.
     */
    public static String readTextFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " doesn't exist.");
        }

        if (!isTextFile(file)) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " is not a text file.");
        }

        return Files.readString(file.toPath());
    }

    /**
     * Checks if a given file is a text file based on its MIME type.
     *
     * @param file The file to check.
     * @return {@code true} if the file is a text file; {@code false} otherwise.
     *         If the file does not exist or is not a file, this method will return false.
     */
    public static boolean isTextFile(File file) {
        if (!file.exists()) {
            System.out.printf("File %s doesn't exist", file.getAbsolutePath());
            return false;
        }

        if (!file.isFile()) {
            return false;
        }

        try {
            // We use the probeContentType method for a more universal and accurate way to check the file type
            // This allows us to handle any type of file that stores text,
            // regardless of the actual file extension
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                System.out.println("Could not determine file type");
                return false;
            }
            return mimeType.startsWith("text");

        } catch (IOException e) {
            System.out.println("Error while checking file type: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of text files from the specified file or directory.
     * If the provided file is a text file, it will be included in the list.
     *
     * @param file      The file or directory to search for text files.
     * @param recursive Whether to search recursively through subdirectories.
     * @return A list of text files found in the specified directory and its subdirectories,
     *         or {@code null} if the file or directory does not exist or is not a text file or directory.
     */
    public static List<File> getTextFiles(File file, boolean recursive) {
        if (!file.exists()) {
            System.out.printf("File %s doesn't exist", file.getAbsolutePath());
            return null;
        }

        if (isTextFile(file)) {
            return List.of(file);
        }

        if (!file.isDirectory()) {
            System.out.printf("%s is neither a text file nor a directory, skipping", file.getAbsolutePath());
            return null;
        }

        List<File> textFiles = new ArrayList<>();

        File[] filesInDirectory = file.listFiles();
        if (filesInDirectory == null) {
            return null;
        }

        for (File f : filesInDirectory) {
            if (isTextFile(f)) {
                textFiles.add(f);
                continue;
            }
            if (recursive) {
                List<File> filesRecursively = getTextFiles(f, true);
                if (filesRecursively != null) {
                    textFiles.addAll(filesRecursively);
                }
            }
        }

        return textFiles;
    }
}
