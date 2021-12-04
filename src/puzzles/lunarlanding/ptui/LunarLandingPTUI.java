package puzzles.lunarlanding.ptui;

import puzzles.lunarlanding.model.LunarLandingConfig;
import puzzles.lunarlanding.model.LunarLandingModel;
import solver.Configuration;
import solver.Solver;
import util.Observer;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class defintion for the textual view and controller
 *
 * @author Lucie Lim
 * November 2021
 */
public class LunarLandingPTUI {

    private LunarLandingModel model;
    private boolean chosenFigure = false;

    public LunarLandingPTUI(String file) {
        this.model = new LunarLandingModel(file);
    }

    //Controller

    /**
     * Read a command and execute loop.
     */
    private void run() {
            Scanner in = new Scanner(System.in);
            this.model.show();
            System.out.println("File loaded");
            for (; ; ) {
                System.out.print("game command: ");
                String line = in.nextLine();
                String[] words = line.split("\\s+");
                if (words.length > 0) {
                    //quit
                    if (words[0].startsWith("q")) {
                        break;
                    }
                    //reload
                    else if (words[0].startsWith("r")) {
                        this.model.reload();
                    }
                    //choose
                    else if (words[0].startsWith("c")) {
                        this.model.choose(Integer.parseInt(words[1]), Integer.parseInt(words[2]));
                        chosenFigure = true;
                    }
                    //load
                    else if (words[0].startsWith("l")) {
                        this.model.load(words[1]);
                    }
                    //go
                    else if (words[0].startsWith("g")) {
                        if (chosenFigure) {
                            this.model.go(words[0], words[1]);
                            chosenFigure = false;
                        } else {
                            System.out.println("Choose a character to move first");
                        }
                    }
                    //hint
                    else if (words[0].equals("hint")) {
                        this.model.hint();
                        this.model.show();
                    }
                    //show
                    else if (words[0].startsWith("s")) {
                        this.model.show();
                    } else {
                        //help
                        this.model.Help();
                    }
                }
            }
        }

    //view

    /**
     * Initialize the view
     */
//    public void initializeView() {
//        this.model.addObserver( this );
//        update( this.model, null );
//    }

//    public void update( ConcentrationModel o, Object arg ) {
//        // if arg is not null, it means the user wants to get the "cheat" board
//        // with all cards face up
//        displayBoard( this.model.getMoveCount(),
//                this.model.howManyCardsUp(),
//                arg == null ? this.model.getCards()
//                        : this.model.getCheat(),
//                false
//        );
//
//        // display a win if all cards are face up (not cheating)
//        if ( this.model.getCards().stream()
//                .allMatch( face -> face.isFaceUp() ) ) {
//            System.out.println( "YOU WIN!" );
//        }
//    }

    public static void main( String[] args ) {
        LunarLandingPTUI ptui = new LunarLandingPTUI(args[0]);
        ptui.run();
    }
}
