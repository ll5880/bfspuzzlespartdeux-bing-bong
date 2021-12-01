package puzzles.tipover;

import puzzles.tipover.model.TipOverConfig;
import solver.Solver;
import solver.Configuration;

import java.io.FileNotFoundException;
import java.util.List;


/**
 * DESCRIPTION
 * @author YOUR NAME HERE
 * November 2021
 */
public class TipOver {

    /*
     * code to read the file name from the command line and
     * run the solver on the puzzle
     */

    public static void main( String[] args ) throws FileNotFoundException {
        TipOverConfig config = new TipOverConfig(args[0]);
        List<Configuration> path = Solver.solve(config);
        int counter = 0;
        for (Configuration conf : path) {
            System.out.println("Step " + counter + ": ");
            System.out.println(conf + "\n");
            counter++;
        }
    }
}
