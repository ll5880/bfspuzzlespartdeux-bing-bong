package solver;

import java.lang.reflect.Array;
import java.util.*;

public class WaterConfig implements Configuration{

    // End goal
    private static int end;
    // Array of max water buckets can hold and current fill of buckets
    private static ArrayList<Integer> max;
    private ArrayList<Integer> buckets;
    private int total = 1;
    private int unique = 1;

    /**
     * Creates water config
     * @param end end goal
     * @param max max water bucket can hold
     * @param buckets current buckets
     */
    public WaterConfig(int end, ArrayList<Integer> max, ArrayList<Integer> buckets) {
        WaterConfig.end = end;
        WaterConfig.max = max;
        this.buckets = buckets;
    }

    /**
     * Creates water config
     * @param buckets current buckets
     */
    public WaterConfig(ArrayList<Integer> buckets) {
        this.buckets = buckets;
    }

    /**
     * Iterates through bucket and checks if any of them hold the amount of water as the end goal
     * @return boolean
     */
    @Override
    public boolean isSolution() {
        for (Integer b : this.buckets) {
            if (b == end) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fills bucket with water and creates config with new bucket status
     * @param val index of bucket
     * @return Configuration
     */
    public Configuration fill(int val) {
        ArrayList<Integer> copy = new ArrayList<>(buckets);
        copy.set(val, max.get(val));

        return new WaterConfig(copy);
    }

    /**
     * Empties bucket with water and creates config with new bucket status
     * @param val index of bucket
     * @return Configuration
     */
    public Configuration empty(int val) {
        ArrayList<Integer> copy = new ArrayList<>(buckets);
        copy.set(val, 0);

        return new WaterConfig(end, max, copy);
    }

    /**
     * Transfers bucket with water into another and creates config with new bucket statuses
     * @param b1 index of bucket
     * @param b2 index of other bucket
     * @return Configuration
     */
    public Configuration transfer(int b1, int b2) {
        ArrayList<Integer> copy = new ArrayList<>(buckets);

        if(buckets.get(b1) <= (max.get(b2) - this.buckets.get(b2))) {
            copy.set(b2, buckets.get(b1) + buckets.get(b2));
            copy.set(b1, 0);
        }
        else {
            copy.set(b2, max.get(b2));
            copy.set(b1, buckets.get(b1) - (max.get(b2) - this.buckets.get(b2)));
        }
        return new WaterConfig(end, max, copy);
    }

    /**
     * Gets neighbors of current config
     * @return Hashset of Configs
     */
    @Override
    public Set<Configuration> getNeighbors() {
        Set<Configuration> neighbors = new HashSet<>();

        for(int i = 0; i < buckets.size(); i++) {
            // if bucket is not full, fill
            if(buckets.get(i) != max.get(i)) {
                neighbors.add(fill(i));
            }
            // if bucket is not empty, empty
            if(buckets.get(i) != 0) {
                neighbors.add(empty(i));
            }
            // if bucket is not empty or max, transfer
            for(int j = 0; j < buckets.size(); j++) {
                if(buckets.get(i) != buckets.get(j)) {
                    neighbors.add(transfer(i, j));
                }
            }
        }
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
     * Checks of two configs are the same
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaterConfig that = (WaterConfig) o;
        return end == that.end && Objects.equals(max, that.max) && Objects.equals(buckets, that.buckets);
    }

    /**
     * Returns hashcode of config
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(end, max, buckets);
    }

    /**
     * Returns string of bucket values
     * @return String
     */
    @Override
    public String toString() {
        return String.valueOf(buckets);
    }
}
