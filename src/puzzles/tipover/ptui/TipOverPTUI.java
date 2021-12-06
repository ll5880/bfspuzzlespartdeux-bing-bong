package puzzles.tipover.ptui;

import puzzles.tipover.model.*;
import puzzles.tipover.model.TipOverObserver;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Creates a text based version of the tip over game
 * @author YOUR NAME HERE
 * November 2021
 */
public class TipOverPTUI implements TipOverObserver<TipOverModel, Object>{

    // model of the game
    private TipOverModel model;

    // creates ptui object, displaying it
    public TipOverPTUI(String filename) throws FileNotFoundException {
        this.model = new TipOverModel(filename);
        initializeView();
    }

    // runs the game continuously and takes in commands, controller
    public void run(){
        this.model.show();
        System.out.println("File Loaded");
        Scanner in = new Scanner(System.in);
        for (; ; ) {
            System.out.print("game command: ");
            String line = in.nextLine();
            String[] fields = line.split("\\s+");
            if (fields.length > 0) {
                // quits the game
                if (fields[0].startsWith("q")) {
                    break;
                } else if (fields[0].startsWith("m")) {
                    // moves and takes in directions to move
                    if (fields[0].equals("move")) {
                        switch (fields[1]) {
                            case "north" -> {
                                this.model.getNorth();
                                // if the model is not the solution or tipped, display
                                // if it were tipped, it'd display in update function, same with solution
                                if (!model.isTipped() && !this.model.getConfig().isSolution()) {
                                    this.model.show();
                                }
                            }
                            case "east" -> {
                                this.model.getEast();
                                if (!model.isTipped() && !this.model.getConfig().isSolution()) {
                                    this.model.show();
                                }
                            }
                            case "south" -> {
                                this.model.getSouth();
                                if (!model.isTipped() && !this.model.getConfig().isSolution()) {
                                    this.model.show();
                                }
                            }
                            case "west" -> {
                                this.model.getWest();
                                if (!model.isTipped() && !this.model.getConfig().isSolution()) {
                                    this.model.show();
                                }
                            }
                            // displays help
                            default -> {
                                System.out.println("Illegal Command");
                                this.displayHelp();
                            }
                        }
                    }
                    // if command isn't recognized, it is an illegal command
                    else {
                        System.out.println("Illegal Command");
                        this.displayHelp();
                    }
                    // returns help and hints
                } else if (fields[0].startsWith("h")) {
                    if (fields[0].equals("help")) {
                        this.displayHelp();
                    } else if (fields[0].equals("hint")) {
                        this.model.getHint();
                        if (!model.isTipped()) {
                            this.model.show();
                        }
                    } else {
                        System.out.println("Illegal Command");
                        this.displayHelp();
                    }
                    //resets from the beginning
                } else if (fields[0].startsWith("r")) {
                    if (fields[0].equals("reload")) {
                        this.model.reload();
                        this.model.show();
                    }
                    // load new file/game
                } else if (fields[0].startsWith("l")) {
                    try {
                        model.loadNew(fields[1]);
                        this.model.show();
                    }
                    catch (FileNotFoundException e) {
                        System.out.println("File Not Found");
                    }
                }
            }
        }
    }

    /**
     * updates the game status with victory text, tip text, or unsolvable text
     * @param o
     * @param o2
     */
    @Override
    public void update(TipOverModel o, Object o2) {
        if (this.model.getConfig().isSolution()) {
            this.model.show();
            System.out.println("YOU WON");
        }
        if (this.model.isTipped()) {
            this.model.show();
            System.out.println("A tower has been tipped over");
        }
        if (!this.model.isSolvable()) {
            System.out.println("Unsolvable Board");
            System.out.println("Reload or Pick a New Board");
        }
    }

    /**
     * Initialize the view
     */
    public void initializeView() {
        this.model.addObserver(this);
        update( this.model, null );
    }

    /**
     * Displays all possible commands
     */
    public void displayHelp() {
        System.out.println("""
                Legal Commands are...
                \t> help : Show all commands.
                \t> move {north|south|east|west}: Go in given direction, possibly tipping a tower. (1 argument)
                \t> reload filename: Load the most recent file again.
                \t> load {board-file-name}: Load a new game board file. (1 argument)
                \t> hint Make the next move for me.
                \t> show Display the board.
                \t> quit""");
    }

    public static void main( String[] args ) throws FileNotFoundException {
        TipOverPTUI ptui = new TipOverPTUI(args[0]);
        ptui.run();
    }
}
