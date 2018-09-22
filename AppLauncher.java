package main;

import main.controllers.MasterController;
import main.databases.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AppLauncher extends Application {

    private static BorderPane root = new BorderPane();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/MasterView.fxml"));
        root = loader.load();

        MasterController controller = loader.getController();
        controller.initialize(new Database("datav2"));
        controller.setDatabaseView();

        primaryStage.setTitle("5e Manager");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("main/views/style.css");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static BorderPane getRoot() {
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
