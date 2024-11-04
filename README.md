# Text File Indexer Application

This repository contains a Java application designed for indexing text files
and querying them based on specific keywords.
It was developed as a test task for my application to a Jetbrains Internship.

## Overview

The Text File Indexer provides a simple and convenient way to index and search through text files.
The application allows users to specify files and directories for indexing
and enables keyword searches across the indexed content.

In designing the application, my primary goal was to create a robust framework that supports easy extensibility.
While I opted
not to implement complex tokenization algorithms or advanced data structures for indexing within this project,
the architecture is designed to make such additions straightforward. 

This means that adding more sophisticated tokenizers or alternative data structures can be done seamlessly,
thanks to the existing framework.

The provided code includes an example of a simple whitespace tokenizer (`WhiteSpaceTokenizer`),
which simply splits text into tokens based on whitespaces (spaces, tabs, newlines, etc.).

It also includes an index based on a hash map (`HashMapIndex`),
which stores the tokens and the files where they appear in a hash map.

## Functionality
The application provides the following functionality:
1. `help` - Displays a list of available commands.
2. `index` - Indexes a list of files or directories. The `-r recursive` flag can be used to quickly index all files text in a directory and its subdirectories.
3. `query` - Searches for files containing a specific keyword.
4. `cd` - Changes the current working directory.
5. `ls` - Lists the files in the current working directory.

The `cd` and `ls` commands make it easier to navigate through the file system and pick the files to index.
They mirror the functionality of the `cd` and `ls` commands in the shell.

## Usage

To run the application, execute the `Main` class:

Here's an example of how the application can be used:
```console
Welcome to the Text File Indexer!
Type 'help' for a list of commands.
Working Directory: /Users/antoniguss/Documents/indexer
> ls
target/
pom.xml
README.md
.gitignore
indexer.iml
.git/
.idea/
src/

Working Directory: /Users/antoniguss/Documents/indexer
> cd src
Changed directory to: /Users/antoniguss/Documents/indexer/src

Working Directory: /Users/antoniguss/Documents/indexer/src
> ls
test/
main/

Working Directory: /Users/antoniguss/Documents/indexer/src
> cd main
Changed directory to: /Users/antoniguss/Documents/indexer/src/main

Working Directory: /Users/antoniguss/Documents/indexer/src/main
> ls
resources/
java/

Working Directory: /Users/antoniguss/Documents/indexer/src/main
> cd resources
Changed directory to: /Users/antoniguss/Documents/indexer/src/main/resources

Working Directory: /Users/antoniguss/Documents/indexer/src/main/resources
> ls
exampleFiles/
wikipedia_extracts/

Working Directory: /Users/antoniguss/Documents/indexer/src/main/resources
> index wikipedia_extracts
Indexing file: /Users/antoniguss/Documents/indexer/src/main/resources/wikipedia_extracts/new_york_city.txt
Indexing file: /Users/antoniguss/Documents/indexer/src/main/resources/wikipedia_extracts/Poland.txt
Indexing file: /Users/antoniguss/Documents/indexer/src/main/resources/wikipedia_extracts/United_States.txt

Working Directory: /Users/antoniguss/Documents/indexer/src/main/resources
> query potatoes
No files found containing 'potatoes'

Working Directory: /Users/antoniguss/Documents/indexer/src/main/resources
> query voivodeship
Files containing 'voivodeship':
-  /Users/antoniguss/Documents/indexer/src/main/resources/wikipedia_extracts/Poland.txt

Working Directory: /Users/antoniguss/Documents/indexer/src/main/resources
> query USA
Files containing 'usa':
-  /Users/antoniguss/Documents/indexer/src/main/resources/wikipedia_extracts/United_States.txt

Working Directory: /Users/antoniguss/Documents/indexer/src/main/resources
> exit
Are you sure you want to exit? (y/n)
y
Thank you for using the File Indexer & Search Utility!
```

## Testing
Unit tests are provided to ensure the functionality of file handling and indexing components.
Tests can be run using JUnit.
**This is the only external dependency in the project**.


### The following resources were used as inspiration, research or useful code snippets:
- [Full-text search](https://en.wikipedia.org/wiki/Full-text_search)
- [Wikipedia—Lexical Analysis](https://en.wikipedia.org/wiki/Lexical_analysis)
- [Check given file is Simple Text File using Java - Stack Overflow](https://stackoverflow.com/questions/17192770/check-given-file-is-simple-text-file-using-java)
- [fawitte/interactive-cli: An interactive cli for Java that can be used for example for prototyping applications with multiple threads that need to be controlled during runtime (e.g. server or client).](https://github.com/fawitte/interactive-cli)
- [Wikipedia](https://www.wikipedia.org/) For long text files written in English.


