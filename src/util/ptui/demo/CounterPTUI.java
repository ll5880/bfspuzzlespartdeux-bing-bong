package util.ptui.demo;

import util.Observer;
import util.ptui.ConsoleApplication;

import java.io.PrintWriter;

/**
 * Demonstrate use of {@link util.ptui.ConsoleApplication}
 * @author RITCS
 * November 2021
 */
public class CounterPTUI
                extends ConsoleApplication
                implements Observer< Counter, Object > {

    private final static String PLUS = "+";
    private final static String MINUS = "-";

    private Counter model;
    private PrintWriter out;

    @Override
    public void init() {
        this.model = new Counter();
        this.model.addObserver( this );
    }

    @Override
    public void start( PrintWriter console ) {
        this.out = console;
        super.setOnCommand(
                PLUS, 0, ": increment counter",
                args -> model.increment()
        );
        super.setOnCommand(
                MINUS, 0, ": decrement counter",
                args -> model.decrement()
        );
    }

    @Override
    public void update( Counter model, Object o ) {
        this.out.println( model.getCount() );
    }

    public static void main( String[] args ) {
        ConsoleApplication.launch( CounterPTUI.class );
    }
}
