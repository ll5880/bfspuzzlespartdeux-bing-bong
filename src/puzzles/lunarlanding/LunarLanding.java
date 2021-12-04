package puzzles.lunarlanding;

import puzzles.lunarlanding.model.LunarLandingConfig;
import solver.Configuration;
import solver.Solver;

import java.util.List;

/**
 * Main program for LunarLanding puzzle.
 * @author Lucie Lim
 * November 2021
 */
public class LunarLanding {

    /*
     * code to read the file name from the command line and
     * run the solver on the puzzle
     */

    public static void main( String[] args ) {
        if ( args.length != 1) {
            System.out.println("No game file found.");
        }
        else{
            //fix the configuration
            LunarLandingConfig lunar = new LunarLandingConfig(args[0]);
            // prints out the solution, solution is not fully made yet
            List<Configuration> path = Solver.solve(lunar);
            if (path.size() > 0) {
                for (int i = 0; i < path.size(); i++) {
                    System.out.println("Step " + i + ": " + path.get(i));
                }
            }
            else {
                System.out.println("No Solution");
            }
        }

    }
}
