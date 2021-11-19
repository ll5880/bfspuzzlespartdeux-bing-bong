package puzzles.water;

import solver.Configuration;
import solver.Solver;
import solver.WaterConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Darian Cheung
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main( String[] args ) {
        if ( args.length < 2 ) {
            System.out.println(
                    ( "Usage: java Water amount bucket1 bucket2 ..." )
            );
        }
        else {

            // Prints the end goal for bucket of water
            System.out.print("Amount: " + args[0] + ", Buckets: [");

            // Creates max water bucket can hold, and current empty buckets
            ArrayList<Integer> buckets = new ArrayList<>();
            ArrayList<Integer> empty = new ArrayList<>();

            for(int i = 1; i < args.length; i++) {
                buckets.add(Integer.parseInt(args[i]));
                empty.add(0);
            }

            // prints the bucket's max holds
            for(int i = 1; i < args.length; i++) {
                if (i + 1 != args.length) {
                    System.out.print(args[i] + ", ");
                }
                else {
                    System.out.println(args[i] + "]");
                }
            }

            // Prints path of water config
            WaterConfig water = new WaterConfig(Integer.parseInt(args[0]), buckets, empty);
            List<Configuration> path = Solver.solve(water);
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
