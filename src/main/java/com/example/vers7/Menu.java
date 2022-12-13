package com.example.vers7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu extends Application {
    private static Scene scene;


    public static void setRoot(String fxml) throws IOException  {
        scene.setRoot(loadFXML(fxml));

    }

    public static Scene getScene() {
        return  scene;
    }


    @Override
    public void start(Stage stage) throws IOException {

       Parent root = FXMLLoader.load(getClass().getResource("/com/example/vers7/login.fxml"));
      // stage.getIcons().add(new Image("2.png"));
   //    stage.getIcons().add(new Image("resources/images/2.png"));

        scene = new Scene(root);
        stage.setTitle("MAZE");
        stage.setScene(scene);
        stage.show();
    }




    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Menu.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}