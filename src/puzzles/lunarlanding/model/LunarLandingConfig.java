package puzzles.lunarlanding.model;

import solver.Configuration;
import util.Coordinates;
import util.Grid;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;

/**
 * A class to represent a single configuration in the  puzzle.
 *
 * @author Lucie Lim
 * November 2021
 */

public class LunarLandingConfig implements Configuration {
    private Grid<String> board;
    private int row;
    private int column;
    private int numOfConfigs = 0;
    private Coordinates lunarLanderCoordinates;

    //holds the info of the figures
    private HashMap<String, Coordinates> figures;

    /**
     * Construct the initial configuration from an input file
     *
     * @param filename the name of the file to read from
     * @catches FileNotFoundException if the file is not found
     *
     */
    public LunarLandingConfig(String filename) {
        try (Scanner in = new Scanner(new File(filename))) {
            in.hasNextLine();
            String line = in.nextLine();
            String[] lines = line.split(" ");

            this.row = Integer.parseInt(lines[0]);
            this.column = Integer.parseInt(lines[1]);
            this.board = new Grid("_", row, column);

            //makes the coordinates of the lunar lander
            lunarLanderCoordinates = new Coordinates(Integer.parseInt(lines[2]), Integer.parseInt(lines[3]));

            figures = new HashMap<>();

            //gets the info for the other robots and puts them into a hashmap
            String nextLine = in.nextLine();
            while (!nextLine.isEmpty()) {
                String[] nextLines = nextLine.split(" ");

                //gets all the coordinates of the figures including the explorer
                Coordinates figureCords =
                        new Coordinates(Integer.parseInt(nextLines[1]), Integer.parseInt(nextLines[2]));
                figures.put(nextLines[0], figureCords);
                nextLine = in.nextLine();
            }

            //makes the grid
            Set<String> keys = figures.keySet();
            for (String key : keys) {
                board.set(key, figures.get(key).row(), figures.get(key).col());
            }
            board.set("!", lunarLanderCoordinates.row(), lunarLanderCoordinates.col());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * The copy constructor takes a config, other, and makes a full "deep" copy
     * of its instance data.
     *
     * @param other the config to copy
     * @param direction the direction to which make configurations for
     * @param figureKey the key for a figure's configuration
     */
    public LunarLandingConfig(LunarLandingConfig other, String direction, String figureKey) {
        this.numOfConfigs = other.numOfConfigs;
        this.lunarLanderCoordinates = other.lunarLanderCoordinates;
        this.row = other.row;
        this.column = other.column;

        //this.figureKeysList = new ArrayList<>(other.figureKeysList);
        this.figures = new HashMap<>(other.figures);

        //makes a new Grid
        this.board = new Grid("_", row, column);

        Coordinates newFigCords = null;

        //changes the position of a figure
        if (direction.equals("NORTH")) {
            //north
            newFigCords = MoveNorth(figureKey);
        } else if (direction.equals("SOUTH")) {
            //south
            newFigCords = MoveSouth(figureKey);
        } else if (direction.equals("WEST")) {
            //west
            newFigCords = MoveWest(figureKey);
        } else {
            //east
            newFigCords = MoveEast(figureKey);
        }

        figures.put(figureKey, newFigCords);

        //places the correct figures in the copy grid
        Set<String> keys = figures.keySet();
        for (String key : keys){
            board.set(key, figures.get(key).row(), figures.get(key).col());
        }
        board.set("!", lunarLanderCoordinates.row(), lunarLanderCoordinates.col());

    }

    /**
     * A method that moves a figure north to another figure
     *
     * @param currentFigure the figure being moved
     * @return Coordinates of the figure after it moves to another figure
     */
    private Coordinates MoveNorth(String currentFigure) {
        Coordinates newFigCords = null;
        for (String key : figures.keySet()) {
            if (figures.get(key).col() == figures.get(currentFigure).col() &&
                    figures.get(key).row() < figures.get(currentFigure).row()) {
                //moves the figure
                newFigCords = new Coordinates(figures.get(key).row() + 1, figures.get(key).col());
            }
        }
        return newFigCords;
    }

    /**
     * A method that moves a figure South to another figure
     *
     * @param currentFigure the figure being moved
     * @return Coordinates of the figure after it moves to another figure
     */
    private Coordinates MoveSouth(String currentFigure) {
        Coordinates newFigCords = null;
        for (String key : figures.keySet()) {
            if (figures.get(key).col() == figures.get(currentFigure).col() &&
                    figures.get(key).row() > figures.get(currentFigure).row()) {
                newFigCords = new Coordinates(figures.get(key).row() - 1, figures.get(key).col());
            }
        }
        return newFigCords;
    }

    /**
     * A method that moves a figure West to another figure
     *
     * @param currentFigure the figure being moved
     * @return Coordinates of the figure after it moves to another figure
     */
    private Coordinates MoveWest(String currentFigure) {
        Coordinates newFigCords = null;
        for (String key : figures.keySet()) {
            if (figures.get(key).row() == figures.get(currentFigure).row() &&
                    figures.get(key).col() < figures.get(currentFigure).col()) {
                newFigCords = new Coordinates(figures.get(key).row(), figures.get(key).col() + 1);
            }
        }
        return newFigCords;
    }

    /**
     * A method that moves a figure East to another figure
     *
     * @param currentFigure the figure being moved
     * @return Coordinates of the figure after it moves to another figure
     */
    private Coordinates MoveEast(String currentFigure) {
        Coordinates newFigCords = null;
        for (String key : figures.keySet()) {
            if (figures.get(key).row() == figures.get(currentFigure).row() &&
                    figures.get(key).col() > figures.get(currentFigure).col()) {
                newFigCords = new Coordinates(figures.get(key).row(), figures.get(key).col() - 1);
            }
        }
        return newFigCords;
    }

    /**
     * The method checks if the puzzle has been solved
     *
     * @return boolean if the puzzle has been solved
     */

    public boolean isSolution() {
        if (lunarLanderCoordinates.equals(figures.get("E"))) {
            return true;
        }
        return false;
    }

    /**
     * The method gets the possible configurations of a move
     *
     * @return Set<Configuration> a list of possible configurations for that move
     */
    public Set<Configuration> getNeighbors() {
        HashSet<Configuration> neighbors = new HashSet<>();

        //makes 4 configs for each direction for a figure
        for (String currentFigure: figures.keySet()) {
            //North
            if (canMove(currentFigure, "north").equals("North")) {
                LunarLandingConfig figureNorth =
                        new LunarLandingConfig(this, "NORTH", currentFigure);
                neighbors.add(figureNorth);
            }
            //South
            if (canMove(currentFigure, "south").equals("South")) {
                LunarLandingConfig figureSouth =
                        new LunarLandingConfig(this, "SOUTH", currentFigure);
                neighbors.add(figureSouth);
            }
            //West
            if (canMove(currentFigure, "west").equals("West")) {
                LunarLandingConfig figureWest =
                        new LunarLandingConfig(this, "WEST", currentFigure);
                neighbors.add(figureWest);
            }
            //East
            if (canMove(currentFigure, "east").equals("East")) {
                LunarLandingConfig figureEast =
                        new LunarLandingConfig(this, "EAST", currentFigure);
                neighbors.add(figureEast);
            }
        }
        return neighbors;
    }

    /**
     * The method checks if there is no figure in between the figure being moved and the figure its being moved to
     *
     * @param a the figure being moved
     * @param b destination figure
     * @return boolean if there is something in between the 2 figures
     */
    private boolean nothingBetween(Coordinates a, Coordinates b) {
        for (String key : figures.keySet()) {
            Coordinates current = figures.get(key);
            if ((a.row() == b.row() && current.row() == a.row() &&
                    ((current.col() < b.col() && a.col() < current.col()) ||
                            (current.col() < a.col() && b.col() < current.col()))

            ) || (a.col() == b.col() && current.col() == a.col() &&
                    ((current.row() < b.row() && a.row() < current.row()) ||
                            (current.row() < a.row() && b.row() < current.row())))) {
                return false;
            }
        }
        return true;
    }

    /**
     * The method checks if a figure can be moved in a direction. Allows a figure to be moved in that direction if
     * another figure is in that direction
     *
     * @param currentFigure the figure being moved
     * @param direction destination figure
     * @return boolean if there is something in between the 2 figures
     */
    private String canMove(String currentFigure, String direction){
        //North
        if (direction.equals("north")) {
            for (String key : figures.keySet()) {
                if (figures.get(key).col() == figures.get(currentFigure).col() &&
                        figures.get(key).row() < figures.get(currentFigure).row() &&
                        nothingBetween(figures.get(currentFigure), figures.get(key))) {
                    //moves the figure
                    return "North";
                }
            }
        }
        //South
        if (direction.equals("south")) {
            for (String key : figures.keySet()) {
                if (figures.get(key).col() == figures.get(currentFigure).col() &&
                        figures.get(key).row() > figures.get(currentFigure).row() &&
                        nothingBetween(figures.get(currentFigure), figures.get(key))) {
                    return "South";
                }
            }
        }
        //West
        if (direction.equals("west")) {
            for (String key : figures.keySet()) {
                if (figures.get(key).row() == figures.get(currentFigure).row() &&
                        figures.get(key).col() < figures.get(currentFigure).col() &&
                        nothingBetween(figures.get(currentFigure), figures.get(key))) {
                    return "West";
                }
            }
        }
        //East
        if (direction.equals("east")) {
            for (String key : figures.keySet()) {
                if (figures.get(key).row() == figures.get(currentFigure).row() &&
                        figures.get(key).col() > figures.get(currentFigure).col() &&
                        nothingBetween(figures.get(currentFigure), figures.get(key))) {
                    return "East";
                }
            }
        }
        return "Can not move";
    }

    /**
     * Returns the string representation of the puzzle
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        //makes the initial first row
        result.append("\n" + "     ");
        for (int num = 0; num < column; num++) {
            result.append(num + "  ");
        }
        result.append("\n" + "   ");

        //makes the horizontal line between the initial first row and the grid
        for (int line = 0; line < column * 3; line++) {
            result.append("_");
        }
        result.append("\n");

        //prints the body of the grid
        for (int r = 0; r < row; r++) {
            result.append(r + " |");
            for (int c = 0; c < column; c++) {
                if (r == lunarLanderCoordinates.row() && c == lunarLanderCoordinates.col()
                        && figures.get("E").equals(lunarLanderCoordinates)) {
                    result.append(" ").append("!E");
                } else {
                    result.append("  ").append(board.get(r, c));
                }
            }
            result.append("\n");
        }

        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LunarLandingConfig that = (LunarLandingConfig) o;
        return row == that.row && column == that.column &&
                numOfConfigs == that.numOfConfigs && Objects.equals(board, that.board) &&
                Objects.equals(lunarLanderCoordinates, that.lunarLanderCoordinates) &&
                Objects.equals(figures, that.figures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, row, column, lunarLanderCoordinates, numOfConfigs, figures);
    }

}
