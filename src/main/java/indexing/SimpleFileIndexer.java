package indexing;

import tokenizing.WhitespaceTokenizer;
import util.FileHandling;

import java.io.IOException;
import java.util.List;

public class SimpleFileIndexer extends FileIndexer {


    public SimpleFileIndexer() {
        super(new WhitespaceTokenizer(), new HashMapIndex());
    }

    @Override
    public boolean indexFile(String filePath) {

        String fileContents = "";
        try {
            fileContents = FileHandling.readTextFile(filePath);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            return false;
        }
        List<String> tokenized = this.tokenizer.tokenize(fileContents);

        for (String token : tokenized) {
            this.index.addToIndex(token, filePath);
        }


        return true;
    }

    @Override
    public boolean indexFiles(String... filePath) {
        for (String file : filePath) {
            if (!indexFile(file)) {
                this.clearIndex();
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> search(String keyword) {
        return this.index.search(keyword);
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
