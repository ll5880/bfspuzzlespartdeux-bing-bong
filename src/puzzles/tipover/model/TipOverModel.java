package puzzles.tipover.model;

import solver.Configuration;
import solver.Solver;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

/**
 * Creates a model to be displayed
 * @author Darian Cheung, Team bingbong
 * November 2021
 */
public class TipOverModel{

    // information to make the configs
    private String filename;
    private boolean tipped;
    public TipOverConfig config;
    public TipOverConfig startConfig;
    private List<Configuration> path;
    private List< TipOverObserver< TipOverModel, Object > > observers;
    private boolean solvable = true;

    /**
     * Creates Model
     * @param filename
     * @throws FileNotFoundException
     */
    public TipOverModel(String filename) throws FileNotFoundException {
        this.observers = new LinkedList<>();
        this.filename = filename;
        this.config = new TipOverConfig(this.filename);
        this.startConfig = new TipOverConfig(this.filename);
        this.path = Solver.solve(this.config);
    }

    /**
     * Returns tipped
     * @return tipped
     */
    public boolean isTipped() {
        return tipped;
    }

    /**
     * Returns current config
     * @return config
     */
    public Configuration getConfig() {
        return this.config;
    }

    /**
     * Displays config
     */
    public void show() {
        System.out.println(this.config);
    }

    /**
     * Sets config to initial state
     */
    public void reload() {
        tipped = false;
        solvable = true;
        this.config = startConfig;
        this.announce(null);
    }

    /**
     * Loads new config
     * @param filename file of new game
     * @throws FileNotFoundException
     */
    public void loadNew(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.startConfig = new TipOverConfig(this.filename);
        this.config = new TipOverConfig(this.filename);
        this.announce(null);
    }

    /**
     * Returns north config of current
     */
    public void getNorth() {
        TipOverConfig temp = new TipOverConfig(this.config, "Current");
        this.config = new TipOverConfig(this.config, "North");
        if (this.config.getHeight() >= 1) {
            tipped = this.config.tipped();
            this.announce(null);
        }
        else {
            this.config = temp;
            this.announce(null);
        }
    }

    /**
     * Returns east config od current
     */
    public void getEast() {
        TipOverConfig temp = new TipOverConfig(this.config, "Current");
        this.config = new TipOverConfig(this.config, "East");
        if (this.config.getHeight() >= 1) {
            tipped = this.config.tipped();
            this.announce(null);
        }
        else {
            this.config = temp;
            this.announce(null);
        }
    }

    /**
     * Returns south config of current
     */
    public void getSouth() {
        TipOverConfig temp = new TipOverConfig(this.config, "Current");
        this.config = new TipOverConfig(this.config, "South");
        if (this.config.getHeight() >= 1) {
            tipped = this.config.tipped();
            this.announce(null);
        }
        else {
            this.config = temp;
            this.announce(null);
        }
    }

    /**
     * Returns west config of current
     * */
    public void getWest() {
        TipOverConfig temp = new TipOverConfig(this.config, "Current");
        this.config = new TipOverConfig(this.config, "West");
        if (this.config.getHeight() >= 1) {
            tipped = this.config.tipped();
            this.announce(null);
        }
        else {
            this.config = temp;
            this.announce(null);
        }
    }

    /**
     * Returns the next step
     */
    public void getHint() {
        this.path = Solver.solve(this.config);
        if (this.path.size() > 1) {
            if (this.config.getHeight() > 1) {
                this.config = (TipOverConfig) path.get(1);
                tipped = true;
            } else if (this.config.getHeight() == 1) {
                this.config = (TipOverConfig) path.get(1);
                tipped = false;
            }
        } else {
            solvable = false;
            this.announce(null);
        }
        this.announce(null);
    }

    /**
     * Returns if the grid is solvable
     * @return solvable
     */
    public boolean isSolvable() {
        return solvable;
    }

    /**
     * Add a new observer to the list for this model
     * @param obs an object that wants an
     *            {@link TipOverObserver#update(Object, Object)}
     *            when something changes here
     */
    public void addObserver( TipOverObserver< TipOverModel, Object > obs ) {
        this.observers.add( obs );
    }

    /**
     * Announce to observers the model has changed;
     */
    private void announce( String arg ) {
        for ( var obs : this.observers ) {
            obs.update( this, arg );
        }
    }
}
