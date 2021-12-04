package puzzles.lunarlanding.model;

import solver.Configuration;
import solver.Solver;
import util.Coordinates;
import util.Observer;

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
    private int hintPath = 0;

    private List< Observer< LunarLandingModel, Object > > observers;

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

    }

    public void load(String file) {
        this.currentConfig = new LunarLandingConfig(file);
        if (!currentConfig.getExceptionCaught()) {
            this.filename = file;
            show();
            System.out.println("File loaded");
        }
    }

    public void reload() {
        this.currentConfig = new LunarLandingConfig(this.filename);
        System.out.println(currentConfig);
    }

    public void choose(int row, int col) {
        figure = this.currentConfig.find(new Coordinates(row, col));
        if (figure == null){
            System.out.println("No figure at that position");
        }
    }

    public void go (String go, String direction) {
        if (figure != null) {
            if (go.equals("go")) {
                switch (direction) {
                    case "north":
                        if (this.currentConfig.canMove(figure, direction).equals("North")) {
                            currentConfig.setFigure(figure, this.currentConfig.MoveNorth(figure));
                            System.out.println(currentConfig);
                            if (currentConfig.isSolution()) {
                                System.out.println("I WON!");
                            }
                        } else {
                            System.out.println("illegal move");
                        }
                        break;
                    case "south":
                        if (this.currentConfig.canMove(figure, direction).equals("South")) {
                            currentConfig.setFigure(figure, this.currentConfig.MoveSouth(figure));
                            System.out.println(currentConfig);
                            if (currentConfig.isSolution()) {
                                System.out.println("I WON!");
                            }
                        } else {
                            System.out.println("illegal move");
                        }
                        break;
                    case "east":
                        if (this.currentConfig.canMove(figure, direction).equals("East")) {
                            currentConfig.setFigure(figure, this.currentConfig.MoveEast(figure));
                            System.out.println(currentConfig);
                            if (currentConfig.isSolution()) {
                                System.out.println("I WON!");
                            }
                        } else {
                            System.out.println("illegal move");
                        }
                        break;
                    case "west":
                        if (this.currentConfig.canMove(figure, direction).equals("West")) {
                            currentConfig.setFigure(figure, this.currentConfig.MoveWest(figure));
                            System.out.println(currentConfig);
                            if (currentConfig.isSolution()) {
                                System.out.println("I WON!");
                            }
                        } else {
                            System.out.println("illegal move");
                        }
                        break;
                    default:
                        System.out.println("illegal command");
                        break;
                }
            }
            else {
                System.out.println("illegal command" + "\n" + "Legal commands are...\n");
                Help();
            }
        }
    }

    //uses solve to get the next config, changes the current config to the next
    //config
    public void hint () {
        List<Configuration> solution = Solver.solve(currentConfig);
        if (currentConfig.isSolution()) {
            announce("I WON!");
        } else if (solution.size() == 0) {
            announce("Unsolvable board");
        } else {
            currentConfig = (LunarLandingConfig) solution.remove(1);
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

    public LunarLandingConfig getCurrentConfig () {
        return currentConfig;
    }

    /**
     * Add a new observer to the list for this model
     * @param obs an object that wants an
     *            {@link Observer#update(Object, Object)}
     *            when something changes here
     */
    public void addObserver( Observer< LunarLandingModel, Object > obs ) {
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
