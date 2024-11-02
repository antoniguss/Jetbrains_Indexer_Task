package indexing;
import tokenizing.Tokenizer;

import java.io.File;
import java.util.Set;


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
     * @param file the text file to be indexed.
     * @return a boolean value indicating whether the indexing was successful or not.
     */
    public abstract boolean indexFile(File file);

    /**
     * Indexes multiple files specified by their file paths.
     *
     * @param file variable number of files to be indexed
     * @return a boolean value indicating whether the indexing operation was successful or not
     */
    public abstract boolean indexFiles(File... file);


    /**
     * Searches the index for files containing a particular phrase or keyword.
     *
     * @param keyword the keyword to search for in the index.
     * @return a Set of file paths that contain the requested keyword
     */
    public abstract Set<File> search(String keyword);

    /**
     * Clears the index, useful when files are updated or deleted.
     */
    public abstract void clearIndex();

    /**
     * @return Returns a set of all files stored in the index.
     */
    public Set<File> getIndexedFiles() {
        return this.index.getIndexedFiles();
    }

}