package zinost;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GameBoard {
    private int numRows;
    private int numCols;
    private Cell[][] grid;
    private List<String> dictionary;
    private static final int[][] FOUR_DIRECTIONS = {
            {-1, 0}, // North
            {1, 0},  // South
            {0, -1}, // West
            {0, 1}   // East
    };

    private static final int[][] EIGHT_DIRECTIONS = {
            {-1, 0},  // North
            {1, 0},   // South
            {0, -1},  // West
            {0, 1},   // East
            {-1, -1}, // Northwest
            {-1, 1},  // Northeast
            {1, -1},  // Southwest
            {1, 1}    // Southeast
    };

    private class Cell {
        int row;
        int col;
        char letter;

        public Cell(int row, int col, char letter) {
            this.row = row;
            this.col = col;
            this.letter = letter;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col, letter);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Cell)) {
                return false;
            }
            Cell c = (Cell) o;
            return c.row == row && c.col == col && c.letter == letter;
        }

        public char getLetter() {
            return letter;
        }
    }

    public GameBoard(Collection<String> emptyDictionary) {
        dictionary = new ArrayList<>(emptyDictionary);
        // Initialize any other attributes needed for the GameBoard

        // Set initial values for numRows and numColumns
        numRows = 0;
        numCols = 0;
    }

    public void loadDictionary(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            dictionary = new ArrayList<>(lines);
        } catch (IOException e) {
            // Handle the exception appropriately (e.g., print an error message)
            e.printStackTrace();
        }
    }

    public void loadGrid(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            numRows = lines.size();
            numCols = lines.get(0).length();

            grid = new Cell[numRows][numCols];

            for (int row = 0; row < numRows; row++) {
                String line = lines.get(row);
                for (int col = 0; col < numCols; col++) {
                    char letter = line.charAt(col);
                    grid[row][col] = new Cell(row, col, letter);
                }
            }
        } catch (IOException e) {
            // Handle the exception appropriately (e.g., print an error message)
            e.printStackTrace();
        }
    }

    private void recursiveSearch(int row, int col, String word, HashSet<Cell> visited, boolean isFourWay, List<String> foundWords) {
        // Base case: If the word exceeds the length limit, return
        if (word.length() > 15) {
            return;
        }

        // Add the current cell to the visited set
        visited.add(grid[row][col]);

        // Check if the word is a valid word in the dictionary
        if (dictionary.contains(word.toLowerCase())) {
            if (word.length() >= 3 && !foundWords.contains(word)) {
                foundWords.add(word);
            }
        }



            // Check all eight possible neighbors (four-way or eight-way search)
            int[][] directions = isFourWay ? FOUR_DIRECTIONS : EIGHT_DIRECTIONS;
            for (int[] direction : directions) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                // Skip if the neighbor is out of bounds or already visited
                if (newRow < 0 || newRow >= numRows || newCol < 0 || newCol >= numCols || visited.contains(grid[newRow][newCol])) {
                    continue;
                }

                // Recursively search from the neighbor cell, passing the updated word
                recursiveSearch(newRow, newCol, word + grid[newRow][newCol].getLetter(), new HashSet<>(visited), isFourWay, foundWords);
            }


        // Remove the current cell from the visited set to backtrack
        visited.remove(grid[row][col]);
    }

    public List<String> findWords(boolean isFourWay) {
        List<String> foundWords = new ArrayList<>();

        // Iterate over each cell in the grid
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                HashSet<Cell> visited = new HashSet<>();
                recursiveSearch(row, col, "" + grid[row][col].getLetter(), visited, isFourWay, foundWords);
            }
        }

        return foundWords;
    }
}