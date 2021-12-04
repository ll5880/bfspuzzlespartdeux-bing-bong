package puzzles.tipover.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import puzzles.tipover.model.Observer;
import puzzles.tipover.model.TipOverModel;

import java.io.File;

/**
 * DESCRIPTION
 * @author YOUR NAME HERE
 * November 2021
 */
public class TipOverGUI extends Application
        implements Observer<TipOverModel, Object> {

    private TipOverModel model;


    @Override
    public void start( Stage stage ) {
        stage.setTitle( "Tip Over" );
        Image spaceship = new Image(
                TipOverGUI.class.getResourceAsStream(
                        "resources" + File.separator + "tipper.png"
                )
        );
        Button temp = new Button();
        temp.setGraphic( new ImageView( spaceship ) );
        Scene scene = new Scene( temp, 640, 480 );
        stage.setScene( scene );
        stage.show();
    }

    @Override
    public void update( TipOverModel tipOverModel, Object o ) {
        System.out.println( "My model has changed! (DELETE THIS LINE)");
    }

    /**
     * main entry point launches the JavaFX GUI.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        this.model.addObserver(this);
        System.out.println("init: Initialize and connect to model!");
    }
}
