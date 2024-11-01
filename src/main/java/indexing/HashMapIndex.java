package indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapIndex implements Index {
    final HashMap<String, List<String>> index;

    public HashMapIndex() {
        index = new HashMap<>();
    }

    @Override
    public void addToIndex(String token, String filePath) {
        if (!index.containsKey(token)) {
            List<String> filePaths = new ArrayList<>();
            filePaths.add(filePath);

            index.put(token, filePaths);
        } else {
            index.get(token).add(filePath);
        }
    }

    @Override
    public List<String> search(String query) {
        return index.getOrDefault(query, new ArrayList<>());
    }

    @Override
    public void clearIndex() {
        this.index.clear();
    }

    @Override
    public String toString() {
        return "HashMapIndex{" +
               "index=" + index +
               '}';
    }
}
