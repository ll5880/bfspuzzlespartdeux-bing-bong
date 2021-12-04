package puzzles.tipover.model;

import solver.Configuration;
import solver.Solver;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

/**
 * DESCRIPTION
 * @author YOUR NAME HERE
 * November 2021
 */
public class TipOverModel{

    private String filename;
    private boolean tipped;
    private TipOverConfig config;
    private List<Configuration> path;
    private List< Observer< TipOverModel, Object > > observers;
    private boolean solvable = true;

    public TipOverModel(String filename) throws FileNotFoundException {
        this.observers = new LinkedList<>();
        this.filename = filename;
        this.config = new TipOverConfig(this.filename);
        this.path = Solver.solve(this.config);
    }

    public boolean isTipped() {
        return tipped;
    }

    public Configuration getConfig() {
        return this.config;
    }

    public void show() {
        System.out.println(this.config);
    }

    public void reload() throws FileNotFoundException {
        this.config = new TipOverConfig(this.filename);
        this.show();
    }

    public void loadNew(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.config = new TipOverConfig(this.filename);
        this.show();
    }

    public Configuration getNorth() {
        TipOverConfig temp = new TipOverConfig(this.config, "Current");
        this.config = new TipOverConfig(this.config, "North");
        if (this.config.getHeight() >= 1) {
            this.show();
            tipped = this.config.tipped();
            this.announce(null);
            return this.config;
        }
        else {
            this.config = temp;
            this.show();
            this.announce(null);
            return this.config;
        }
    }

    public Configuration getEast() {
        TipOverConfig temp = new TipOverConfig(this.config, "Current");
        this.config = new TipOverConfig(this.config, "East");
        if (this.config.getHeight() >= 1) {
            this.show();
            tipped = this.config.tipped();
            this.announce(null);
            return this.config;
        }
        else {
            this.config = temp;
            this.show();
            this.announce(null);
            return this.config;
        }
    }
    public Configuration getSouth() {
        TipOverConfig temp = new TipOverConfig(this.config, "Current");
        this.config = new TipOverConfig(this.config, "South");
        if (this.config.getHeight() >= 1) {
            this.show();
            tipped = this.config.tipped();
            this.announce(null);
            return this.config;
        }
        else {
            this.config = temp;
            this.show();
            this.announce(null);
            return this.config;
        }
    }
    public Configuration getWest() {
        TipOverConfig temp = new TipOverConfig(this.config, "Current");
        this.config = new TipOverConfig(this.config, "West");
        if (this.config.getHeight() >= 1) {
            this.show();
            tipped = this.config.tipped();
            this.announce(null);
            return this.config;
        }
        else {
            this.config = temp;
            this.show();
            this.announce(null);
            return this.config;
        }
    }

    public Configuration getHint() {
        this.path = Solver.solve(this.config);
        if (this.path.size() > 1) {
            this.config = (TipOverConfig) path.get(1);
            if (this.config.getHeight() > 1) {
                this.show();
                this.announce(null);
                tipped = true;
            } else if (this.config.getHeight() == 1) {
                this.show();
                this.announce(null);
                tipped = false;
            }
        } else {
            System.out.println("Unsolvable Board");
            solvable = false;
            this.announce(null);
        }
        return this.config;
    }

    public boolean isSolvable() {
        return solvable;
    }

    /**
     * Add a new observer to the list for this model
     * @param obs an object that wants an
     *            {@link Observer#update(Object, Object)}
     *            when something changes here
     */
    public void addObserver( Observer< TipOverModel, Object > obs ) {
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
