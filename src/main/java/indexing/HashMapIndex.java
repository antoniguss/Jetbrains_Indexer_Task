package indexing;

import java.io.File;
import java.util.*;

public class HashMapIndex implements Index {
    final Set<File> indexedFiles;
    final HashMap<String, Set<File>> index;

    public HashMapIndex() {
        this.indexedFiles = new HashSet<>();
        this.index = new HashMap<>();
    }

    @Override
    public void addToIndex(String token, File file) {
        if (!index.containsKey(token)) {
            Set<File> files = new HashSet<>();
            files.add(file);

            index.put(token, files);
        } else {
            index.get(token).add(file);
        }
        this.indexedFiles.add(file);
    }

    /**
     * Removes a file from the index.
     * Note:
     * This requires iterating over the entire key set
     * and might be inefficient for indexes with a large number of tokens.
     * @param file The file to be removed from the index.
     */
    @Override
    public void removeFileFromIndex(File file) {
        if (!this.indexedFiles.contains(file)) {
            return;
        }
        this.indexedFiles.remove(file);

        List<String> tokens = new ArrayList<>(this.index.keySet());
        for (String token: tokens) {
            this.index.get(token).remove(file);
            if (this.index.get(token).isEmpty()) {
                this.index.remove(token);
            }
        }
    }


    /**
     * Searches the index for files containing a particular phrase or keyword.
     * If a file removed from the index is found during the search, it will be removed from the hashmap index and from the returned set.
     * @param query The token to search for in the index.
     * @return A set of file paths that contain the requested token. An empty set if no files are found.
     */
    @Override
    public Set<File> search(String query) {
        Set<File> filesFound = index.get(query);
        if (filesFound == null) {
            return Collections.emptySet();
        }

        return filesFound;
    }

    @Override
    public void clearIndex() {
        this.index.clear();
        this.indexedFiles.clear();
    }

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
