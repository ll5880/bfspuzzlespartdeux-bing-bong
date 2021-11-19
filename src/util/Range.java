package util;

import java.util.function.Consumer;

/**
 * Represent a range of integers for iteration in a simple for loop
 * or {@link Iterable#forEach(Consumer)} method call.
 * The class implements the {@link java.util.Collection}.
 * This class is an attempt to model the Python 3 range class in Java.
 * <br><br>
 * Demonstration of Use
 * <pre>
        Range zeroSix = Range.across( 6 ); // Show that ranges can be reused.
        System.out.println();
        for ( Range r : new Range[]{
                Range.empty(), zeroSix, zeroSix, Range.across( 2, 13 ),
                Range.across( 3, 12, 3 ), Range.across( 10, 0, -2 )
        } ) {
            System.out.print( r + ":" );
            r.forEach( i -&gt; System.out.print( " " + i ) );
            System.out.println();
            System.out.println();
        }
 </pre>
 *
 * @author James Heliotis
 */
public class Range extends java.util.AbstractCollection< Integer > {

    /**
     * the first value in the range
     * This field is not private so that the nested
     * class IntRangeIterator has easier access to it.
     */
    private final int start;

    /**
     * the value after the last value in the range
     * This field is not private so that the nested
     * class IntRangeIterator has easier access to it.
     */
    private final int end;

    /**
     * incremental value
     * This field is not private so that the nested
     * class IntRangeIterator has easier access to it.
     */
    private final int incr;

    /**
     * Create a range.<br>
     *
     * @param first the first value in the range
     * @param last  the value after the last value in the range
     * @param by    the incremental value
     * @rit.pre by != 0
     */
    private Range( int first, int last, int by ) {
        assert by != 0 : "by cannot be 0.";
        start = first;
        end = last;
        incr = by;
    }

    /**
     * Create a range.<br>
     *
     * @param first the first value in the range
     * @param last  the value after the last value in the range
     * @param by    the incremental value
     * @return the Range object
     * @rit.pre by != 0
     */
    public static Range across( int first, int last, int by ) {
        return new Range( first, last, by );
    }

    /**
     * Create a range of successive increasing integers.
     *
     * @param first the first value in the range
     * @param last  the value after the last value in the range
     * @return the Range object
     */
    public static Range across( int first, int last ) {
        return new Range( first, last, 1 );
    }

    /**
     * Create a range of successive increasing integers starting at zero.
     *
     * @param last the value after the last value in the range
     * @return the Range object
     */
    public static Range across( int last ) {
        return new Range( 0, last, 1 );
    }

    /**
     * Create an empty range of integers.
     * If you put this in a for loop, the loop will be skipped.
     * @return a new empty range
     */
    public static Range empty() {
        return new Range( 0, 0, 1 );
    }

    /**
     * {@inheritDoc}
     *
     * @return the number of values in this Range.
     */
    public int size() {
        int result;
        if ( incr > 0 ) {
            result = ( end - start ) % incr;
            result = ( result + end - start ) / incr;
        }
        else {
            result = ( start - end ) % -incr;
            result = ( result + start - end ) / -incr;
        }
        return Math.max( result, 0 );
    }

    private class IntRangeIterator implements java.util.Iterator< Integer > {

        /**
         * the current value of the iterator
         */
        private int counter;

        /**
         * Create the iterator. Initialize it to the
         * first value of the range.
         */
        public IntRangeIterator() {
            counter = start;
        }

        /**
         * Does the iterator have more elements?
         *
         * @return true iff there are more values in the range.
         */
        public boolean hasNext() {
            return ( incr > 0 ) ?
                    ( counter < end ) :
                    ( counter > end );
        }

        /**
         * Provide the next element in the iteration. If this is the first
         * call to this method, return the first value in the range.
         *
         * @return the first value in the range when called the first time,
         * and after that, the previous value plus the increment.
         * @throws java.util.NoSuchElementException if there are no more
         *                                          elements
         */
        public Integer next() {
            int result;
            if ( !hasNext() ) {
                throw new java.util.NoSuchElementException(
                        "value: " + ( counter + incr ) );
            }
            else {
                result = counter;
                counter = counter + incr;
                return result;
            }
        }

        /**
         * This method is not supported. Ranges are immutable.
         *
         * @throws UnsupportedOperationException because this cannot be called.
         */
        public void remove() {
            throw new UnsupportedOperationException(
                    "Range.IntRangeIterator.remove()" );
        }
    }

    /**
     * {@inheritDoc}
     */
    public java.util.Iterator< Integer > iterator() {
        return new IntRangeIterator();
    }

    /**
     * Display the computed parameters for this range.
     *
     * @return a string containing the range parameters
     */
    @Override
    public String toString() {
        return "Range(" + size() + ")[ from " +
               start + " to " + end + " by " + incr + " ]";
    }

    /**
     * Demonstrate the capabilities of the Range class
     * @param args not used
     */
    public static void main( String[] args ) {
        Range zeroSix = Range.across( 6 ); // Show that ranges can be reused.
        System.out.println();
        for ( Range r : new Range[]{
                Range.empty(), zeroSix, zeroSix, Range.across( 2, 13 ),
                Range.across( 3, 12, 3 ), Range.across( 10, 0, -2 )
        } ) {
            System.out.print( r + ":" );
            r.forEach( i -> System.out.print( " " + i ) );
            System.out.println();
            System.out.println();
        }
    }
}
