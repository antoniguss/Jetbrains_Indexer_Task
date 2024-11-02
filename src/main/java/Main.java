import java.io.IOException;

public class Main {

    // TODO: implement the file search
    // TODO: better logging
    // TODO: more robust error handling
    // TODO: implement "ls" command
    public static void main(String[] args) {

        Application app = new Application();
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

