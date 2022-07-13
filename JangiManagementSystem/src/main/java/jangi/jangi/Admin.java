package jangi.jangi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Admin extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Admin.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styles.css")).toExternalForm());
        stage.setTitle("Jangi Management System!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}