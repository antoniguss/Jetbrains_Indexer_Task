package indexing;

import tokenizing.WhitespaceTokenizer;
import util.FileHandling;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class SimpleFileIndexer extends FileIndexer {


    public SimpleFileIndexer() {
        super(new WhitespaceTokenizer(), new HashMapIndex());
    }

    @Override
    public boolean indexFile(File file) {
        System.out.println("Indexing file: " + file.getAbsolutePath());

        String fileContents;
        try {
            fileContents = FileHandling.readTextFile(file);
        } catch (IOException e) {
//            System.out.println("Error reading file: " + file.getAbsolutePath());
            return false;
        } catch (IllegalArgumentException e) {
//            System.out.printf("File is not a text file: %s, skipping%n", file.getAbsolutePath());
            return false;
        }
        List<String> tokenized = this.tokenizer.tokenize(fileContents);

        for (String token : tokenized) {
            this.index.addToIndex(token.toLowerCase(), file);
        }

        return true;
    }

    @Override
    public boolean indexFiles(File... files) {
        for (File file : files) {
            if (!indexFile(file)) {
                this.clearIndex();
                return false;
            }
        }
        return true;
    }


    @Override
    public Set<File> search(String keyword) {
        return this.index.search(keyword.toLowerCase());
    }

    @Override
    public void clearIndex() {
        this.index.clearIndex();
    }

    @Override
    public String toString() {
        return "SimpleFileIndexer{" +
               "tokenizer=" + tokenizer +
               ", index=" + index +
               '}';
    }
}
