package application;

import controller.MainController;
import exceptions.AlertWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/View.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Integration Assignment");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest( event -> {
            new AlertWindow("Closing Connections");
            MainController.closeConnection();
        });
        primaryStage.show();
    }

}
