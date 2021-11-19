package puzzles.clock;

import solver.*;

import java.util.LinkedList;
import java.util.List;


/**
 * Main class for the "clock" puzzle.
 *
 * @author Darian Cheung
 */
public class Clock {

    /**
     * Run an instance of the clock puzzle.
     * @param args [0]: number of hours on the clock;
     *             [1]: starting time on the clock;
     *             [2]: goal time to which the clock should be set.
     */
    public static void main( String[] args ) {
        if ( args.length != 3 ) {
            System.out.println( "Usage: java Clock hours start end" );
        }
        else {
            // Creates clock config and will print the clock's path
            ClockConfig clock = new ClockConfig(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            System.out.println("Hours: " + args[0] + ", Start: " + args[1] + ", End: " + args[2]);
            List<Configuration> path = Solver.solve(clock);
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
