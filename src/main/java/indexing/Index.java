package indexing;

import java.io.File;
import java.util.Set;

/**
 * The Index interface defines methods for indexing, searching, and clearing indexes of files.
 * This interface allows for implementing indexes based on different underlying data structures
 * (such as HashMaps, Tries, etc.).
 */
public interface Index {

    /**
     * Adds a file to the index containing a specified token.
     * @param token The string token associated with the file.
     * @param file The file to be indexed.
     */
    void addToIndex(String token, File file);

    /**
     * Removes a file from the index.
     * After a file is removed,
     * it should never appear when using the {@link #search(String)} method or the {@link #getIndexedFiles()} method.
     * @param file The file to be removed from the index.
     */
    void removeFileFromIndex(File file);

    /**
     * Removes all entries from the index and the set of indexed files.
     */
    void clearIndex();

    /**
     * Searches the index for files associated with a specific token.
     * @param query The string token to search for in the index.
     * @return A set of files that contain the requested token, or an empty set if none are found.
     */
    Set<File> search(String query);

    /**
     * Returns a set of all files currently indexed.
     * @return A set of all files stored in the index.
     */
    Set<File> getIndexedFiles();
}
