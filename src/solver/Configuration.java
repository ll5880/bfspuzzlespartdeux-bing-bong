package solver;

import puzzles.clock.*;

import java.util.Collection;
import java.util.*;

/**
 * Configuration abstraction for the solver algorithm
 *
 * @author Darian Cheung
 */
public interface Configuration {

    /*
     * List here the methods that the configurations of all the
     * puzzles must implement.
     * The project writeup explains that there are other acceptable designs,
     * so use of this interface is not required. However, for full design
     * credit, use of a shared solver that requires the implementation of
     * a certain abstraction from all puzzles is required.
     */

    /**
     * Returns whether the configuration is correct
     * @return boolean
     */
    boolean isSolution();

    /**
     * Returns the neighbor configurations
     * @return Hashset of Configurations
     */
    Set<Configuration> getNeighbors();
}
