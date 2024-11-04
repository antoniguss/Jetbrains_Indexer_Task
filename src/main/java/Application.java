import indexing.FileIndexer;
import indexing.SimpleFileIndexer;
import util.FileHandling;

import java.io.File;
import java.io.IOException;
import java.util.*;

class Application implements Runnable {
    private final FileIndexer fileIndexer;
    private final Map<String, Command> commands;
    private File currentDirectory;

    public Application() {
        this.fileIndexer = new SimpleFileIndexer();
        this.currentDirectory = new File(System.getProperty("user.dir"));
        this.commands = new HashMap<>();
        this.initializeCommands();
    }


    private void initializeCommands() {
        commands.put("help", this::displayHelp);
        commands.put("index", this::handleFileIndexer);
        commands.put("query", this::handleFileSearch);
        commands.put("cd", this::handleChangeDirectory);
        commands.put("ls", this::handleListFiles);
        commands.put("exit", this::exitApplication);
    }

    @Override
    public void run() {
        System.out.println("Welcome to the Text File Indexer!");
        System.out.println("Type 'help' for a list of commands.");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Working Directory: " + this.currentDirectory.getAbsolutePath());
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                continue;
            }
            // Extract command from the command string. The first word is the command, the rest (if present) are the arguments
            String[] parts = input.split("\\s+");
            String command = parts[0];
            String[] args = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : new String[0];

            // Check for exit command
            if (command.equals("exit")) {
                if (commands.get("exit").execute(args)) {
                    break;
                }

                continue;
            }

            // Check if the command is valid
            if (!commands.containsKey(command)) {
                System.out.println("Invalid command. Please try again.");
                continue;
            }

            // Execute command
            if (!commands.get(command).execute(args)) {
                System.out.println("Error while executing command. Please try again.");
            }
            System.out.println();
        }
        scanner.close();
        System.out.println("Thank you for using the File Indexer & Search Utility!");
    }

    private boolean displayHelp(String[] ignored) {
        System.out.println("Available commands:");
        System.out.println("1. index [-r recursively] <path1> <path2> ... <pathN> - Index all text files in the specified directories and files.");
        System.out.println("2. query <word> - Find files containing the specified word in the stored index.");
        System.out.println("3. cd <path> - Change the current directory.");
        System.out.println("4. ls - List all files in the current directory.");
        System.out.println("5. exit - Exit the application.");
        return true;
    }


    private boolean handleFileIndexer(String[] args) {
        String wrongInputMessage = "Please provide a list of paths. If a path contains spaces, it should be put in quotation marks. The paths should be separated by spaces.";
        // There should always be at least one argument. If the first argument is the recursive flag, there should be at least two arguments
        if (args.length == 0) {
            System.out.println(wrongInputMessage);
            return false;
        }

        boolean recursive = false;
        String[] filePaths = args;
        if (args[0].equals("-r") || args[0].equals("--recursive")) {
            recursive = true;

            // Remove the first argument
            filePaths = Arrays.copyOfRange(args, 1, args.length);
        }

        // Check if at least one file path is provided
        if (filePaths.length == 0) {
            System.out.println(wrongInputMessage);
            return false;
        }

        List<File> textFiles = new ArrayList<>();

        for (String filePath : filePaths) {
            // paths cannot contain quotation marks, so we remove them if one is present
            filePath = filePath.replaceAll("\"", "");

            File providedFile = new File(filePath);

            if (!providedFile.isAbsolute()) {
                providedFile = new File(this.currentDirectory, providedFile.getPath());
            }

            List<File> files;
            if ((files = FileHandling.getTextFiles(providedFile, recursive)) != null) {
                textFiles.addAll(files);
            }

        }

        for (File textFile : textFiles) {
            if (!this.fileIndexer.getIndexedFiles().contains(textFile)) {
                if (!this.fileIndexer.indexFile(textFile)) {
                    System.out.println("Error while indexing file: " + textFile.getAbsolutePath());
                    return false;
                }
                continue;
            }

            // Ask if the user wants to update the index for the file
            System.out.println("File already indexed. Update? (y/n)");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("y")) {
                if (!this.fileIndexer.updateFileInIndex(textFile)) {
                    System.out.println("Error while updating index for file: " + textFile.getAbsolutePath());
                    return false;
                }
            }
        }


        return true;
    }


    private boolean handleFileSearch(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide one keyword to search for.");
            return false;
        }
        String keyword = args[0].trim().toLowerCase();

        Set<File> files = fileIndexer.search(keyword);

        if (files.isEmpty()) {
            System.out.println("No files found containing '" + keyword + "'");
            return true;
        }

        System.out.println("Files containing '" + keyword + "':");
        for (File file : files) {
            System.out.println("- " + file);
        }
        return true;
    }

    private boolean handleChangeDirectory(String[] args) {

        if (args.length == 0) {
            // If no argument is provided, we change to the user's home directory
            this.currentDirectory = new File(System.getProperty("user.home"));
            return true;
        }

        String directoryPath = args[0];


        File providedDirectory = new File(directoryPath);
        System.out.println("providedDirectory = " + providedDirectory);

        File newCurrentDirectory;
        if (providedDirectory.isAbsolute()) {
            newCurrentDirectory = providedDirectory;
        } else {
            newCurrentDirectory = new File(this.currentDirectory, directoryPath);
        }


        // Make path canonical
        try {
            newCurrentDirectory = newCurrentDirectory.getCanonicalFile();
        } catch (IOException e) {
            System.out.println("Error while making path canonical: " + e.getMessage());
            return false;
        }

        if (!newCurrentDirectory.exists()) {
            System.out.println("Directory doesn't exist.");
            return false;
        }

        this.currentDirectory = new File(newCurrentDirectory.getAbsolutePath());
        System.out.println("Changed directory to: " + this.currentDirectory);


        return true;
    }

    private boolean handleListFiles(String[] args) {
        File[] files = this.currentDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                // If a file is a directory, we print its name followed by a slash
                if (file.isDirectory()) {
                    System.out.println(file.getName() + "/");
                } else {
                    System.out.println(file.getName());
                }
            }
        }
        return true;
    }

    /**
     * Asks the user if they want to exit the application.
     *
     * @return true if the user wants to exit the application, false otherwise
     */
    private boolean exitApplication(String[] ignored) {

        System.out.println("Are you sure you want to exit? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("y");
    }

    interface Command {
        // Indicating if command was executed successfully
        boolean execute(String[] args);

    }
}
