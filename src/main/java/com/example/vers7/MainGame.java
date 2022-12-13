package com.example.vers7;

import Controllers.StatsController;
import com.example.vers7.components.HorizontalWallComponent;
import com.example.vers7.components.PawnComponent;
import com.example.vers7.components.TileComponent;
import com.example.vers7.components.VerticalWallComponent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainGame extends Application implements GameScreen{
    Connection connection;
    PreparedStatement pst;
    ResultSet rs;
    public static final int TILE_SIZE =50;
    private List<PawnComponent> pawnComponentList = new ArrayList<PawnComponent>(GameSession.MAX_PLAYERS);
    private GameSession gameSession;
    private int height;
    private int width;
    private PawnComponent.PawnType[] pawnTypes = {PawnComponent.PawnType.RED, PawnComponent.PawnType.BLUE}; //Could possibly loop through the pawnTypes enums instead of storing in an array
    private int turnIndex;

    private TileComponent[][] tileBoard;
    private HorizontalWallComponent[][] horizontalWalls;
    private VerticalWallComponent[][] verticalWalls;
    private Group tileGroup = new Group();
    private Group pawnGroup = new Group();
    private Group horizontalWallGroup = new Group();
    private Group verticalWallGroup = new Group();
    private Label currentTurnLabel;
    private Label wallsLabel;

    private Label scoreLabel;

    private Scene scene;
    public MainGame(Stage stage, GameSession gameSession, List<HumanPlayer> players) {
        setupModel(gameSession, gameSession.getBoard(), players);
        currentTurnLabel = new Label();
        wallsLabel = new Label();
        scoreLabel = new Label();

        scene = new Scene(createContent());
        //stage.getIcons().add(new Image("/2.png"));
        stage.setTitle("MAZE");
        stage.setScene(scene);
        stage.show();
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize((width * TILE_SIZE) + 85, height * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pawnGroup, horizontalWallGroup, verticalWallGroup,infoPanel());

        //Add tiles to the board
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TileComponent tile = new TileComponent(x, y);
                tileBoard[x][y] = tile;
                tileGroup.getChildren().add(tile);
            }
        }
        //Add vertical walls to the board
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(x == (width - 1)) {
                    continue;
                }
                VerticalWallComponent wall = new VerticalWallComponent(x, y);
                verticalWalls[x][y] = wall;
                verticalWallGroup.getChildren().add(wall);
                final int thisX = x;
                final int thisY = y;
                final int nextWallX = x;
                final int nextWallY = y + 1;
                wall.setOnMouseEntered(e -> {
                    if(nextWallX == (width - 1)) {
                        return;
                    }
                    if(nextWallY > 0 && nextWallY < height) {
                        if(!gameSession.getBoard().containsWall(thisX, thisY, false) && !gameSession.getBoard().containsWall(nextWallX, nextWallY, false)) {
                            wall.setFill(Color.valueOf("a87171"));
                            verticalWalls[nextWallX][nextWallY].setFill(Color.valueOf("a87171"));
                        }
                    }
                });
                wall.setOnMouseExited(e -> {
                    if(nextWallX == (width - 1)) {
                        return;
                    }
                    if(nextWallY > 0 && nextWallY < height) {

                        if(!gameSession.getBoard().containsWall(thisX, thisY, false) && !gameSession.getBoard().containsWall(nextWallX, nextWallY, false)) {
                            wall.setFill(Color.rgb(153, 217, 234, 0.8));
                            verticalWalls[nextWallX][nextWallY].setFill(Color.rgb(153, 217, 234, 0.8));
                        }
                    }
                });

                wall.setOnMousePressed(e -> {
                    if(nextWallX == width || nextWallY == height) { //A vertical wall cannot be placed at the very top of the board
                        return;
                    }

                    if(thisX == width) { //A vertical wall cannot be placed at the very edge of the board
                        System.out.println("You cannot place a wall here.");
                        return;
                    }
                    if(e.isPrimaryButtonDown()) {
                        if(gameSession.getBoard().containsWall(thisX, thisY, false) ||
                                gameSession.getBoard().containsWall(nextWallX, nextWallY, false)) {
                            System.out.println("You cannot place a wall here.");
                            return;
                        }
                        if(gameSession.getPlayer(turnIndex).getWalls() == 0) {
                            System.out.println("You do not have any walls left.");
                            return;
                        }
                        gameSession.getBoard().setWall(thisX, thisY, false, true, gameSession.getPlayer(turnIndex));
                        wall.setFill(Color.valueOf(gameSession.getPlayer(turnIndex).getPawnColour()));
                        System.out.println("1. Wall placed at X: " + thisX + " Y: " + thisY);
                        if(nextWallX < width) {
                            gameSession.getBoard().setWall(nextWallX, nextWallY, false, false, gameSession.getPlayer(turnIndex));
                            verticalWalls[nextWallX][nextWallY].setFill(Color.valueOf(gameSession.getPlayer(turnIndex).getPawnColour()));
                            System.out.println("2. Wall placed at: X" + nextWallX + " " + nextWallY);
                        }
                        gameSession.getPlayer(turnIndex).getStatistics().incrementWallsUsed();
                        gameSession.getPlayer(turnIndex).decrementWalls();
                        gameSession.getPlayer(turnIndex).updateScore(-5);
                        gameSession.getPlayer(turnIndex).getStatistics().updateScore(-5);


                        updateTurn();
                    } else if(e.isSecondaryButtonDown()) {
                        {
                            System.out.println("You can't remove walls in a game ");
                        }
                    }
                });
            }
        }
        //Add horizontal walls to the board
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(y == 0) {
                    continue;
                }
                HorizontalWallComponent wall = new HorizontalWallComponent(x, y);
                horizontalWalls[x][y] = wall;
                final int thisX = x;
                final int thisY = y;
                int nextWallX = x + 1;
                int nextWallY = y;

                wall.setOnMouseEntered(e -> {
                    if(nextWallY == 0) {
                        return;
                    }
                    if(nextWallX > 0 && nextWallX < width) {
                        if(!gameSession.getBoard().containsWall(thisX, thisY, true) && !gameSession.getBoard().containsWall(nextWallX, nextWallY, true)) {
                            wall.setFill(Color.valueOf("a87171"));
                            horizontalWalls[nextWallX][nextWallY].setFill(Color.valueOf("a87171"));
                        }
                    }

                });
                wall.setOnMouseExited(e -> {
                    if(nextWallY == 0) { //A horizontal wall cannot be placed at the very top of the board
                        return;
                    }

                    if(nextWallX > 0 && nextWallX < width) {
                        if(!gameSession.getBoard().containsWall(thisX, thisY, true) && !gameSession.getBoard().containsWall(nextWallX, nextWallY, true)) {
                            wall.setFill(Color.rgb(153, 217, 234, 0.8));
                            horizontalWalls[nextWallX][nextWallY].setFill(Color.rgb(153, 217, 234, 0.8));
                        }
                    }
                });
                wall.setOnMousePressed(e -> {
                    if(nextWallY == 0 || nextWallX > width) { //A horizontal wall cannot be placed at the very top of the board
                        return;
                    }
                    if(thisX == width) { //A horizontal wall cannot be placed at the very edge of the board
                        System.out.println("You cannot place a wall here.");
                        return;
                    }
                    if(e.isPrimaryButtonDown()) {
                        if(gameSession.getBoard().containsWall(thisX, thisY, true) ||
                                gameSession.getBoard().containsWall(nextWallX, nextWallY, true)) {
                            System.out.println("You cannot place a wall here.");
                            return;
                        }
                        if(gameSession.getPlayer(turnIndex).getWalls() == 0) {
                            System.out.println("You do not have any walls left.");
                            return;
                        }
                        gameSession.getBoard().setWall(thisX, thisY, true, true, gameSession.getPlayer(turnIndex));
                        wall.setFill(Color.valueOf(gameSession.getPlayer(turnIndex).getPawnColour()));
                        System.out.println("1. Wall placed at X: " + thisX + " Y: " + thisY);
                        if(nextWallX > 0 && nextWallX < width) {
                            gameSession.getBoard().setWall(nextWallX, nextWallY, true, false, gameSession.getPlayer(turnIndex));
                            horizontalWalls[nextWallX][nextWallY].setFill(Color.valueOf(gameSession.getPlayer(turnIndex).getPawnColour()));
                            System.out.println("2. Wall placed at: X" + nextWallX + " " + nextWallY);
                        }
                        gameSession.getPlayer(turnIndex).getStatistics().incrementWallsUsed();
                        gameSession.getPlayer(turnIndex).decrementWalls();
                        gameSession.getPlayer(turnIndex).updateScore(-5);
                        gameSession.getPlayer(turnIndex).getStatistics().updateScore(-5);


                        updateTurn();
                    } else if(e.isSecondaryButtonDown()) {
                        {
                            System.out.println("You can't  remove walls in a game ");
                        }
                    }
                });
                horizontalWallGroup.getChildren().add(wall);
            }
        }
        pawnGroup.getChildren().addAll(pawnComponentList);
        return root;
    }

    private Node infoPanel() {
        Pane panel = new Pane();

        int offset = Settings.getSingleton().getBoardWidth();
        currentTurnLabel.setText("Player "+gameSession.getPlayer(turnIndex).getUsername() + "'s turn");
        currentTurnLabel.setTextFill(Color.valueOf(gameSession.getPlayer(turnIndex).getPawnColour()));
        currentTurnLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        wallsLabel.setText("Walls left: " + gameSession.getPlayer(turnIndex).getWalls());
        wallsLabel.setTextFill(Color.valueOf(gameSession.getPlayer(turnIndex).getPawnColour()));
        wallsLabel.setTranslateY(50);

        scoreLabel.setText("SCORE: " + gameSession.getPlayer(turnIndex).getScore());
        scoreLabel.setTextFill(Color.valueOf(gameSession.getPlayer(turnIndex).getPawnColour()));
        scoreLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        scoreLabel.setTranslateY(100);



        panel.getChildren().addAll(currentTurnLabel, wallsLabel,scoreLabel);

            panel.setTranslateX(450);

        return panel;
    }



    private void updateTurn() {
        gameSession.getPlayer(turnIndex).getStatistics().incrementTotalMoves();
        if(turnIndex < gameSession.getPlayers().size() - 1) {
            turnIndex++;
            System.out.println("Next turn: " + pawnTypes[turnIndex]);
        } else if(turnIndex == gameSession.getPlayers().size() - 1) {
            turnIndex = 0;
            System.out.println("Next turn: " + pawnTypes[turnIndex]);
        }
        currentTurnLabel.setText(gameSession.getPlayer(turnIndex).getUsername() + "'s turn");
        currentTurnLabel.setTextFill(Color.valueOf(gameSession.getPlayer(turnIndex).getPawnColour()));
        wallsLabel.setText("Walls left: " + gameSession.getPlayer(turnIndex).getWalls());
        wallsLabel.setTextFill(Color.valueOf(gameSession.getPlayer(turnIndex).getPawnColour()));

        scoreLabel.setText("SCORE: " + gameSession.getPlayer(turnIndex).getScore());
        scoreLabel.setTextFill(Color.valueOf(gameSession.getPlayer(turnIndex).getPawnColour()));
        scoreLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

    }


    private void setupModel(GameSession gs, Board board, List<HumanPlayer> players) {
        Settings settings = Settings.getSingleton();
        gameSession = new GameSession(board);
        for(HumanPlayer player : players) {
            gameSession.addPlayer(player);
        }
        height = settings.getBoardHeight();
        width = settings.getBoardWidth();
        tileBoard = new TileComponent[board.getWidth()][board.getHeight()];
        horizontalWalls = new HorizontalWallComponent[board.getWidth()][board.getHeight()];
        verticalWalls = new VerticalWallComponent[board.getWidth()][board.getHeight()];
        turnIndex = 0;
       setupPawns();
    }

    private void setupPawns() {
        int currentIndex = 0;
        int xStartingPositions[] = null;
        int yStartingPositions[] = null;
        //Starting positions for player 1, player 2

        xStartingPositions = new int[]{(width / 2), (width / 2)};
        yStartingPositions = new int[]{(height - 1), (0)};

        for(int i=0; i < 2; i++) {
            //Loop through hardcoded starting positions and pawn types to assign to each player's pawn
            PawnComponent pawn = makePawn(pawnTypes[currentIndex], xStartingPositions[currentIndex], yStartingPositions[currentIndex], gameSession.getPlayer(i).getName(), gameSession.getPlayer(i).getPawnColour());
            pawnComponentList.add(pawn);
            currentIndex++;

        }
    }

    private PawnComponent makePawn(PawnComponent.PawnType type, int x, int y, String name, String colour) {
        PawnComponent pawn = new PawnComponent(type, x, y, name, colour);

        pawn.setOnMouseReleased(e -> {
            int newX = toBoard(pawn.getLayoutX());
            int newY = toBoard(pawn.getLayoutY());
            Tile currentTile = new Tile(toBoard(pawn.getOldX()), toBoard(pawn.getOldY()));
            Tile nextTile = new Tile(newX, newY);
            if(gameSession.isValidMove(currentTile, nextTile) && isCurrentTurn(type)) {
                System.out.println(type + " x:" + newX + " y:" + newY);
                pawn.move(newX, newY);
                gameSession.getBoard().getTile(currentTile.getX(), currentTile.getY()).setContainsPawn(false);
                gameSession.getBoard().getTile(nextTile.getX(), nextTile.getY()).setContainsPawn(true);
                gameSession.getPlayer(turnIndex).updateScore(10);
                gameSession.getPlayer(turnIndex).getStatistics().updateScore(10);


                //Check if the pawn is in a winning position

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost/maze", "root", "");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


                switch(type) {
                    case RED:

                        if(newY == 0) {
                            gameSession.setWinner(gameSession.getPlayer(turnIndex));
                            String uname = gameSession.getPlayer(turnIndex).getUsername();
                            gameSession.getPlayer(turnIndex).getStatistics().updateScore(100);
                            try {
                                pst = connection.prepareStatement("select score from players where username = ? ");
                                pst.setString(1,uname );
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            try {
                                rs = pst.executeQuery();
                                if (rs.next()) {
                                    int lastScore= Integer.parseInt(rs.getString("score"));
                                    System.out.println(lastScore);
                                    int newScore = gameSession.getPlayer(turnIndex).getStatistics().getScore();
                                    if(lastScore <newScore ){

                                        try {
                                            pst = connection.prepareStatement("update players set score = ? where username = ? ");
                                            pst.setString(1,String.valueOf(newScore) );
                                            pst.setString(2,uname );
                                            pst.executeUpdate();

                                        } catch (SQLException ex) {
                                            throw new RuntimeException(ex);
                                        }



                                    }
                                }

                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }






                            endGame(gameSession);
                        }
                        break;
                    case BLUE:
                        if(newY == 8) {
                            gameSession.setWinner(gameSession.getPlayer(turnIndex));
                            String uname = gameSession.getPlayer(turnIndex).getUsername();
                            gameSession.getPlayer(turnIndex).getStatistics().updateScore(100);
                            try {
                                pst = connection.prepareStatement("select score from players where username = ? ");
                                pst.setString(1,uname );
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            try {
                                rs = pst.executeQuery();
                                if (rs.next()) {
                                    int lastScore= Integer.parseInt(rs.getString("score"));
                                    System.out.println(lastScore);
                                    int newScore = gameSession.getPlayer(turnIndex).getStatistics().getScore();
                                    if(lastScore <newScore ){

                                        try {
                                            pst = connection.prepareStatement("update players set score = ? where username = ? ");
                                            pst.setString(1,String.valueOf(newScore) );
                                            pst.setString(2,uname );
                                            pst.executeUpdate();

                                        } catch (SQLException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                }
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            endGame(gameSession);
                        }
                        break;
                }
                //update turn
                updateTurn();
            } else {
                pawn.reverseMove();
            }
        });
        return pawn;

    }

    private void endGame(GameSession gs) {
        try {
            Stage stage = (Stage) tileGroup.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("stats.fxml"));
            Scene scene = new Scene((Parent)loader.load());
            StatsController controller = loader.<StatsController>getController();
            controller.setGameSession(gs);
            stage.setTitle("Maze");
         //   stage.getIcons().add(new Image("icons/unnamed.png"));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Determines whether it is the current {@link Player player's} turn.
     * @param type the type of pawn
     * @return whether it is the  current turn
     */
    private boolean isCurrentTurn(PawnComponent.PawnType type) {
        if(pawnTypes[turnIndex] == type) {
            return true;
        }
        System.out.println("It is not your turn!");
        return false;
    }


    private int toBoard(double pixel) {
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    }
    
    @Override
    public void start(Stage stage)  {

    }
}

