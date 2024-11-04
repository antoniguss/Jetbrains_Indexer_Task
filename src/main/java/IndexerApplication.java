import indexing.FileIndexer;
import indexing.SimpleFileIndexer;
import util.FileHandling;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The IndexerApplication class manages the command-line interface (CLI)
 * for the text file indexing and searching application.
 * It allows users to execute various commands related to file indexing and searching.
 */
class IndexerApplication implements Runnable {
    private final FileIndexer fileIndexer; // The file indexer used for indexing files
    private final Map<String, Command> commands; // Map to store available commands
    private File currentDirectory; // The current working directory

    /**
     * Initializes the IndexerApplication with a SimpleFileIndexer
     * and sets the current directory to the user's working directory.
     */
    public IndexerApplication() {
        this.fileIndexer = new SimpleFileIndexer();
        this.currentDirectory = new File(System.getProperty("user.dir"));
        this.commands = new HashMap<>();
        this.initializeCommands(); // Set up available commands
    }

    /**
     * Initializes the command map with available commands and their handlers.
     */
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
                continue; // Skip empty input
            }
            // Split input into command and arguments
            String[] parts = input.split("\\s+");
            String command = parts[0];
            String[] args = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : new String[0];

            // Check for exit command
            if (command.equals("exit")) {
                if (commands.get("exit").execute(args)) {
                    break; // Exit the application
                }
                continue;
            }

            // Validate command
            if (!commands.containsKey(command)) {
                System.out.println("Invalid command. Please try again.");
                continue;
            }

            // Execute the command
            if (!commands.get(command).execute(args)) {
                System.out.println("Error while executing command. Please try again.");
            }
            System.out.println();
        }
        scanner.close();
        System.out.println("Thank you for using the File Indexer & Search Utility!");
    }

    /**
     * Displays a list of available commands to the user.
     *
     * @param ignored Unused parameter.
     * @return true to indicate that the command was executed successfully.
     */
    private boolean displayHelp(String[] ignored) {
        System.out.println("Available commands:");
        System.out.println("1. index [-r recursively] <path1> <path2> ... <pathN> - Index all text files in the specified directories and files.");
        System.out.println("2. query <word> - Find files containing the specified word in the stored index.");
        System.out.println("3. cd <path> - Change the current directory.");
        System.out.println("4. ls - List all files in the current directory.");
        System.out.println("5. exit - Exit the application.");
        return true;
    }

    /**
     * Handles the indexing of files based on user input.
     *
     * @param args Command line arguments specifying file paths to index.
     * @return true if the indexing was successful, false otherwise.
     */
    private boolean handleFileIndexer(String[] args) {
        String wrongInputMessage = "Please provide a list of paths. If a path contains spaces, it should be put in quotation marks. The paths should be separated by spaces.";

        // Check for at least one argument
        if (args.length == 0) {
            System.out.println(wrongInputMessage);
            return false;
        }

        boolean recursive = false; // Flag for recursive indexing
        String[] filePaths = args;

        // Check for the recursive flag
        if (args[0].equals("-r") || args[0].equals("--recursive")) {
            recursive = true;
            filePaths = Arrays.copyOfRange(args, 1, args.length); // Remove the flag
        }

        // Ensure at least one file path is provided
        if (filePaths.length == 0) {
            System.out.println(wrongInputMessage);
            return false;
        }

        List<File> textFiles = new ArrayList<>();

        for (String filePath : filePaths) {
            // Remove quotes from the file path
            filePath = filePath.replaceAll("\"", "");
            File providedFile = new File(filePath);

            // If the provided path is not absolute, we assume it's relative to the current directory
            if (!providedFile.isAbsolute()) {
                providedFile = new File(this.currentDirectory, providedFile.getPath());
            }

            // Get text files from the provided path
            List<File> files = FileHandling.getTextFiles(providedFile, recursive);
            if (files != null) {
                textFiles.addAll(files);
            }
        }

        // Index each text file
        for (File textFile : textFiles) {

            if (!this.fileIndexer.getIndexedFiles().contains(textFile)) {
                if (!this.fileIndexer.indexFile(textFile)) {
                    System.out.println("Error while indexing file: " + textFile.getAbsolutePath());
                    return false;
                }
                continue;
            }

            // Prompt user to update the index if the file is already indexed
            System.out.printf("File `%s` already indexed. Update? (y/n) ", textFile.getName());
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

    /**
     * Searches for files containing a specific keyword.
     *
     * @param args Command line arguments containing the search keyword.
     * @return {@code true} if the search was successful, {@code false} otherwise.
     */
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
            System.out.println("- " + file.getAbsolutePath());
        }
        return true;
    }

    /**
     * Changes the current working directory based on user input.
     * If no path is provided, it changes to the user's home directory.
     *
     * @param args Command line arguments containing the new directory path.
     * @return true if the directory change was successful, false otherwise.
     */
    private boolean handleChangeDirectory(String[] args) {
        if (args.length == 0) {
            // Change to the user's home directory if no argument is provided
            this.currentDirectory = new File(System.getProperty("user.home"));
            return true;
        }

        String directoryPath = args[0];
        File providedDirectory = new File(directoryPath);
        File newCurrentDirectory;

        // Determine the new current directory
        if (providedDirectory.isAbsolute()) {
            newCurrentDirectory = providedDirectory;
        } else {
            newCurrentDirectory = new File(this.currentDirectory, directoryPath);
        }

        // Make the path canonical
        try {
            newCurrentDirectory = newCurrentDirectory.getCanonicalFile();
        } catch (IOException e) {
            System.out.println("Error while making path canonical: " + e.getMessage());
            return false;
        }

        // Check if the directory exists
        if (!newCurrentDirectory.exists()) {
            System.out.println("Directory doesn't exist.");
            return false;
        }

        this.currentDirectory = new File(newCurrentDirectory.getAbsolutePath());
        System.out.println("Changed directory to: " + this.currentDirectory);

        return true;
    }

    /**
     * Lists all files in the current working directory.
     *
     * @return true if the listing was successful.
     */
    private boolean handleListFiles(String[] ignored) {
        File[] files = this.currentDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                // Print directory names with a slash
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
     * Prompts the user to confirm if they want to exit the application.
     *
     * @return true if the user confirms exit, false otherwise.
     */
    private boolean exitApplication(String[] ignored) {
        System.out.println("Are you sure you want to exit? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("y");
    }

    /**
     * Command interface for executing user commands.
     */
    interface Command {
        /**
         * Executes a command with the given arguments.
         *
         * @param args Command arguments.
         * @return true if the command executed successfully, false otherwise.
         */
        boolean execute(String[] args);
    }
}
