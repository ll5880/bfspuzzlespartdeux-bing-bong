package puzzles.lunarlanding.ptui;

import puzzles.lunarlanding.model.LunarLandingConfig;
import puzzles.lunarlanding.model.LunarLandingModel;
import puzzles.lunarlanding.model.LunarObserver;
import puzzles.tipover.model.TipOverObserver;
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
public class LunarLandingPTUI implements LunarObserver<LunarLandingModel, Object> {

    private LunarLandingModel model;
    private boolean chosenFigure = false;
    private boolean reachedSolution = false;

    public LunarLandingPTUI(String arg) {
        this.model = new LunarLandingModel(arg);
        this.model.load(arg);
        initializeView();
    }

    //Controller

    /**
     * Read a command and execute loop.
     */
    private void run() {
        Scanner in = new Scanner(System.in);
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
                        //reload a puzzle = solution not reached
                        reachedSolution = false;
                    }
                    //choose
                    else if (words[0].startsWith("c")) {
                        this.model.choose(Integer.parseInt(words[1]), Integer.parseInt(words[2]));
                        chosenFigure = true;
                    }
                    //load
                    else if (words[0].startsWith("l")) {
                        this.model.load(words[1]);
                        reachedSolution = false;
                    }
                    //go
                    else if (words[0].startsWith("g")) {
                        if (chosenFigure) {
                            reachedSolution = false;
                            this.model.go(words[0], words[1]);
                            chosenFigure = false;

                        } else {
                            System.out.println("Choose a character to move first");
                        }
                    }
                    //hint
                    else if (words[0].equals("hint")) {
                        this.model.hint();
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
        //the hint method
        if (o != null){
            System.out.println(o);
        }

        //if a figure has been moved
//        if (this.model.getFigureMoved()) {
//            this.model.show();
//            reachedSolution = false;
//        }



    }

    /**
     * Initialize the view
     */
    public void initializeView(){
        this.model.addObserver(this);
        update(this.model, "");

    }

    public static void main( String[] args ) {
        LunarLandingPTUI ptui = new LunarLandingPTUI(args[0]);
        ptui.run();
    }

}
