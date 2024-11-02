package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class FileHandling {

    public static String readTextFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " doesn't exist.");
        }

        if (!isTextFile(file)) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " is not a text file.");
        }

        return Files.readString(file.toPath());
    }

    public static String readTextFile(String filePath) throws IOException {
        File file = new File(filePath);

        return readTextFile(file);
    }


    public static boolean isTextFile(File file) {

        if (!file.exists() || !file.isFile()) {
            System.out.println("File doesn't exist");
            return false;
        }

        try {
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                System.out.println("Could not determine file type");
                return false;
            }
            return mimeType.startsWith("text");

        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }


    }


    /**
     * Returns a list of text files in the given directory and its subdirectories.
     * If the provided file argument is a text file, we return a list containing that file.
     * @param file The file or directory
     * @param recursive Whether to search recursively
     * @return A list of text files in the given directory and its subdirectories.
     * Returns {@code null} if the file or directory doesn't exist or is neither a text file nor a directory.
     */
    public static List<File> getTextFiles(File file, boolean recursive){
        if (!file.exists()) {
            System.out.printf("File %s doesn't exist", file.getAbsolutePath());
            return null;
        }

        if (isTextFile(file)) {
            return List.of(file);
        }

        if (!file.isDirectory()) {
            System.out.printf("%s is not a directory", file.getAbsolutePath());
            return null;
        }

        List<File> textFiles = new ArrayList<>();

        File[] filesInDirectory = file.listFiles();
        if (filesInDirectory == null) {
            return null;
        }

        for (File f : filesInDirectory) {
            if (isTextFile(f)) {
                textFiles.add(f);
            }
            if (recursive) {

                List<File> filesRecursively = getTextFiles(f, recursive);
                if (filesRecursively != null) {
                    textFiles.addAll(filesRecursively);
                }
            }
        }

        return textFiles;

    }

}
