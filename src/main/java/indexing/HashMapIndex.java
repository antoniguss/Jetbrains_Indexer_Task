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
    }

    @Override
    public Set<File> search(String query) {
        return index.getOrDefault(query, new HashSet<>());
    }

    @Override
    public void clearIndex() {
        this.index.clear();
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
