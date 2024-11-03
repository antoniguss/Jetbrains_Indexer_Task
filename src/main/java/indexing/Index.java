package indexing;

import java.io.File;
import java.util.Set;

/**
 * The Index interface defines methods for indexing, searching, and clearing indexes of files.
 */
public interface Index {
    void addToIndex(String token, File file);
    void removeFileFromIndex(File file);
    void clearIndex();

    Set<File> search(String query);
    Set<File> getIndexedFiles();
}
