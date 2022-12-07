package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AbstractController {


    protected void loadScreen(Stage primaryStage, String layoutFile){
        try {

            Parent root = FXMLLoader.load((getClass().getResource(layoutFile)));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Maze");
            //primaryStage.getIcons().add(new Image("resources/icons/favicon.png"));
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
