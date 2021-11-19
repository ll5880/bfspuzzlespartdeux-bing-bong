package util.ptui.demo;

import util.Observer;

import java.util.LinkedList;
import java.util.List;

/**
 * A class that maintains an integer counter and can be observed
 * by a UI. It represents the model in the MVC architectural pattern.
 *
 * @author RIT CS
 */
public class Counter{

    private int count;
    private final List < Observer< Counter, Object > > observers =
            new LinkedList<>();

    public Counter() {
        this.count = 0;
        this.notifyObservers();
    }

    public void addObserver( Observer< Counter, Object > observer ){
        observers.add( observer );
    }

    private void notifyObservers(){
        for ( Observer< Counter, Object > observer: observers ) {
            observer.update( this, null );
        }
    }

    public int getCount() {
        return this.count;
    }

    public void increment() {
        this.count += 1;
        this.notifyObservers();
    }

    public void decrement() {
        this.count -= 1;
        this.notifyObservers();
    }

}
