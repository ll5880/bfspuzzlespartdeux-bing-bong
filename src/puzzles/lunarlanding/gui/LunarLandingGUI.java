package puzzles.lunarlanding.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import puzzles.lunarlanding.model.LunarLandingModel;
import puzzles.lunarlanding.model.LunarObserver;
import util.Coordinates;
import util.Grid;
import util.Observer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Lucie Lim
 * November 2021
 */
public class LunarLandingGUI extends Application
        implements LunarObserver< LunarLandingModel, Object > {

    private LunarLandingModel model;
    private GridPane MainGridPane;
    //private HashMap<String, Image> allFigures;
    //private Image image;
    private Stage stage;


    /**
     * initalizes the model and adds an observer
     *
     * @throws Exception
     */
    @Override
    public void init(){
        List <String> params = getParameters().getRaw();
        System.out.println("init: Initialize and connect to model!");
        this.model = new LunarLandingModel(params.get(0));
        model.addObserver(this);
    }

    private Image getFigure(String key) {
        switch (key) {
            case ("E"):
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream("resources/explorer.png")));
            case ("P"):
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream("resources/robot-purple.png")));
            case ("B"):
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream("resources/robot-blue.png")));
            case ("G"):
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream("resources/robot-green.png")));
            case ("O"):
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream("resources/robot-orange.png")));
            case ("Y"):
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream("resources/robot-yellow.png")));
            case ("W"):
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream("resources/robot-white.png")));
            default:
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream("resources/lander.png")));
        }
    }

    private GridPane makeGridPane(){
        MainGridPane = new GridPane();
        for (int r = 0; r < model.getCurrentConfig().getRow(); r++){
            for (int c = 0; c < model.getCurrentConfig().getColumn(); c++){
                //makes initial grid
                Button button = new Button();

                Coordinates coords = new Coordinates(r, c);
                //finds if a figure should exist at that coords
                String figure = model.getCurrentConfig().find(coords);

                if (figure != null) {
                    //assigns an image of a figure to that button
                    button.setGraphic(new ImageView(getFigure(figure)));

                } else if (model.getCurrentConfig().getLunarLanderCoordinates().equals(coords)) {
                    //if coords are equals to the coords of the lunarlander adds it to the button
                    button.setGraphic(new ImageView(getFigure("!")));
                } else {
                    ImageView view = new ImageView();
                    view.setPreserveRatio(true);
                    view.setFitHeight(75);
                    view.setFitWidth(75);
                    button.setGraphic(view);
                }
                MainGridPane.add(button, c, r);
            }
        }
        return MainGridPane;
    }

    @Override
    public void start( Stage stage ) {
        //init();
        stage.setTitle( "Lunar Lander" );
        BorderPane mainBorderPane = new BorderPane();

        Image spaceship = new Image(
                LunarLandingGUI.class.getResourceAsStream(
                        "resources" + File.separator + "lander.png"
                )
        );
        Button temp = new Button();
        temp.setGraphic( new ImageView( spaceship ) );

        //Bottom - arrows
        BorderPane bottom = new BorderPane();
        GridPane controls = new GridPane();

        Button Up = new Button("Up");
        controls.add(Up, 1 , 0);
        Up.setOnAction(event -> model.go("go", "north"));

        Button Down = new Button("Down");
        controls.add(Down, 1, 2);
        Down.setOnAction(event -> model.go("go", "down"));

        Button Right = new Button("Right");
        controls.add(Right, 2,1 );
        Right.setOnAction(event -> model.go("go", "east"));

        Button Left = new Button("Left");
        controls.add(Left, 0, 1);
        Left.setOnAction(event -> model.go("go", "west"));

        bottom.setLeft(controls);

        //Bottom - other controls
        Button load = new Button("Load");
        //load.setOnAction(event -> model.load());

        Button reload = new Button("Reload");
        reload.setOnAction(event -> model.reload());

        Button hint = new Button("Hint");
        hint.setOnAction(event -> model.hint());

        HBox helperButtons = new HBox(5, load, reload, hint);
        helperButtons.setAlignment(Pos.CENTER);
        bottom.setCenter(helperButtons);


        mainBorderPane.setBottom(bottom);
        BorderPane.setAlignment(controls, Pos.BOTTOM_CENTER);

        //Center
        this.MainGridPane = makeGridPane();
        mainBorderPane.setCenter(MainGridPane);
        BorderPane.setAlignment(MainGridPane, Pos.CENTER);

        //Scene scene = new Scene( temp, 640, 480 );
        Scene scene = new Scene(mainBorderPane);
        this.stage = stage;
        stage.setScene( scene );
        stage.show();
    }

    @Override
    public void update( LunarLandingModel lunarLandingModel, Object o) {
//        //refresh the image based on the model
        this.MainGridPane = makeGridPane();
        this.stage.hide();
        this.stage.show();
    }

    /**
     * main entry point launches the JavaFX GUI.
     *
     * @param args
     */
    public static void main( String[] args ) {
        Application.launch( args );
    }
}
