package puzzles.tipover.ptui;

import puzzles.tipover.model.*;
import puzzles.tipover.model.Observer;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * DESCRIPTION
 * @author YOUR NAME HERE
 * November 2021
 */
public class TipOverPTUI implements Observer<TipOverModel, Object>{

    private TipOverModel model;

    public TipOverPTUI(String filename) throws FileNotFoundException {
        this.model = new TipOverModel(filename);
        initializeView();
    }

    public void run() throws FileNotFoundException {
        this.model.show();
        System.out.println("File Loaded");
        Scanner in = new Scanner(System.in);
        for (; ; ) {
            System.out.print("game command: ");
            String line = in.nextLine();
            String[] fields = line.split("\\s+");
            if (fields.length > 0) {
                if (fields[0].startsWith("q")) {
                    break;
                } else if (fields[0].startsWith("m")) {
                    if (fields[0].equals("move")) {
                        switch (fields[1]) {
                            case "north" -> this.model.getNorth();
                            case "east" -> this.model.getEast();
                            case "south" -> this.model.getSouth();
                            case "west" -> this.model.getWest();
                            default -> {
                                System.out.println("Illegal Command");
                                this.displayHelp();
                            }
                        }
                    }
                    else {
                        System.out.println("Illegal Command");
                        this.displayHelp();
                    }
                } else if (fields[0].startsWith("h")) {
                    if (fields[0].equals("help")) {
                        this.displayHelp();
                    } else if (fields[0].equals("hint")) {
                        this.model.getHint();
                    } else {
                        System.out.println("Illegal Command");
                        this.displayHelp();
                    }
                } else if (fields[0].startsWith("r")) {
                    if (fields[0].equals("reload")) {
                        this.model.reload();
                    }
                } else if (fields[0].startsWith("l")) {
                    try {
                        model.loadNew(fields[1]);
                    }
                    catch (FileNotFoundException e) {
                        System.out.println("File Not Found");
                    }
                }
            }
        }
    }

    @Override
    public void update(TipOverModel o, Object o2) {
        if (this.model.getConfig().isSolution()) {
            System.out.println("YOU WON");
        }
        if (this.model.isTipped()) {
            System.out.println("A tower has been tipped over");
        }
        if (!this.model.isSolvable()) {
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
