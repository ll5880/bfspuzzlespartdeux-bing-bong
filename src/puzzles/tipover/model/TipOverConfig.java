package puzzles.tipover.model;

import solver.*;
import util.Coordinates;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
/**
 * Creates a configuration that holds the board info and the coordinates of the tipper and goal
 * @author Darian Cheung, Team bingbong
 * November 2021
 */

public class TipOverConfig implements Configuration {

    // Dimensions of the grid
    public String[][] grid;
    public static int width;
    public static int length;

    // height of tower, if it's tipped or not
    private int height;
    private boolean tipped;

    // coords of goal and tipper
    public Coordinates coords;
    public static Coordinates goal;

    // number of configs ran
    private int total = 1;
    private int unique = 1;

    /**
     * Creates initial TipOverConfig
     * @param filename data creates grid
     * @throws FileNotFoundException
     */
    public TipOverConfig(String filename) throws FileNotFoundException {
        try (Scanner in = new Scanner(new File(filename))) {
            in.hasNextLine();
            String line = in.nextLine();
            String[] fields = line.split("\\s+");

            width = Integer.parseInt(fields[0]);
            length = Integer.parseInt(fields[1]);

            this.grid = new String[width][length];
            this.coords = new Coordinates(fields[2], fields[3]);
            goal = new Coordinates(fields[4], fields[5]);

            for (int r = 0; r < width; r++) {
                line = in.nextLine();
                fields = line.split("\\s+");
                for (int c = 0; c < length; c++) {
                    this.grid[r][c] = fields[c];
                }
            }
        }
        this.height = Integer.parseInt(this.grid[coords.row()][coords.col()]);
    }

    /**
     * Creates neighboring configs
     * @param other current config
     * @param direction decides tiles next to current
     */
    public TipOverConfig(TipOverConfig other, String direction) {
        // copies data
        this.coords = other.coords;
        this.grid = new String[width][length];

        for (int r = 0; r < width; r++) {
            System.arraycopy(other.grid[r], 0, this.grid[r], 0, length);
        }

        // Depending on direction, will create a new config with new coordinates
        switch (direction) {
            case "North" -> {
                // checks if coords will be in bounds
                if(other.coords.row() - other.height >= 0) {
                    if (other.height > 1) {
                        if (this.grid[other.coords.row()-1][other.coords.col()].equals("0")
                                && this.grid[other.coords.row() - other.getHeight()][other.coords.col()].equals("0")) {
                            // decides if tower will be tipped over
                            for (int i = 1; i < other.height + 1; i++) {
                                this.grid[other.coords.row()][other.coords.col()] = "0";
                                this.grid[other.coords.row()-i][other.coords.col()] = "1";
                            }
                            this.coords = new Coordinates(other.coords.row() - 1, other.coords.col());
                            this.tipped = true;
                        } else {
                            this.coords = new Coordinates(other.coords.row() - 1, other.coords.col());
                            this.tipped = false;
                        }
                    }
                    else {
                        this.coords = new Coordinates(other.coords.row() -1, other.coords.col());
                        this.tipped = false;
                    }
                }
                else if (other.coords.row() - 1 >= 0) {
                    this.coords = new Coordinates(other.coords.row() - 1, other.coords.col());
                    this.tipped = false;
                }
            }
            case "East" -> {
                if(other.coords.col() + other.height < length) {
                    if (other.height > 1) {
                        if (this.grid[other.coords.row()][other.coords.col() + 1].equals("0")
                                && this.grid[other.coords.row()][other.coords.col() + other.getHeight()].equals("0")) {
                            for (int i = 1; i < other.height + 1; i++) {
                                this.grid[other.coords.row()][other.coords.col()] = "0";
                                this.grid[other.coords.row()][other.coords.col() + i] = "1";
                            }
                            this.coords = new Coordinates(other.coords.row(), other.coords.col() + 1);
                            this.tipped = true;
                        } else {
                            this.coords = new Coordinates(other.coords.row(), other.coords.col() + 1);
                            this.tipped = false;
                        }
                    }
                    else {
                        this.coords = new Coordinates(other.coords.row(), other.coords.col() + 1);
                        this.tipped = false;
                    }
                }
                else if (other.coords.col() + 1 < length) {
                    this.coords = new Coordinates(other.coords.row(), other.coords.col() + 1);
                    this.tipped = false;
                }
            }
            case "South" -> {
                if(other.coords.row() + other.height < width) {
                    if (other.height > 1) {
                        if (this.grid[other.coords.row()+1][other.coords.col()].equals("0")
                                && this.grid[other.coords.row() + other.getHeight()][other.coords.col()].equals("0")) {
                            for (int i = 1; i < other.height + 1; i++) {
                                this.grid[other.coords.row()][other.coords.col()] = "0";
                                this.grid[other.coords.row()+i][other.coords.col()] = "1";
                            }
                            this.coords = new Coordinates(other.coords.row() + 1, other.coords.col());
                            this.tipped = true;
                        } else {
                            this.coords = new Coordinates(other.coords.row() + 1, other.coords.col());
                            this.tipped = false;
                        }
                    }
                    else {
                        this.coords = new Coordinates(other.coords.row() + 1, other.coords.col());
                        this.tipped = false;
                    }
                }
                else if (other.coords.row() + 1 < width) {
                    this.coords = new Coordinates(other.coords.row() + 1, other.coords.col());
                    this.tipped = false;
                }
            }
            case "West" -> {
                if (other.coords.col() - other.height >= 0) {
                    if (other.height > 1) {
                        if (this.grid[other.coords.row()][other.coords.col() - 1].equals("0")
                                && this.grid[other.coords.row()][other.coords.col() - other.getHeight()].equals("0")) {
                            for (int i = 1; i < other.height + 1; i++) {
                                this.grid[other.coords.row()][other.coords.col()] = "0";
                                this.grid[other.coords.row()][other.coords.col() - i] = "1";
                            }
                            this.coords = new Coordinates(other.coords.row(), other.coords.col() - 1);
                            this.tipped = true;
                        } else {
                            this.coords = new Coordinates(other.coords.row(), other.coords.col() - 1);
                            this.tipped = false;
                        }
                    }
                    else {
                        this.coords = new Coordinates(other.coords.row(), other.coords.col() - 1);
                        this.tipped = false;
                    }
                }
                else if (other.coords.col() - 1 >= 0) {
                    this.coords = new Coordinates(other.coords.row(), other.coords.col() - 1);
                    this.tipped = false;
                }
            }
        }
        this.height = Integer.parseInt(this.grid[coords.row()][coords.col()]);
    }

