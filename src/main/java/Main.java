import indexing.FileIndexer;
import indexing.SimpleFileIndexer;
import util.FileHandling;

import java.io.IOException;
import java.util.*;

public class Main {

    // TODO: put functionality into an Application class
    // TODO: implement the file search
    // TODO: better logging
    // TODO: more robust error handling
    // TODO: implement "ls" command
    public static void main(String[] args) throws IOException {

        Application app = new Application(args);
        Thread thread = new Thread(app);
        thread.start();
    }


    // TODO: put functionality into an Application class
    // The app should handle interruptions like Ctrl+C
    // Example of application usage:
    //$ java TextFileIndexer
    //Welcome to the Text File Indexer!
    //Type 'help' for a list of commands.
    //
    //> help
    //Available commands:
    //1. index <path1> <path2> ... <pathN> - Index all text files in the specified directories and files.
    //2. query <word> - Find files containing the specified word in the stored index.
    //3. exit - Exit the application.
    //
    //> index /path/to/text/files
    //Indexing files in /path/to/text/files...
    //Indexed 3 files.
    //-  file1.txt
    //-  file2.txt
    //-  file3.txt
    //
    //> query the
    //Searching for 'the'...
    //Results:
    //-  file1.txt: contains 'the' (positions: 1, 8)
    //-  file2.txt: contains 'the' (positions: 1, 6)
    //-  file3.txt: contains 'the' (positions: 1)
    //
    //> query journey
    //Searching for 'journey'...
    //Results:
    //-  file3.txt: contains 'journey' (position: 1)
    //
    //> exit
    //Thank you for using the Text File Indexer. Goodbye!
}

class Application implements Runnable {
    private final FileIndexer fileIndexer;
    private final Map<String, Command> commands;
    private String currentDirectory;

    public Application(String[] args) {
        this.fileIndexer = new SimpleFileIndexer();

        this.currentDirectory = System.getProperty("user.dir");
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
            System.out.println("Working Directory: " + this.currentDirectory);
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
        // There should always be at least one argument. If the first argument is the recursive flag, there should be at least two arguments
        if (args.length == 0) {
            System.out.println("Please provide a list of paths. Each path should be put in quotation marks. The paths should be separated by spaces.");
            return false;
        }

        // The first argument should be the recursive flag (if present)
        boolean recursive = args[0].equals("-r") || args[0].equals("--recursive");

        // Remove the first argument from the array
        String[] filePaths = Arrays.copyOfRange(args, 1, args.length);

        // Check if at least one file path is provided
        if (filePaths.length == 0) {
            System.out.println("Please provide a list of paths. Each path should be put in quotation marks. The paths should be separated by spaces.");
            return false;
        }

        List<String> textFiles = new ArrayList<>();

        for (String filePath : filePaths) {
            if (recursive) {
                List<String> textFilesRecursively = FileHandling.getTextFilesRecursively(filePath);
                if (textFilesRecursively != null) {
                    textFiles.addAll(textFilesRecursively);
                }
            } else {
                List<String> textFilesInDirectory = FileHandling.getTextFiles(filePath);
                if (textFilesInDirectory != null) {
                    textFiles.addAll(textFilesInDirectory);
                }
            }

        }

        if (!fileIndexer.indexFiles(textFiles.toArray(new String[0]))) {
            System.out.println("Error while indexing files.");
            return false;
        }

        return true;
    }

    private boolean handleFileSearch(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide one keyword to search for.");
            return false;
        }
        String keyword = args[0];

        List<String> files = fileIndexer.search(keyword);

        if (files.isEmpty()) {
            System.out.println("No files found containing '" + keyword + "'");
            return true;
        }

        System.out.println("Files containing '" + keyword + "':");
        for (String file : files) {
            System.out.println("- " + file);
        }
        return true;
    }

    private boolean handleChangeDirectory(String[] args) {
        // TODO: implement changing directory
        System.out.println("Changing directory...");
        return true;
    }

    private boolean handleListFiles(String[] args) {
        // TODO: implement listing files
        System.out.println("Listing files...");
        return true;
    }

    /**
     * Asks the user if they want to exit the application.
     * @return true if the user wants to exit the application, false otherwise
     */
    private boolean exitApplication(String[] ignored) {

        System.out.println("Are you sure you want to exit? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("y");
    }

    @FunctionalInterface
    interface Command {
        // Indicating if command was executed successfully
        boolean execute(String[] args);

    }
}
