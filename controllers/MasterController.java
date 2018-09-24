package main.controllers;

import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import main.AppLauncher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import main.model.MasterDataModel;

import java.io.IOException;

public class MasterController {

    private static ToolBar toolBar = new ToolBar();
    private TabPane databaseView;
    private GridPane calendarView;

    @FXML private VBox topBox;

    public MasterController() { } // Constructor must be empty.

    /**
     * Sets up the different views, controllers, and initializes them.
     *
     * @param model The model to be used in the program.
     * @throws IOException .
     */
    public void initialize(MasterDataModel model) throws IOException {
        topBox.getChildren().add(toolBar);

        FXMLLoader databaseLoader = new FXMLLoader(getClass().getResource("../views/DatabaseView.fxml"));
        databaseView = databaseLoader.load();
        DatabaseController databaseController = databaseLoader.getController();
        databaseController.initialize(model);

        FXMLLoader calendarLoader = new FXMLLoader(getClass().getResource("../views/CalendarView.fxml"));
        calendarView = calendarLoader.load();
    }

    /**
     * Used for other controllers to get the toolbar and adjust the Nodes as needed, as each view should change the
     * toolbar accordingly.
     *
     * @return the global toolbar
     */
    static ToolBar getMasterToolBar() {
        return toolBar;
    }

    /**
     * Sets the center of the bordepane to the Vbox which contains the database gui
     *
     * @throws IOException
     */
    @FXML
    public void setDatabaseView() throws IOException {
        AppLauncher.getRoot().setCenter(databaseView);
    }

    @FXML
    public void setCalendarView() throws IOException {
        AppLauncher.getRoot().setCenter(calendarView);
    }
}
