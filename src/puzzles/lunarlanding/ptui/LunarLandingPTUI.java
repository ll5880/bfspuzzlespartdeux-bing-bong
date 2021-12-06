package puzzles.lunarlanding.ptui;

import puzzles.lunarlanding.model.LunarLandingConfig;
import puzzles.lunarlanding.model.LunarLandingModel;
import puzzles.tipover.model.Observer;
import solver.Configuration;
import solver.Solver;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Class definition for the textual view and controller
 *
 * @author Lucie Lim
 * November 2021
 */
public class LunarLandingPTUI implements Observer< LunarLandingModel, Object > {

    private LunarLandingModel model;
    private boolean chosenFigure = false;

    public LunarLandingPTUI(String arg) {
        this.model = new LunarLandingModel(arg);
        this.model.load(arg);
//        initializeView();
    }

    //Controller

    /**
     * Read a command and execute loop.
     */
    private void run() {
            Scanner in = new Scanner(System.in);
            //System.out.println("File loaded");
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
    @Override
    public void update(LunarLandingModel lunarLandingModel, Object o) {
        if (this.model.getCurrentConfig().isSolution()) {
            model.show();
            System.out.println("You win");
        }
        //if a figure has been moved
        if (this.model.getFigureMoved()) {
            model.show();
        } else {
            System.out.println("Test");
        }

        //if a board is not solvable
        if (!this.model.isSolvable()) {
            System.out.println("Unsolvable board");
            System.out.println("pick new board");
        }
    }

    /**
     * Initialize the view
     */
//    public void initializeView() {
//        this.model.addObserver( this );
//        update( this.model, null );
//    }

    public void initializeView(){
        this.model.addObserver(this);
        update(this.model, null);

    }

    public static void main( String[] args ) {
        LunarLandingPTUI ptui = new LunarLandingPTUI(args[0]);
        ptui.run();
    }

}
