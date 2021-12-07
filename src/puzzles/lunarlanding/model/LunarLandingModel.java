package puzzles.lunarlanding.model;

import solver.Configuration;
import solver.Solver;
import util.Coordinates;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains the model of the lunar landing puzzle
 *
 * @author Lucie Lim
 * November 2021
 */
public class LunarLandingModel {

    private LunarLandingConfig currentConfig;
    private String filename;
    private Solver solver;
    private String figure;

    private List< LunarObserver< LunarLandingModel, Object > > observers;

    /*
     * Code here includes...
     * Additional data variables for anything needed beyond what is in
     *   the config object to describe the current state of the puzzle
     * Methods to support the controller part of the GUI, e.g., load, move
     * Methods and data to support the "subject" side of the Observer pattern
     *
     * WARNING: To support the hint command, you will likely have to do
     *   a cast of Config to LunarLandingConfig somewhere, since the solve
     *   method works with, and returns, objects of type Configuration.
     */

    /**
     * Construct a LunarLandingModel; there is only one configuration.
     */
    public LunarLandingModel (String file) {
        this.observers = new LinkedList<>();
        filename = file;
        currentConfig = new LunarLandingConfig(filename);
        solver = new Solver();
        figure = null;
    }

    /**
     * Loads a new puzzle
     *
     * @param file the new file with the puzzle.
     */
    public void load(String file) {
        this.currentConfig = new LunarLandingConfig(file);
        if (!currentConfig.getExceptionCaught()) {
            this.filename = file;
            show();
            announce("File loaded");
        }
    }

    /**
     * Reloads the puzzle
     */
    public void reload() {
        this.currentConfig = new LunarLandingConfig(this.filename);
        System.out.println(currentConfig);
        announce("File loaded");
    }

    /**
     * Choose a figure to be moved, given coordinates
     *
     * @param row the row of the chosen figure
     * @param col the column of the chosen figure
     */
    public void choose(int row, int col) {
        figure = this.currentConfig.find(new Coordinates(row, col));
        if (figure == null){
            announce("No figure at that position");
        }
    }

    /**
     * Choose a figure to be moved, given a number representing the position of the figure
     *
     * @param position the position of the figure on the board
     */
    public void choose(int position) {
        int r = position / currentConfig.getRow();
        int c = position - (r * currentConfig.getRow());
        figure = this.currentConfig.find(new Coordinates(r, c));
        if (figure == null){
            announce("No figure at that position");
        }
    }

    /**
     * Moves the figure given the proper command and direction
     *
     * @param go the command
     * @param direction the direction the figure is moving towards
     */
    public void go (String go, String direction) {
        if (figure != null) {
            if (go.equals("go")) {
                switch (direction) {
                    case "north":
                        System.out.println(currentConfig);
                        if (this.currentConfig.canMove(figure, direction).equals("North")) {
                            currentConfig.setFigure(figure, this.currentConfig.MoveNorth(figure));
                            announce("");
                            show();
                            if (this.currentConfig.isSolution()) {
                                announce("You Win");
                            }
                        } else {
                            announce("illegal move");
                        }
                        break;
                    case "south":
                        if (this.currentConfig.canMove(figure, direction).equals("South")) {
                            currentConfig.setFigure(figure, this.currentConfig.MoveSouth(figure));
                            announce("");
                            show();
                            if (this.currentConfig.isSolution()) {
                                announce("You Win");
                            }
                        } else {
                            announce("illegal move");
                        }
                        break;
                    case "east":
                        if (this.currentConfig.canMove(figure, direction).equals("East")) {
                            currentConfig.setFigure(figure, this.currentConfig.MoveEast(figure));
                            announce("");
                            show();
                            if (this.currentConfig.isSolution()) {
                                announce("You Win");
                            }
                        } else {
                            announce("illegal move");
                        }
                        break;
                    case "west":
                        if (this.currentConfig.canMove(figure, direction).equals("West")) {
                            currentConfig.setFigure(figure, this.currentConfig.MoveWest(figure));
                            announce("");
                            show();
                            if (this.currentConfig.isSolution()) {
                                announce("You Win");
                            }
                        } else {
                            announce("illegal move");
                        }
                        break;
                    default:
                        announce("illegal command");
                        break;
                }
            }
            else {
                announce("illegal command" + "\n" + "Legal commands are...\n");
                Help();
            }
        }
    }

    /**
     * Gives the user a hint, sets the current configuration to one of the configs in the solution path
     * of the puzzle.
     *
     * Will tell if a board is unsolvable or is a board is already solved.
     */
    public void hint () {
        List<Configuration> solution = Solver.solve(currentConfig);
        if (isSolvable() == false) {
            announce("Unsolvable board");
        } else {
            if (solution.size() > 1) {
                currentConfig = (LunarLandingConfig) solution.remove(1);
                show();
                announce("");
                if (currentConfig.isSolution()) {
                    announce("You Win!");
                }
            } else {
                announce("Board is already solved");
            }
        }
    }

    /**
     * shows the board
     */
    public void show() {
        System.out.println(currentConfig.toString());
    }

    /**
     * Print on standard out help for the game.
     */
    public void Help() {
        System.out.println( "show                              -- display the board" );
        System.out.println( "load (filename)                   -- loads a new puzzle from the provided file" );
        System.out.println( "quit                              -- quit the game" );
        System.out.println( "hint                              -- moves the correct figure 1 move towards the solution" );
        System.out.println( "go (north, south, east, west)     -- move a figure in a direction" );
        System.out.println( "reload                            -- restart to its initial state" );
        System.out.println( "choose (row) (column)             -- choose a figure to move" );
        System.out.println( "help                              -- show all commands" );
    }

    /**
     * checks if the current configuration is solvable
     *
     * @return boolean
     */
    public boolean isSolvable() {
        List<Configuration> solution = Solver.solve(currentConfig);
        if (solution.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Returns the current lunarlandingConfig
     *
     * @return LunarLandingConfig
     */
    public LunarLandingConfig getCurrentConfig () {
        return currentConfig;
    }

    /**
     * Add a new observer to the list for this model
     * @param obs an object that wants an
     *            {@link LunarObserver#update(Object, Object)}
     *            when something changes here
     */
    public void addObserver( LunarObserver< LunarLandingModel, Object > obs ) {
        this.observers.add( obs );
    }

    /**
     * Announce to observers the model has changed;
     */
    private void announce(String arg) {
        for (var obs : this.observers) {
            obs.update(this, arg);
        }
    }

}
