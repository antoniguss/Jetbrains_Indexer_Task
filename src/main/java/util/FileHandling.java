package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileHandling {

    public static String readTextFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (!isTextFile(file)) {
            throw new IllegalArgumentException(filePath + " is not a text file.");
        }

        return Files.readString(Path.of(filePath));

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
     * Returns a list of all text files in the specified directory
     *
     * @param directoryPath The path to the directory
     * @return A list of paths to the text files. Null if the file at path doesn't exist or is not a directory
     */
    public static List<String> getTextFiles(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            System.out.println("Directory doesn't exist: " + directoryPath);
            return null;
        } else if (!directory.isDirectory()) {
            System.out.println("Directory is not a directory: " + directoryPath);
            return null;
        }
        File[] files = directory.listFiles();
        List<String> textFiles = new java.util.ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (isTextFile(file)) {
                    textFiles.add(file.getPath());
                }
            }
        }

        return textFiles;

    }


    public static List<String> getTextFilesRecursively(String directoryPath){
        File directory = new File(directoryPath);
        // If the directory doesn't exist or is not a directory, return null
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Directory doesn't exist");
            return null;
        }
        List<String> textFiles = new ArrayList<>();

        // Get all files in the directory
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively get all text files in the subdirectory
                    List<String> textFilesRecursively = getTextFilesRecursively(file.getPath());
                    if (textFilesRecursively != null) {
                        textFiles.addAll(textFilesRecursively);
                    }
                } else if (isTextFile(file)) {
                    textFiles.add(file.getPath());
                }
            }
        }

        return textFiles;

    }
}
