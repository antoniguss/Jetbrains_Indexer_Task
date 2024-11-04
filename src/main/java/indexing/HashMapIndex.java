package indexing;

import java.io.File;
import java.util.*;

/**
 * The HashMapIndex class implements the Index interface using a HashMap
 * to store and manage indexed files.
 * It allows for adding, removing, searching, and clearing indexed files efficiently.
 *
 * <p> </p>A HashMap was chosen as a way to store the index as it should provide a constant-time lookup and insertion.
 * The efficiency is limited when considering the removal of files from the index, as it requires iterating over all tokens.
 */
public class HashMapIndex implements Index {
    final Set<File> indexedFiles;
    final HashMap<String, Set<File>> index; // Map of tokens to sets of files

    /**
     * Initializes a new HashMapIndex with empty sets for indexed files
     * and an empty index.
     */
    public HashMapIndex() {
        this.indexedFiles = new HashSet<>();
        this.index = new HashMap<>();
    }

    /**
     * Adds a file to the index under a specified token.
     * If the token does not exist, a new entry is created.
     *
     * @param token The token associated with the file.
     * @param file The file to be indexed.
     */
    @Override
    public void addToIndex(String token, File file) {
        if (!index.containsKey(token)) {
            Set<File> files = new HashSet<>();
            files.add(file);
            index.put(token, files); // Create a new entry for the token
        } else {
            index.get(token).add(file); // Add the file to the existing token
        }
        this.indexedFiles.add(file); // Track the indexed file
    }

    /**
     * Removes a file from the index.
     * This method iterates over all tokens and removes the file from each
     * associated set. If a token's set becomes empty, the token is removed
     * from the index.
     *
     * @param file The file to be removed from the index.
     */
    @Override
    public void removeFileFromIndex(File file) {
        if (!this.indexedFiles.contains(file)) {
            return;
        }
        this.indexedFiles.remove(file);

        List<String> tokens = new ArrayList<>(this.index.keySet());
        for (String token : tokens) {
            this.index.get(token).remove(file); // Remove file from each token's set

            if (this.index.get(token).isEmpty()) {
                // Remove an entry from the index if the set is empty
                this.index.remove(token);
            }
        }
    }

    /**
     * Searches the index for files associated with a specific token.
     *
     * @param query The token to search for in the index.
     * @return A set of files that contain the requested token, or an empty set if none are found.
     */
    @Override
    public Set<File> search(String query) {
        return index.getOrDefault(query, new HashSet<>()); // Return the set of files found
    }

    /**
     * Clears all entries from the index and the set of indexed files.
     */
    @Override
    public void clearIndex() {
        this.index.clear(); // Clear the index
        this.indexedFiles.clear(); // Clear the indexed files
    }

    /**
     * Returns a set of all files currently indexed.
     *
     * @return A set of indexed files.
     */
    @Override
    public Set<File> getIndexedFiles() {
        return this.indexedFiles;
    }

    @Override
    public String toString() {
        return "HashMapIndex{" +
               "index=" + index +
               '}';
    }
}
