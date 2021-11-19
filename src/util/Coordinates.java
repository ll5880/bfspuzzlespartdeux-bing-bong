package util;

import java.util.*;

/**
 * A handy class for manipulating 2D locations and vectors.
 * The coordinates are integers, making them suitable for grid-based
 * computations.
 * <br><br>
 * Demonstration of Use
 * <pre>
        Coordinates origin = new Coordinates( 4, 7 );
        System.out.println( "Origin set at " + origin );

        int distance = 2;

        for ( Direction d: CARDINAL_NEIGHBORS ) {
            System.out.println(
                    "Distance " + distance +
                    " in direction " + d + " is " +
                    origin.sum( d.coords.multiply( distance ) )
            );
        }
        System.out.println();
        for ( Direction d: INTERCARDINAL_NEIGHBORS ) {
            System.out.println(
                    "Distance " + distance +
                    " in direction " + d + " is " +
                    origin.sum( d.coords.multiply( distance ) )
            );
        }
 </pre>
 * @author RIT CS
 * September 2021
 */
public class Coordinates {

    /**
     * An enumeration that names the eight common directions that one
     * moves around in a grid
     */
    public static enum Direction {
        NORTH( -1, 0 ),
        NORTHEAST( -1, 1 ),
        EAST( 0, 1 ),
        SOUTHEAST( 1, 1 ),
        SOUTH( 1, 0 ),
        SOUTHWEST( 1, -1 ),
        WEST( 0, -1 ),
        NORTHWEST( -1, -1 );

        /**
         * The relative coordinates associated with the direction
         */
        public final Coordinates coords;

        /**
         * Produce a direction from the individual relative coordinates.
         * @param rDelta row change
         * @param cDelta column change
         */
        Direction( int rDelta, int cDelta ) {
            this.coords = new Coordinates( rDelta, cDelta );
        }

        /**
         * See if the named direction is legitimate
         * @param dir the string name of the direction
         * @return true only if this name is valid. (Case must match.)
         */
        public static boolean isDirection( String dir ) {
            try {
                Direction dummy = Direction.valueOf( dir );
            }
            catch( IllegalArgumentException iae ) {
                return false;
            }
            return true;
        }
    }

    /**
     * The four principal "compass" directions
     */
    public static final Direction[] CARDINAL_NEIGHBORS = {
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    };

    /**
     * The four diagonal directions
     */
    public static final Direction[] INTERCARDINAL_NEIGHBORS = {
            Direction.NORTHWEST, Direction.NORTHEAST,
            Direction.SOUTHWEST, Direction.SOUTHEAST
    };

    private final int row;
    private final int col;

    /**
     * Initialize this object with the actual integer values.
     * @param row the row value
     * @param col the column value
     */
    public Coordinates( int row, int col ) {
        this.row = row;
        this.col = col;
    }

    /**
     * Initialize this object with an array of integer values.
     * @param pair array containing the row number, or y coordinate,
     *             increasing DOWNwards, followed by the
     *             column number, or x coordinate, increasing to the right
     * @rit.pre pair.length == 2
     */
    public Coordinates( int[] pair ) {
        this( pair[ 0 ], pair[ 1 ] );
    }

    /**
     * Initialize this object with strings representing the integer values.
     * {@link Integer#parseInt(String)} is used to parse the strings.
     * @see Coordinates#Coordinates(int, int)
     * @param rowStr row number as a string
     * @param colStr column number as a string
     */
    public Coordinates( String rowStr, String colStr ) {
        this( Integer.parseInt( rowStr ), Integer.parseInt( colStr ) );
    }

    /**
     * What is the row coordinate?
     * @return the row value stored in this Coordinates object
     */
    public int row() {
        return this.row;
    }

    /**
     * What is the column coordinate?
     * @return the column value stored in this Coordinates object
     */
    public int col() {
        return this.col;
    }

    /**
     * Compute the "difference" vector between this location and
     * another location.
     * This is useful, in combination with
     * {@link Coordinates#sum(Coordinates)},
     * for writing a loop over a sequence of locations.
     * <br>
     * Semantics: for all Coordinates a,b: a.sum(b.difference(a)) equals a.
     * @param c the other location
     * @return a new Coordinates object containing the row difference and the
     * column difference, i.e., what to add to this to get to the c location.
     */
    public Coordinates difference( Coordinates c ) {
        return new Coordinates( c.row - this.row, c.col - this.col );
    }

    /**
     * Add the "row" and "column" values of the argument to this object.
     * This is useful, in combination with
     * {@link Coordinates#difference(Coordinates)},
     * for writing a loop over a sequence of locations.
     * @param delta contains the increment to the row and column values (+/-)
     * @return a new Coordinates object containing the resulting location
     */
    public Coordinates sum( Coordinates delta ) {
        return new Coordinates( this.row + delta.row, this.col + delta.col );
    }

    /**
     * Magnify the Coordinate, interpreted as a vector, or
     * distance from the origin, by a factor.
     * @param factor the amount by which to multiply the row and column values
     * @return the new magnified Coordinate
     */
    public Coordinates multiply( int factor ) {
        return new Coordinates( this.row * factor, this.col * factor );
    }

    /**
     * Compare this object to another Coordinates object
     * @param o the other Coordinates object
     * @return true only if both the rows and columns are equal
     */
    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null ) return false;
        if ( o instanceof Coordinates that ) {
            return this.row == that.row && this.col == that.col;
        }
        return false;
    }

    /**
     * Compute a hash code for this object.
     * @return the result of using {@link Objects#hash(Object...)} on the row
     *         and column
     */
    @Override
    public int hashCode() {
        return Objects.hash( this.row, this.col );
    }

    /**
     * Produce a human-readable string of this object.
     * @return a string in the format "(row,column)"
     */
    @Override
    public String toString() {
        return "(" + this.row + ',' + this.col + ')';
    }

    /**
     * Demonstration of the uses of the Coordinates class
     * @param args not used
     */
    public static void main( String[] args ) {
        Coordinates origin = new Coordinates( 4, 7 );
        System.out.println( "Origin set at " + origin );

        int distance = 2;

        for ( Direction d: CARDINAL_NEIGHBORS ) {
            System.out.println(
                    "Distance " + distance +
                    " in direction " + d + " is " +
                    origin.sum( d.coords.multiply( distance ) )
            );
        }
        System.out.println();
        for ( Direction d: INTERCARDINAL_NEIGHBORS ) {
            System.out.println(
                    "Distance " + distance +
                    " in direction " + d + " is " +
                    origin.sum( d.coords.multiply( distance ) )
            );
        }
    }
}
