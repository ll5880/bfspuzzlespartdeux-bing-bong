package puzzles.lunarlanding.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.lunarlanding.model.LunarLandingModel;
import puzzles.lunarlanding.model.LunarObserver;
import util.Coordinates;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lucie Lim
 * November 2021
 */
public class LunarLandingGUI extends Application
        implements LunarObserver< LunarLandingModel, Object > {

    private LunarLandingModel model;
    private GridPane mainGridPane;
    private ArrayList <FigureButton> figureButtons = new ArrayList<>();
    private Stage stage;
    private Label text;
    private boolean reachedSolution = false;


    /**
     * initalizes the model and adds an observer
     *
     * @throws Exception
     */
    @Override
    public void init(){
        List <String> params = getParameters().getRaw();
        this.model = new LunarLandingModel(params.get(0));
        model.addObserver(this);
    }

    private class FigureButton extends Button {

        private Coordinates coords;
        private String name;

        public FigureButton(Coordinates coords, String name){
            this.coords = coords;
            this.name = name;
        }

        public Coordinates getButtonCoords() {
            return coords;
        }

        public String getName () {
            return name;
        }
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
        mainGridPane = new GridPane();
        for (int r = 0; r < model.getCurrentConfig().getRow(); r++){
            for (int c = 0; c < model.getCurrentConfig().getColumn(); c++){
                //makes initial grid
                Coordinates coords = new Coordinates(r, c);

                //finds if a figure should exist at that coords
                String figure = model.getCurrentConfig().find(coords);

                if (figure != null) {
                    //assigns an image of a figure to that button
                    FigureButton button = new FigureButton(coords, figure);
                    button.setGraphic(new ImageView(getFigure(figure)));
                    //gets a hashmap of all the buttons with figures on it
                    figureButtons.add(button);
                    int position = (r * model.getCurrentConfig().getRow()) + c;
                    button.setOnAction(event -> {
                        model.choose(position);
                    });
                    mainGridPane.add(button, c, r);

                } else if (model.getCurrentConfig().getLunarLanderCoordinates().equals(coords)) {
                    //if coords are equals to the coords of the lunarlander adds it to the button
                    FigureButton button = new FigureButton(coords, "!");
                    button.setGraphic(new ImageView(getFigure("!")));
                    mainGridPane.add(button, c, r);
                } else {
                    FigureButton button = new FigureButton(coords, figure);
                    ImageView view = new ImageView();
                    view.setPreserveRatio(true);
                    view.setFitHeight(75);
                    view.setFitWidth(75);
                    button.setGraphic(view);
                    mainGridPane.add(button, c, r);
                }
            }
        }
        return mainGridPane;
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

        Label top = new Label("File loaded");
        text = top;
        mainBorderPane.setTop(top);
        BorderPane.setAlignment(top, Pos.CENTER);

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
        Down.setOnAction(event -> model.go("go", "south"));

        Button Right = new Button("Right");
        controls.add(Right, 2,1 );
        Right.setOnAction(event -> model.go("go", "east"));

        Button Left = new Button("Left");
        controls.add(Left, 0, 1);
        Left.setOnAction(event -> model.go("go", "west"));

        bottom.setLeft(controls);

        //Bottom - other controls
        Button load = new Button("Load");
        final FileChooser fileChooser = new FileChooser();
        load.setOnAction(
                event -> {
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        openFile(file);
                    }
                });

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
        this.mainGridPane = makeGridPane();
        mainBorderPane.setCenter(mainGridPane);
        BorderPane.setAlignment(mainGridPane, Pos.CENTER);

        Scene scene = new Scene(mainBorderPane);
        this.stage = stage;
        stage.setScene( scene );
        stage.show();
    }

    @Override
    public void update( LunarLandingModel lunarLandingModel, Object o) {

        for (int r = 0; r < model.getCurrentConfig().getRow(); r++) {
            for (int c = 0; c < model.getCurrentConfig().getColumn(); c++) {
                //makes initial grid
                Coordinates coords = new Coordinates(r, c);

                //finds if a figure should exist at that coords
                String figure = model.getCurrentConfig().find(coords);

                if (figure != null) {
                    //assigns an image of a figure to that button
                    FigureButton button = new FigureButton(coords, figure);
                    button.setGraphic(new ImageView(getFigure(figure)));
                    //gets a hashmap of all the buttons with figures on it
                    figureButtons.add(button);
                    int position = (r * model.getCurrentConfig().getRow()) + c;
                    button.setOnAction(event -> {
                        model.choose(position);
                    });
                    mainGridPane.add(button, c, r);
                } else if (model.getCurrentConfig().getLunarLanderCoordinates().equals(coords)) {
                    //if coords are equals to the coords of the lunarlander adds it to the button
                    FigureButton button = new FigureButton(coords, "!");
                    button.setGraphic(new ImageView(getFigure("!")));
                    mainGridPane.add(button, c, r);
                } else {
                    FigureButton button = new FigureButton(coords, figure);
                    ImageView view = new ImageView();
                    view.setPreserveRatio(true);
                    view.setFitHeight(75);
                    view.setFitWidth(75);
                    button.setGraphic(view);
                    mainGridPane.add(button, c, r);
                }
            }
        }

        // if a move is illegal then it will show illegal move

        if (o != null){
            text.setText(o.toString());
        }

    }

    /**
     * opens the file and loads it, updating the model/grid
     * @param file
     */
    private void openFile(File file) {
        this.mainGridPane.getChildren().clear();
        model.load("data/lunarlander/" + file.getName());
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
