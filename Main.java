import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import databases.Database;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    static String trim(String s) {
        return s.replaceAll("\\*\\*$", "").replaceAll("^\\*\\*", "");
    }

    static String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.setup(new Database("data"));

        primaryStage.setTitle("5e Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
