package util.ptui;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.util.Map.Entry;

/**
 * A class to do console-based user interaction in a manner similar to
 * how JavaFX does window-based interaction.
 * This class is designed to be inherited by any console application.
 * When {@link #launch(Class,String[])} is called with the subclass, an
 * instance of the given class is created and the argument array is saved.
 * (The subclass must have a default constructor.)
 * <br><br>
 * Use: For a subclass called <code>MyPTUI</code>, put this line in
 * your <code>main</code> method:
 * <br><br>
 * <code>ConsoleApplication.launch(&nbsp;TipOverPTUI.class,&nbsp;args&nbsp;);</code>
 *
 * @author RIT CS
 */
public abstract class ConsoleApplication {

    private String[] cmdLineArgs;

    private Thread eventThread;

    /* *** Support code for registering actions in the PTUI *************** */

    /**
     * Functional interface for all PTUI commands
     */
    public interface ConsoleHandler {
        /**
         * Run a command
         * @param commandArgs the strings entered <em>after</em>
         *                    the command name
         */
        public abstract void handle( String[] commandArgs  );
    }

    /**
     * The important information stored with each PTUI command
     * <ol>
     *     <li>the number of arguments needed for this command (fixed)</li>
     *     <li>a description of what the command does (for help)</li>
     *     <li>the code that runs when the user types this command</li>
     * </ol>
     */
    private static record CommandInfo(
            int numArgs, String helpMsg, ConsoleHandler handler ) {}

    private Map< String, CommandInfo > actions;

    /**
     * The prompt seen by the user in the console before they enter a command
     */
    public static final String PROMPT = "> ";

    private static final String[] NO_ARGS = new String[ 0 ];

    public static final String HELP = "help";
    public static final String QUIT = "quit";

    /**
     * An automatically added action that prints the list of actions
     * @param args not used
     */
    private void help( String[] args ) {
        this.out.println( "Legal commands are..." );
        for ( Entry< String, CommandInfo > legal: this.actions.entrySet() ) {
            this.out.print( '\t' + PROMPT + legal.getKey() + " " );
            CommandInfo info = legal.getValue();
            this.out.print( info.helpMsg );
            int numArgs = info.numArgs;
            this.out.println(
                    numArgs > 0
                            ? " (" + numArgs + " argument" + ((numArgs>1)?"s)":")")
                            : ""
            );
        }
        this.out.println( '\t' + PROMPT + QUIT );
        this.out.println();
    }

    /**
     * Add a new command to the repertoire for this PTUI.
     * @param command the string the user must type in after the prompt
     * @param numArgs how many arguments this command needs (fixed)
     * @param helpMsg a description of the command used by the help command
     * @param handler the code that gets executed when the user types
     *                this command
     */
    public void setOnCommand(
            String command, int numArgs, String helpMsg, ConsoleHandler handler
    ) {
        this.actions.put(
                command, new CommandInfo( numArgs, helpMsg, handler )
        );
    }

    /* *** Launch methods ***************************************************/

    /**
     * Run a console application where the command line arguments are ignored.
     * Use: For a subclass called <code>MyPTUI</code>, put this line in
     * your <code>main</code> method:
     * <br><br>
     * <code>ConsoleApplication.launch(&nbsp;TipOverPTUI.class&nbsp;);</code>
     * @see #launch(Class, String[])
     *
     * @param ptuiClass the class object that refers to the class to
     *             be instantiated
     */
    public static void launch(
            Class< ? extends ConsoleApplication > ptuiClass
    ) {
        launch( ptuiClass, NO_ARGS );
    }

