package Controllers;

import com.example.vers7.MainGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuController extends AbstractController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button exitButton;
    @FXML
    private Button playButton;


    @FXML
    void onplayButton(ActionEvent event) throws IOException {
        //  Menu.setRoot("mainGame");
        List<Player> players = new ArrayList<Player>(2);
        Player player1 = new HumanPlayer("1", "#FF0000");
        Player player2 = new HumanPlayer("2", "#0000FF");
        players.add(player1);
        players.add(player2);
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
        setupGame(players);



//      root = FXMLLoader.load(getClass().getResource("mainGame.fxml"));
//       stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
//      stage.setScene(scene);
//     stage.show();
//

//        Stage stage = (Stage) exitButton.getScene().getWindow();
//        loadScreen(stage, "mainGame.fxml");
    }

    private void setupGame(List<Player> players) {
        Stage stage = new Stage();
        Board board = new Board(Settings.getSingleton().getBoardHeight(), Settings.getSingleton().getBoardWidth());
        GameSession gameSession = new GameSession(board);
        MainGame main = new MainGame(stage, gameSession, players);
        stage.show();



       // Menu.setRoot("mainGame");

    }

    @FXML
    private void onExitBtnPress(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Exit Maze");
        alert.setContentText("Are you sure you want to exit the game?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            return;
        } else {
            //empty to handle normal closing
        }

    }
}
