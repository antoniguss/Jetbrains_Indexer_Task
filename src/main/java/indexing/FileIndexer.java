package indexing;
import tokenizing.Tokenizer;

import java.io.IOException;
import java.util.List;


/**
 * The FileIndexer interface defines methods for indexing, searching, and clearing indexes of files.
 * An implementation of this indexer must define its own structure for processing, storing and searching an index of the provided files.
 */
public abstract class FileIndexer {
    final Tokenizer tokenizer;
    final Index index;

    public FileIndexer(Tokenizer tokenizer, Index index) {
        this.tokenizer = tokenizer;
        this.index = index;
    }

    /**
     * Indexes a given file.
     *
     * @param filePath the path of the text file to be indexed.
     * @return a boolean value indicating whether the indexing was successful or not.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    public abstract boolean indexFile(String filePath);

    /**
     * Indexes multiple files specified by their file paths.
     *
     * @param filePath variable number of file paths to be indexed
     * @return a boolean value indicating whether the indexing operation was successful or not
     */
    public abstract boolean indexFiles(String... filePath);


    /**
     * Searches the index for files containing a particular phrase or keyword.
     *
     * @param keyword the keyword to search for in the index.
     * @return a List of file paths that contain the requested keyword
     */
    public abstract List<String> search(String keyword);

    /**
     * Clears the index, useful when files are updated or deleted.
     */
    public abstract void clearIndex();
}