    /**
     * Run a console application, with command line arguments.
     * <ol>
     * <li>
     *     An instance of a ConsoleApplication subclass is created.
     * </li>
     * <li>
     *     The passed in string arguments are copied and saved.
     * </li>
     * <li>
     *     The class's default constructor is run.
     * </li>
     * <li>
     *     The subclass's {@link #init() init} method is run.
     * </li>
     * <li>
     *     A new thread is spawned.
     *     <ol>
     *     <li>
     *         A {@link Scanner Scanner} and
     *         {@link java.io.Writer Writer} are created and connected to a
     *         text console (currently stdin and stdout).
     *     </li>
     *     <li>
     *         The class's {@link #start(PrintWriter) start} method is
     *         called with the Writer as an argument.
     *     </li>
     *     <li>
     *         Inside the start method the legal commands for this application
     *         need to be set up using
     *         {@link #setOnCommand(String, int, String, ConsoleHandler) setOnCommand}.
     *     </li>
     *     <li>
     *         After the start method returns, this application processes
     *         commands that have been set up, gives an error message for
     *         badly formed commands, and then terminates when the user types
     *         {@link #QUIT the quit string}.
     *     </li>
     *     </ol>
     * </li>
     * </ol>
     * Use: For a subclass called <code>MyPTUI</code>, put this line in
     * your <code>main</code> method:
     * <br><br>
     * <code>ConsoleApplication.launch(&nbsp;TipOverPTUI.class,&nbsp;args&nbsp;);</code>
     *
     * @param ptuiClass the class object that refers to the class to
     *             be instantiated
     * @param args the array of strings from the command line
     */
    public static void launch(
            Class< ? extends ConsoleApplication > ptuiClass,
            String[] args
    ) {
        try {
            Constructor< ? > ctor = ptuiClass.getConstructor();
            ConsoleApplication ptuiApp =
                    (ConsoleApplication)ctor.newInstance();
            ptuiApp.cmdLineArgs = Arrays.copyOf( args, args.length );

            try {
                try {
                    ptuiApp.init();
                }
                catch( Exception e ) {
                    throw new InitException( e );
                }
                ptuiApp.actions = new HashMap<>();
                ptuiApp.eventThread = new Thread( ptuiApp.new Runner() );
                ptuiApp.eventThread.start();
                ptuiApp.eventThread.join();
            }
            catch( InterruptedException ie ) {
                System.err.println( "Console event thread interrupted" );
            }
            catch( InitException e ) {
                System.err.println(
                        "ConsoleApplication launch phase failed. "
                );
                e.printStackTrace( System.err );
            }
        }
        catch( NoSuchMethodException nsme ) {
            System.err.println( "Problem invoking ConsoleApp's constructor:" );
            System.err.println( nsme.getMessage() );
        }
        catch( InvocationTargetException ite ) {
            System.err.println( "Problem with ConsoleApp instance:" );
            System.err.println( ite.getMessage() );
        }
        catch( InstantiationException ie ) {
            System.err.println( "Can't instantiate Console App:" );
            System.err.println( ie.getMessage() );
        }
        catch( IllegalAccessException iae ) {
            System.err.println( iae.getMessage() );
        }
    }

    /**
     * There is no need for a constructor in this superclass.
     */
    protected ConsoleApplication() {}

    private PrintWriter out = null;

    private class Runner implements Runnable {

        public Runner() {}

        public void run() {

            setOnCommand( HELP,
                          0, ": Show all commands.",
                          ConsoleApplication.this::help
            );


            // We don't put the PrintWriter in try-with-resources because
            // we don't want it to be closed. The Scanner can close.
            ConsoleApplication.this.out = null;
            try ( Scanner consoleIn = new Scanner( System.in ) ) {
                try {
                    out = new PrintWriter(
                            new OutputStreamWriter( System.out ), true );
                    ConsoleApplication.this.start( out );
                    boolean quit = false;
                    out.print( PROMPT ); out.flush();
                    while ( consoleIn.hasNextLine() ) {
                        String nextCommand = consoleIn.nextLine();
                        String[] operands = nextCommand.split( "\\s+" );
                        String commandName = operands[ 0 ].toLowerCase();
                        if ( commandName.equals( QUIT ) ) {
                            out.println( "Closing application." );
                            quit = true;
                            break;
                        }
                        Map< String, CommandInfo > actionMap =
                                ConsoleApplication.this.actions;
                        if ( actionMap.containsKey( commandName ) &&
                             actionMap.get( commandName ).numArgs ==
                             operands.length - 1 ) {
                            operands = Arrays.copyOfRange(
                                    operands, 1, operands.length
                            );
                            actionMap.get( commandName )
                                    .handler.handle( operands );
                        }
                        else {
                            out.println( "Illegal command" );
                            ConsoleApplication.this.help( NO_ARGS );
                        }
                        out.print( PROMPT ); out.flush();
                    }
                    if ( !quit ) {
                        out.println( "End of console input reached." );
                    }
                }
                catch( Exception e ) {
                    System.err.println( "ConsoleApplication event thread" );
                    System.err.println( "During execution of main app" );
                    e.printStackTrace( System.err );
                }
                try {
                    stop();
                }
                catch( Exception e ) {
                    System.err.println( "ConsoleApplication event thread" );
                    System.err.println( "During execution of app stop code" );
                    e.printStackTrace( System.err );
                }
            }
        }
    }

    /**
     * Fetch the application's command line arguments
     * @return the string array that was passed to launch, if any, or else
     *         an empty array
     */
    public List< String > getArguments() {
        return Arrays.asList( this.cmdLineArgs );
    }

    /**
     * For an exception that occurs during the init phase
     */
    private static class InitException extends Exception {
        public InitException( Exception orig ) {
            super( "ConsoleApplication launch: init phase failed.", orig );
        }
    }

    /**
     * A do-nothing setup method that can be overwritten by subclasses
     * when necessary
     * @throws Exception in case an overriding subclass's method does
     */
    public void init() throws Exception {}

    /**
     * Subclasses implement this method to set up their command line
     * protocol and actions.
     *
     * @param console Where the UI should print output. It is recommended to save
     *                this object in a field in the subclass.
     * @throws Exception in case an overriding subclass's method does
     */
    public abstract void start( PrintWriter console ) throws Exception;

    /**
     * A do-nothing teardown method that can be overwritten by subclasses
     * when necessary.
     * @throws Exception in case an overriding subclass's method does
     */
    public void stop() throws Exception {}

}
