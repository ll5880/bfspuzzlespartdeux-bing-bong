package solver;

import java.util.*;

public class ClockConfig implements Configuration {

    // Hours of the clock and end goal
    private static int hours, end;
    // Start time
    private int start;
    private int total = 1;
    private int unique = 1;

    /**
     * Creates a Clock Config
     * @param hours Total hours in clock
     * @param start Set time
     * @param end End time
     */
    public ClockConfig(int hours, int start, int end) {
        ClockConfig.hours = hours;
        this.start = start;
        ClockConfig.end = end;
    }

    /**
     * Creates a Clock Config
     * @param start Set time
     */
    public ClockConfig(int start) {
        this.start = start;
    }

    /**
     * Checks if current config is the solution
     * @return
     */
    @Override
    public boolean isSolution() {
        return this.start == end;
    }

    /**
     * Gets the neighbors of the current config
     * @return Hashset of Configs
     */
    @Override
    public Set<Configuration> getNeighbors() {
        Set<Configuration> neighbors = new HashSet<>();
        ClockConfig prev, next;

        // If the current time is 1 or end of clock, the neighbors wrap around
        if (start == 1) {
            prev = new ClockConfig(hours);
            next = new ClockConfig(start + 1);
        }
        else if (start == hours) {
            prev = new ClockConfig(start - 1);
            next = new ClockConfig(1);
        }
        else {
            prev = new ClockConfig(hours, start - 1, end);
            next = new ClockConfig(hours, start + 1, end);
        }
        neighbors.add(prev);
        neighbors.add(next);
        return neighbors;
    }

    @Override
    public void addTotals() {
        total++;
    }

    @Override
    public void addUnique() {
        unique++;
    }

    @Override
    public int returnTotal() {
        return total;
    }

    @Override
    public int returnUnique() {
        return unique;
    }

    /**
     * Equals method, checks if two objects are the same
     * @param other compared object
     * @return boolean
     */
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof ClockConfig) {
            ClockConfig n = (ClockConfig) other;
            result = this.start == n.start;
        }
        return result;
    }

    /**
     * Hashcode of object
     * @return int
     */
    public int hashCode() {
        return this.start;
    }

    /**
     * Returns string of clock config
     * @return String
     */
    public String toString() {
        return String.valueOf(start);
    }
}
