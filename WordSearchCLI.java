package zinost;


import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class WordSearchCLI {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Invalid number of arguments");
            System.exit(1);
        }

        String searchTechnique = args[0];
        String gridFilename = args[1];
        String wordlistFilename = args[2];
        String dataStructure = args[3];

        GameBoard board;

        if (dataStructure.equals("ArrayList")) {
            board = new GameBoard(new ArrayList<>());
        } else if (dataStructure.equals("LinkedList")) {
            board = new GameBoard(new LinkedList<>());
        } else if (dataStructure.equals("HashSet")) {
            board = new GameBoard(new HashSet<>());
        } else if (dataStructure.equals("TreeSet")) {
            board = new GameBoard(new TreeSet<>());
        } else {
            System.out.println("Invalid data structure: " + dataStructure);
            System.exit(1);
            return;
        }

        // Load grid and dictionary
        board.loadGrid(Path.of(gridFilename));
        board.loadDictionary(Path.of(wordlistFilename));

        long startTime = System.currentTimeMillis();

        // Find words based on the specified search technique
        List<String> foundWords;
        if (searchTechnique.equals("4way")) {
            foundWords = board.findWords(true);
        } else if (searchTechnique.equals("8way")) {
            foundWords = board.findWords(false);
        } else {
            System.out.println("Invalid search technique: " + searchTechnique);
            System.exit(1);
            return;
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        // Print the found words
        System.out.println("Words found:");
        for (String word : foundWords) {
            System.out.println(word);
        }

        // Print the total number of words found
        System.out.println("Total words found: " + foundWords.size());

        // Print the total time required to complete the search
        System.out.println("Total time: " + totalTime + "ms");
    }
}


