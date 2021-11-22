package puzzles.lunarlanding.model;

import solver.Configuration;
import util.Coordinates;
import util.Grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;

/**
 * DESCRIPTION
 * @author Lucie Lim
 * November 2021
 */
public class LunarLandingConfig implements Configuration{
    private Grid<String> board;
    private int row;
    private int column;

    //coordinates of the lunar lander
    private Coordinates lunarLanderCoordinates;

    private HashMap<Coordinates, String> figures;

    //makes the initial configuration
    public LunarLandingConfig(String filename) {
        try (Scanner in = new Scanner(new File(filename))) {
            in.hasNextLine();
            String line = in.nextLine();
            String[] lines = line.split(" ");

            this.row = Integer.parseInt(lines[0]);
            this.column = Integer.parseInt(lines[1]);
            this.board = new Grid( "_", row, column);

            //makes the coordinates of the lunar lander
            lunarLanderCoordinates = new Coordinates(Integer.parseInt(lines[2]),Integer.parseInt(lines[3]));

            figures = new HashMap<>();

            //gets the info for the other robots and puts them into a hashmap
            String nextLine = in.nextLine();
            while (!nextLine.isEmpty()) {
                String[] nextLines = nextLine.split(" ");
                Coordinates robotCords =
                        new Coordinates(Integer.parseInt(nextLines[1]), Integer.parseInt(nextLines[2]));
                figures.put(robotCords, nextLines[0]);
                nextLine = in.nextLine();
            }

            //makes the grid
            Set<Coordinates> keys = figures.keySet();
            for (Coordinates key : keys){
                board.set(figures.get(key), key.row(), key.col());
            }
            board.set("!", lunarLanderCoordinates.row(), lunarLanderCoordinates.col());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LunarLandingConfig() {

    }

    public boolean isSolution() {
        return false;
    }

    public Set<Configuration> getNeighbors() {
        return new HashSet<>();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("     ");
        for (int num = 0; num < column; num++) {
            result.append(num + "  ");
        }
        result.append("\n" + "    ");

        for (int line = 0; line < column * 3; line++){
            result.append("_");
        }
        result.append("\n");

        for ( int r = 0; r < row; r++) {
            result.append(r + " |");
            for ( int c = 0; c < column; c++) {
                result.append("  ").append(board.get(r, c));
            }
            result.append( " |" + "\n");
        }
        return result.toString();
    }

}
