package util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

/**
 * A generic two-dimensional puzzle board.
 * This is not much more useful than using a plain 2D primitive array,
 * except that hashCode and equals are simplified by being defined here,
 * and this class accepts Coordinates instances in lieu of pairs of integers.
 * <br><br>
 * Demonstration of Use
 * <pre>
        record Letter( char val ) {}
        final int width = 3;
        final int height = 2;
        final Letter blank = new Letter( ' ' );
        Grid&lt; Letter &gt; myGrid = new Grid&lt;&gt;( blank, height, width );
        System.out.println( myGrid );

        int row = 0, col = 0;
        for ( char c: new char[]{'A','B','C','D','E','F'} ) {
            myGrid.set( new Letter( c ), row, col );
            col = ( col + 1 ) % width;
            if ( col == 0 ) row += 1;
        }
        System.out.println( myGrid );

        myGrid.set( new Letter( 'Γ' ), 0, 2 );
        myGrid.set( new Letter( 'Δ' ), 1, 0 );
        myGrid.set( new Letter( 'Z' ), 1, 2 );
        System.out.println( myGrid );

        for ( Coordinates test: new Coordinates[]{
                new Coordinates( 1, 1 ),
                new Coordinates( -1, 2 ),
                new Coordinates( 0, 3 )
        } ) {
            System.out.println(
                    test + " in bounds? " + myGrid.legalCoords( test )
            );
        }
 </pre>
 *
 * @author RIT CS
 * September 2021
 */
public class Grid< Thing> {

    private final int nRows;
    private final int nCols;
    private final Thing[][] spots;

    /**
     * Construct a grid using a default element value
     * @param init value to be assigned to all locations
     * @param height number of rows
     * @param width number of columns
     */
    public Grid( Thing init, int height, int width ) {
        this.nRows = height;
        this.nCols = width;
        //noinspection unchecked
        this.spots = (Thing[][])
                Array.newInstance( init.getClass(), height, width );
        for ( int r = 0; r < height; ++r ) {
            Arrays.fill( spots[ r ], init );
        }
    }

    /**
     * Copy constructor.
     * Grid elements are copied using assignment.
     * That means that the elements in the grid are shared,
     * if they are objects.
     * As long as the element type Thing is immutable, this is fine.
     * @param other the original grid to be copied
     */
    public Grid( Grid< Thing > other ) {
        this.nRows = other.nRows;
        this.nCols = other.nCols;
        //noinspection unchecked
        this.spots = (Thing[][])
                Array.newInstance(
                        other.spots[0][0].getClass(),
                        this.nRows, this.nCols
                );
        for ( int r = 0; r < this.nRows; ++r ) {
            this.spots[ r ] = Arrays.copyOf( other.spots[ r ], this.nCols );
        }
    }

    /**
     * What is the height of this grid?
     * @return the number of rows in the grid
     */
    public int getNRows() {
        return this.nRows;
    }

    /**
     * What is the width of this grid?
     * @return the number of columns in the grid
     */
    public int getNCols() {
        return this.nCols;
    }

    /**
     * Are the given coordinates legal?
     * @param r row number
     * @param c column number
     * @return true only if r is in [0,height) and c is in [0,width)
     */
    public boolean legalCoords( int r, int c ) {
        return
                r >= 0 && r < this.nRows &&
                c >= 0 && c < this.nCols;
    }

    /**
     * Are the given coordinates legal?
     * @param coord the coordinates to be tested
     * @return true only if r is in [0,height) and c is in [0,width)
     */
    public boolean legalCoords( Coordinates coord ) {
        return legalCoords( coord.row(), coord.col() );
    }

    /**
     * Fetch a value from the grid
     * @param r row where the value is stored
     * @param c column where the value is stored
     * @return the value stared at location (r,c)
     */
    public Thing get( int r, int c ) {
        return spots[ r ][ c ];
    }

    /**
     * Fetch a value from the grid
     * @param coord location where the value is stored
     * @return the value stared at the given location
     */
    public Thing get( Coordinates coord ) {
        return spots[ coord.row() ][ coord.col() ];
    }

    /**
     * Change the value on the grid
     * @param t the new value
     * @param r row where the value is to be placed
     * @param c column where the value is to be placed
     */
    public void set( Thing t, int r, int c ) {
        spots[ r ][ c ] = t;
    }

    /**
     * Change the value on the grid
     * @param t the new value
     * @param coord where the value is to be placed
     */
    public void set( Thing t, Coordinates coord ) {
        spots[ coord.row() ][ coord.col() ] = t;
    }

    /**
     * Compare this grid to another grid
     * @param o the other grid (or another object)
     * @return true only if
     *         o refers to a Grid,
     *         the dimensions of this Grid and o are the same, and
     *         all corresponding elements of the grids are the same
     *         by using {@link Arrays#deepEquals(Object[], Object[])}
     */
    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null ) return false;
        if ( o instanceof Grid< ? > grid ) {
            return Arrays.deepEquals( spots, grid.spots );
        }
        return false;
    }

    /**
     * Compute a hash code for this grid.
     * @return a value based on {@link Arrays#deepHashCode(Object[])}
     */
    @Override
    public int hashCode() {
        int result = Objects.hash( nRows, nCols );
        result = 31 * result + Arrays.deepHashCode( spots );
        return result;
    }

    /**
     * Create a string representing this grid.
     * @return a one-line string where each row's toString() values are
     *         concatenated using spaces, and the rows are separated and
     *         surrounded by vertical bars ("|")
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder( "|" );
        for ( Thing[] row: this.spots ) {
            for ( Thing thing: row ) {
                result.append( ' ' ).append( thing );
            }
            result.append( " |" );
        }
        return result.toString();
    }

    /**
     * Demonstration program for this Grid class
     * @param args not used
     */
    public static void main( String[] args ) {
        record Letter( char val ) {}
        final int width = 3;
        final int height = 2;
        final Letter blank = new Letter( ' ' );
        Grid< Letter > myGrid = new Grid<>( blank, height, width );
        System.out.println( myGrid );

        int row = 0, col = 0;
        for ( char c: new char[]{'A','B','C','D','E','F'} ) {
            myGrid.set( new Letter( c ), row, col );
            col = ( col + 1 ) % width;
            if ( col == 0 ) row += 1;
        }
        System.out.println( myGrid );

        myGrid.set( new Letter( 'Γ' ), 0, 2 );
        myGrid.set( new Letter( 'Δ' ), 1, 0 );
        myGrid.set( new Letter( 'Z' ), 1, 2 );
        System.out.println( myGrid );

        for ( Coordinates test: new Coordinates[]{
                new Coordinates( 1, 1 ),
                new Coordinates( -1, 2 ),
                new Coordinates( 0, 3 )
        } ) {
            System.out.println(
                    test + " in bounds? " + myGrid.legalCoords( test )
            );
        }

    }
}
