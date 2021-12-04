package solver;

import java.util.*;

import puzzles.clock.*;

/**
 * This class contains a universal algorithm to find a path from a starting
 * configuration to a solution, if one exists
 *
 * @author Darian Cheung
 */
public class Solver {

    public static List<Configuration> solve(Configuration config) {

        // Predecessor Map to access and store Configs
        Map<Configuration, Configuration> predecessors = new HashMap<>();
        // Queue and Deque Configs
        Queue<Configuration> queue = new LinkedList<>();
        // Path, shortest solution
        List<Configuration> path = new LinkedList<>();

        //counters for total configs

        // add initial configs to queue and map to store and later trace back to.
        queue.add(config);
        predecessors.put(config, null);

        while (!queue.isEmpty()) {
            // checks queue if any that pass are the solution
            Configuration current = queue.remove();
            if (current.isSolution()) {
                // hops from the solution config to the config it's attached to until it gets to config started with
                path.add(0, current);
                Configuration node = predecessors.get(current);
                while(node != config && node != null) {
                    path.add(0, node);
                    node = predecessors.get(node);
                }
                if (node != null) {
                    path.add(0, config);
                }
                return path;
            }
            // get the neighbor of configs to store in queue and map if current config is not the solution
            for (Configuration nbr : current.getNeighbors()) {
                if (!predecessors.containsKey(nbr)) {
                    predecessors.put(nbr, current);
                    queue.offer(nbr);
                    config.addTotals();
                    config.addUnique();
                }
                else {
                    config.addTotals();
                }
            }
        }
        // print configs
        return path;
    }
}