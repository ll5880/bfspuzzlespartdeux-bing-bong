package puzzles.tipover.gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.tipover.model.TipOverObserver;
import puzzles.tipover.model.TipOverConfig;
import puzzles.tipover.model.TipOverModel;
import java.io.FileNotFoundException;

/**
 * Creates a user interface, can play tipover using buttons to control the tipper
 * @author Darian Cheung, Team bingbong
 * November 2021
 */
public class TipOverGUI extends Application
        implements TipOverObserver<TipOverModel, Object> {

    // model to be displayed
    private static TipOverModel model;
    // grid
    private GridPane gridPane;
    // text above the grid
    private Label top;

    // all images for the buttons
    private final Image zero = new Image(getClass().getResourceAsStream("resources/0.png"), 50, 50, false, false);
    private final Image one = new Image(getClass().getResourceAsStream("resources/1.png"), 50, 50, false, false);
    private final Image two = new Image(getClass().getResourceAsStream("resources/2.png"), 50, 50, false, false);
    private final Image three = new Image(getClass().getResourceAsStream("resources/3.png"), 50, 50, false, false);
    private final Image four = new Image(getClass().getResourceAsStream("resources/4.png"), 50, 50, false, false);
    private final Image five = new Image(getClass().getResourceAsStream("resources/5.png"), 50, 50, false, false);
    private final Image six = new Image(getClass().getResourceAsStream("resources/6.png"), 50, 50, false, false);
    private final Image seven = new Image(getClass().getResourceAsStream("resources/7.png"), 50, 50, false, false);
    private final Image eight = new Image(getClass().getResourceAsStream("resources/8.png"), 50, 50, false, false);
    private final Image nine = new Image(getClass().getResourceAsStream("resources/9.png"), 50, 50, false, false);
    private final Image tipper = new Image(getClass().getResourceAsStream("resources/tipper.png"), 50, 50, false, false);
    private final Image up = new Image(getClass().getResourceAsStream("resources/uparrow.png"), 50, 50, false, false);
    private final Image down = new Image(getClass().getResourceAsStream("resources/downarrow.png"), 50, 50, false, false);
    private final Image right = new Image(getClass().getResourceAsStream("resources/rightarrow.png"), 50, 50, false, false);
    private final Image left = new Image(getClass().getResourceAsStream("resources/leftarrow.png"), 50, 50, false, false);
    private final Image star = new Image(getClass().getResourceAsStream("resources/star.png"), 50, 50, false, false);

    /**
     * Class that takes in the value in each grid and sets appropriate image
     */
    private class NumberedButtons extends Button{
        private Image image;

        public NumberedButtons(int number) {
            switch (number) {
                case -2 -> image = star;
                case -1 -> image = tipper;
                case 0 -> image = zero;
                case 1 -> image = one;
                case 2 -> image = two;
                case 3 -> image = three;
                case 4 -> image = four;
                case 5 -> image = five;
                case 6 -> image = six;
                case 7 -> image = seven;
                case 8 -> image = eight;
                case 9 -> image = nine;
            }
        }
    }

    /**
     * Start function, creates the window with grid, text, and buttons
     * @param stage
     */
    @Override
    public void start( Stage stage ) {

        // border pane to insert grid pane and text labels
        BorderPane borderPane = new BorderPane();
        top = new Label("Loaded Game, Standing on a " + model.config.grid[model.config.coords.row()][model.config.coords.col()]);
        borderPane.setTop(top);

        borderPane.setPrefSize(800, 400);

        // sets the center
        gridPane = makeGrid();
        borderPane.setCenter(gridPane);

        // sets the right side
        GridPane side = makeSide();
        borderPane.setRight(side);

        // creates button to load new files
        final FileChooser fileChooser = new FileChooser();
        Button load = new Button("Load");
        load.setOnAction(
                e -> {
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        openFile(file);
                    }
                });
        side.add(load, 2, 3);

        // sets scene
        Scene scene = new Scene(borderPane);
        stage.setTitle("Tip Over");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates a gridPane based off of the model
     * @return gridPae
     */
    public GridPane makeGrid() {
        GridPane gridPane = new GridPane();
        for (int row = 0; row < TipOverConfig.width; row++) {
            for (int col = 0; col < TipOverConfig.length; col++) {
                NumberedButtons button;
                // creates buttons with the number at the current coordinates, if it's the goal, it's a star
                // if it's the current, it's the tipper
                if (model.config.coords.row() == row && model.config.coords.col() == col) {
                    button = new NumberedButtons(-1);
                    button.setGraphic(new ImageView(button.image));
                }
                else if (TipOverConfig.goal.row() == row && TipOverConfig.goal.col() == col) {
                    button = new NumberedButtons(-2);
                    button.setGraphic(new ImageView(button.image));
                }
                else {
                    button = new NumberedButtons(Integer.parseInt(model.config.grid[row][col]));
                    button.setGraphic(new ImageView(button.image));
                }
                gridPane.add(button, col, row);
            }
        }
        return gridPane;
    }

    /**
     * creates the side buttons, holding the arrow keys and reset and hint buttons
     * @return grid pane
     */
    public GridPane makeSide() {
        GridPane gridPane = new GridPane();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 3; col++) {
                Button button;
                if (row == 0 && col == 1) {
                    button = new Button();
                    button.setGraphic(new ImageView(up));
                    button.setOnAction(event -> model.getNorth());
                    gridPane.add(button, col, row);
                }
                if (row == 1 && col == 0) {
                    button = new Button();
                    button.setGraphic(new ImageView(left));
                    button.setOnAction(event -> model.getWest());
                    gridPane.add(button, col, row);
                }
                if (row == 1 && col == 2) {
                    button = new Button();
                    button.setGraphic(new ImageView(right));
                    button.setOnAction(event -> model.getEast());
                    gridPane.add(button, col, row);
                }
                if (row == 2 && col == 1) {
                    button = new Button();
                    button.setGraphic(new ImageView(down));
                    button.setOnAction(event -> model.getSouth());
                    gridPane.add(button, col, row);
                }
                if (row == 3 && col == 0) {
                    button = new Button("Reset");
                    button.setOnAction(event -> {
                        model.reload();
                        top.setText("Reloaded");
                    });
                    gridPane.add(button, col, row);
                }
                if (row == 3 && col == 1) {
                    button = new Button("Hint");
                    button.setOnAction(event -> model.getHint());
                    gridPane.add(button, col, row);
                }
            }
        }
        return gridPane;
    }

    /**
     * Updates the screen after the buttons will update the model
     * @param tipOverModel
     * @param o
     */
    @Override
    public void update( TipOverModel tipOverModel, Object o ) {
        for (int row = 0; row < TipOverConfig.width; row++) {
            for (int col = 0; col < TipOverConfig.length; col++) {
                NumberedButtons button;
                if (model.config.coords.row() == row && model.config.coords.col() == col) {
                    button = new NumberedButtons(-1);
                    button.setGraphic(new ImageView(button.image));
                }
                else if (TipOverConfig.goal.row() == row && TipOverConfig.goal.col() == col) {
                    button = new NumberedButtons(-2);
                    button.setGraphic(new ImageView(button.image));
                }
                else {
                    button = new NumberedButtons(Integer.parseInt(model.config.grid[row][col]));
                    button.setGraphic(new ImageView(button.image));
                }
                gridPane.add(button, col, row);
            }
        }
        // status texts, like victory text and such
        if (model.config.coords.equals(TipOverConfig.goal)) {
            top.setText("YOU WON");
        }
        else {
            top.setText("Standing on a " + model.config.grid[model.config.coords.row()][model.config.coords.col()]);
        }
        if (model.isTipped()) {
            top.setText("Tipped Over a Tower");
        }
        if (!model.isSolvable()) {
            top.setText("Unsolvable");
        }
    }

    /**
     * main entry point launches the JavaFX GUI.
     *
     * @param args not used
     */
    public static void main(String[] args) throws FileNotFoundException {
        model = new TipOverModel(args[0]);
        Application.launch(args);
    }

    @Override
    public void init(){
        model.addObserver(this);
        System.out.println("init: Initialize and connect to model!");
    }

    /**
     * opens the file and loads it, updating the model/grid
     * @param file
     */
    private void openFile(File file) {
        try {
            this.gridPane.getChildren().clear();
            model.loadNew("data/tipover/" + file.getName());
        } catch (IOException ex) {
            Logger.getLogger(
                    TipOverGUI.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }
}
