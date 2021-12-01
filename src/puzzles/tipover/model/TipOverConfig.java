package puzzles.tipover.model;

import solver.*;
import util.Coordinates;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
/**
 * DESCRIPTION
 * @author YOUR NAME HERE
 * November 2021
 */

public class TipOverConfig implements Configuration {

    private String[][] grid;
    private static int width;
    private static int length;

    private int height;

    private Coordinates coords;
    private static Coordinates goal;

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

    public TipOverConfig(TipOverConfig other, String direction) {
        this.coords = other.coords;
        this.grid = new String[width][length];

        for (int r = 0; r < width; r++) {
            System.arraycopy(other.grid[r], 0, this.grid[r], 0, length);
        }

        switch (direction) {
            case "North" -> {
                if(other.coords.row() - other.height >= 0) {
                    if (other.height > 1) {
                        for (int i = 1; i < other.height + 1; i++) {
                            this.grid[other.coords.row()][other.coords.col()] = "0";
                            this.grid[other.coords.row() - i][other.coords.col()] = "1";
                        }
                        this.coords = new Coordinates(other.coords.row() - 1, other.coords.col());
                    }
                    else {
                        this.coords = new Coordinates(other.coords.row() - other.height, other.coords.col());
                    }
                }
            }
            case "East" -> {
                if(other.coords.col() + other.height < length) {
                    if (other.height > 1) {
                        for (int i = 1; i < other.height + 1; i++) {
                            this.grid[other.coords.row()][other.coords.col()] = "0";
                            this.grid[other.coords.row()][other.coords.col() + i] = "1";
                        }
                        this.coords = new Coordinates(other.coords.row(), other.coords.col() + 1);
                    }
                    else {
                        this.coords = new Coordinates(other.coords.row(), other.coords.col() + other.height);
                    }
                }
            }
            case "South" -> {
                if(other.coords.row() + other.height < width) {
                    if (other.height > 1) {
                        for (int i = 1; i < other.height + 1; i++) {
                            this.grid[other.coords.row()][other.coords.col()] = "0";
                            this.grid[other.coords.row() + i][other.coords.col()] = "1";
                        }
                        this.coords = new Coordinates(other.coords.row() + 1, other.coords.col());
                    }
                    else {
                        this.coords = new Coordinates(other.coords.row() + other.height, other.coords.col());
                    }
                }
            }
            case "West" -> {
                if(other.coords.col() - other.height >= 0) {
                    if (other.height > 1) {
                        for (int i = 1; i < other.height + 1; i++) {
                            this.grid[other.coords.row()][other.coords.col()] = "0";
                            this.grid[other.coords.row()][other.coords.col()-i] = "1";
                        }
                        this.coords = new Coordinates(other.coords.row(),other.coords.col() - 1);
                    }
                    else {
                        this.coords = new Coordinates(other.coords.row(), other.coords.col() - other.height);
                    }
                }
            }
        }
        this.height = Integer.parseInt(this.grid[coords.row()][coords.col()]);
    }

    @Override
    public boolean isSolution() {
        return this.coords.equals(goal);
    }

    @Override
    public Set<Configuration> getNeighbors() {
        Set<Configuration> neighbors = new HashSet<>();

        TipOverConfig North = new TipOverConfig(this, "North");
        TipOverConfig East = new TipOverConfig(this, "East");
        TipOverConfig South = new TipOverConfig(this, "South");
        TipOverConfig West = new TipOverConfig(this, "West");

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

    @Override
    public int hashCode() {
        return this.coords.hashCode() + Arrays.deepHashCode(this.grid);
    }

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

