package Controllers;

import com.example.vers7.MainGame;
import com.example.vers7.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