    /**
     * Is the tower tipped over
     * @return tipped
     */
    public boolean tipped() {
        return tipped;
    }

    /**
     * Checks if the config is the solution
     * @return true if config coords is equal to the goal
     */
    @Override
    public boolean isSolution() {
        return this.coords.equals(goal);
    }

    /**
     * Gets neighbors of current config
     * @return Set of Configs
     */
    @Override
    public Set<Configuration> getNeighbors() {
        Set<Configuration> neighbors = new HashSet<>();

        TipOverConfig North = new TipOverConfig(this, "North");
        TipOverConfig East = new TipOverConfig(this, "East");
        TipOverConfig South = new TipOverConfig(this, "South");
        TipOverConfig West = new TipOverConfig(this, "West");

        // If the heights are 0, then it's not a valid config
        if (North.height != 0) {
            neighbors.add(North);
        }
        if (East.height != 0) {
            neighbors.add(East);
        }
        if (South.height != 0) {
            neighbors.add(South);
        }
        if (West.height != 0) {
            neighbors.add(West);
        }

        return neighbors;
    }

    /**
     * Adds to total configs ran
     */
    @Override
    public void addTotals() {
        total++;
    }

    /**
     * Adds to unique configs
     */
    @Override
    public void addUnique() {
        unique++;
    }

    /**
     * Returns total configs
     * @return total
     */
    @Override
    public int returnTotal() {
        return total;
    }

    /**
     * Returns unique configs
     * @return unique
     */
    @Override
    public int returnUnique() {
        return unique;
    }

    /**
     * Checks if configs are the same
     * @param o
     * @return true if they are
     */
    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if(o instanceof TipOverConfig) {
            TipOverConfig n = (TipOverConfig) o;
            for (int r = 0; r < width; r++) {
                for (int c = 0; c < length; c++) {
                    if (!this.grid[r][c].equals(n.grid[r][c])) {
                        return false;
                    }
                }
            }
            result = this.coords.equals(n.coords);
        }
        return result;
    }

    /**
     * Returns height
     * @return height
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns hashCode of coords and grid
     * @return int
     */
    @Override
    public int hashCode() {
        return this.coords.hashCode() + Arrays.deepHashCode(this.grid);
    }

    /**
     * String format of grid
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("    ");
        for(int i = 0; i < length; i++) {
            str.append("  ").append(i);
        }
        str.append("\n    ");
        str.append("___".repeat(length));

        for(int i = 0; i < width; i++) {
            str.append("\n ");
            str.append(i).append(" | ");
            for(int j = 0; j < length; j++) {
                if(this.coords.row() == i && this.coords.col() == j) {
                    str.append("*");
                }
                else if(goal.row() == i && goal.col() == j) {
                    str.append("!");
                }
                else {
                    str.append(" ");
                }

                if(this.grid[i][j].equals("0")) {
                    str.append("_ ");
                }
                else {
                    str.append(this.grid[i][j] + " ");
                }
            }
        }
        return str.toString();
    }
}

