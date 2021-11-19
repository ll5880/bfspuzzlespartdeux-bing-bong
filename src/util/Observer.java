package util;

import java.util.LinkedList;
import java.util.List;

/**
 * An interface representing any class whose objects get notified when
 * the objects they are observing update themselves.
 * <br><br>
 * Demonstration of Use
 *
 * <pre>
        //
        // Note that this class does not know about Obs.
        //
        class Subj {
            private List&lt; Observer&lt; Subj, String &gt; &gt; observers =
                    new LinkedList&lt;&gt;();
            private int x;
            public Subj( int x ) { this.x = x; }
            public void register( Observer&lt; Subj, String &gt; obs ) {
                this.observers.add( obs );
            }
            public void notifyObservers( String msg ) {
                for ( Observer&lt; Subj, String &gt; obs: this.observers ) {
                    obs.update( this, msg );
                }
            }
            public void increment() {
                this.x += 1;
                notifyObservers( "increment happened" );
            }
            public String toString() { return "Subj[" + this.x + ']'; }

            public int getX() { return this.x; }
        }

        //
        // Note that this class does know about Subj.
        //
        class Obs implements Observer&lt; Subj, String &gt; {
            public void update( Subj subject, String msg ) {
                System.out.println(
                        "OMG, " + subject + " says " + msg +
                        "; its value is now " + subject.getX()
                );
            }
        }

        Subj numHolder = new Subj( 1 );
        Obs subjObserver = new Obs();
        numHolder.register( subjObserver );
        numHolder.increment();
        numHolder.increment();
 </pre>
 *
 * @param <Subject>    the type of object an implementor of this interface
 *                     is observing
 * @param <ClientData> optional data the model can send to the observer
 *                     (null if nothing)
 * @author RIT CS
 */
public interface Observer< Subject, ClientData > {
    /**
     * The observed subject calls this method on each observer that has
     * previously registered with it. This version of the design pattern
     * follows the "push model" in that the subject can provide
     * ClientData to inform the observer about what exactly has happened.
     * But this convention is not required. It may still be necessary for
     * the observer to also query the subject to find out more about what has
     * happened. If this is a simple signal with no data attached,
     * or if it can safely be assumed that the observer already has a
     * reference to the subject, even the subject parameter may be null.
     * But as always this would have to be agreed to by designers of both sides.
     *
     * @param subject the object that wishes to inform this object
     *                about something that has happened.
     * @param data    optional data the model can send to the observer
     * @see <a href="https://sourcemaking.com/design_patterns/observer">the
     * Observer design pattern</a>
     */
    void update( Subject subject, ClientData data );

    /**
     * A very simple demonstration of the use of this interface
     * @param args not used
     */
    public static void main( String[] args ) {
        /*
         * Note that this class does not know about Obs.
         */
        class Subj {
            private final List< Observer< Subj, String > > observers =
                    new LinkedList<>();
            private int x;
            public Subj( int x ) { this.x = x; }
            public void register( Observer< Subj, String > obs ) {
                this.observers.add( obs );
            }
            public void notifyObservers( String msg ) {
                for ( Observer< Subj, String > obs: this.observers ) {
                    obs.update( this, msg );
                }
            }
            public void increment() {
                this.x += 1;
                notifyObservers( "increment happened" );
            }
            public String toString() { return "Subj[" + this.x + ']'; }

            public int getX() { return this.x; }
        }

        /*
         * Note that this class does know about Subj.
         */
        class Obs implements Observer< Subj, String > {
            public void update( Subj subject, String msg ) {
                System.out.println(
                        "OMG, " + subject + " says " + msg +
                        "; its value is now " + subject.getX()
                );
            }
        }

        Subj numHolder = new Subj( 1 );
        Obs subjObserver = new Obs();
        numHolder.register( subjObserver );
        numHolder.increment();
        numHolder.increment();
    }
}
