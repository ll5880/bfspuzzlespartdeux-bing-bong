package puzzles.lunarlanding.model;

import solver.Configuration;
import solver.Solver;
import util.Coordinates;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains the model of the lunar landing puzzle
 * @author Lucie Lim
 * November 2021
 */
public class LunarLandingModel {

    private LunarLandingConfig currentConfig;
    private String filename;
    private Solver solver;
    private String figure;
    private boolean figureMoved;
    private boolean solvable;
    private boolean solved;
    private boolean loaded;

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

    public LunarLandingModel (String file) {
        this.observers = new LinkedList<>();
        filename = file;
        currentConfig = new LunarLandingConfig(filename);
        solver = new Solver();
        figure = null;
        figureMoved = false;
        loaded = true;
        solved = false;

    }

    public void load(String file) {
        this.currentConfig = new LunarLandingConfig(file);
        if (!currentConfig.getExceptionCaught()) {
            this.filename = file;
            show();
            announce("File loaded");
        }
    }

    public void reload() {
        this.currentConfig = new LunarLandingConfig(this.filename);
        System.out.println(currentConfig);
        announce("File loaded");
        loaded = true;
        solved = false;
    }

    public void choose(int row, int col) {
        figure = this.currentConfig.find(new Coordinates(row, col));
        if (figure == null){
            announce("No figure at that position");
        }
        loaded = false;
    }

    public void choose(int position) {
        int r = position / currentConfig.getRow();
        int c = position - (r * currentConfig.getRow());
        figure = this.currentConfig.find(new Coordinates(r, c));
        if (figure == null){
            announce("No figure at that position");
        }
        loaded = false;
    }

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
                            figureMoved = true;
                            if (this.currentConfig.isSolution()) {
                                announce("You Win");
                            }
                        } else {
                            announce("illegal move");
                            figureMoved = false;
                        }
                        break;
                    case "south":
                        if (this.currentConfig.canMove(figure, direction).equals("South")) {
                            currentConfig.setFigure(figure, this.currentConfig.MoveSouth(figure));
                            announce("");
                            show();
                            figureMoved = true;
                            if (this.currentConfig.isSolution()) {
                                announce("You Win");
                            }
                        } else {
                            announce("illegal move");
                            figureMoved = false;
                        }
                        break;
                    case "east":
                        if (this.currentConfig.canMove(figure, direction).equals("East")) {
                            currentConfig.setFigure(figure, this.currentConfig.MoveEast(figure));
                            announce("");
                            show();
                            figureMoved = true;
                            if (this.currentConfig.isSolution()) {
                                announce("You Win");
                            }
                        } else {
                            announce("illegal move");
                            figureMoved = false;
                        }
                        break;
                    case "west":
                        if (this.currentConfig.canMove(figure, direction).equals("West")) {
                            currentConfig.setFigure(figure, this.currentConfig.MoveWest(figure));
                            announce("");
                            show();
                            figureMoved = true;
                            if (this.currentConfig.isSolution()) {
                                announce("You Win");
                            }
                        } else {
                            announce("illegal move");
                            figureMoved = false;
                        }
                        break;
                    default:
                        announce("illegal command");
                        figureMoved = false;
                        break;
                }
            }
            else {
                announce("illegal command" + "\n" + "Legal commands are...\n");
                Help();
            }
        }
        loaded = false;
    }

    public void hint () {
        List<Configuration> solution = Solver.solve(currentConfig);
        if (isSolvable() == false) {
            announce("Unsolvable board");
        } else {
            solvable = true;
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

    //showing where we are on the board
    public void show() {
        System.out.println(currentConfig.toString());
    }

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

    public boolean getFigureMoved() {
        return figureMoved;
    }

    public boolean isSolvable() {
        List<Configuration> solution = Solver.solve(currentConfig);
        if (solution.size() == 0) {
            return false;
        }
        return true;
    }

    public LunarLandingConfig getCurrentConfig () {
        return currentConfig;
    }

    public boolean isLoaded() {
        return loaded;
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
