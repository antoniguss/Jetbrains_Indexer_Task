package indexing;

import java.util.List;

public interface Index {
    void addToIndex(String token, String filePath);
    List<String> search(String query);

    void clearIndex();
}
