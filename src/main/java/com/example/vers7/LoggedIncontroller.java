package com.example.vers7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

import java.awt.*;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;


import java.io.IOException;
import java.sql.*;



public class LoggedIncontroller {
    Connection connection;
    PreparedStatement pst;
     ResultSet rs;


    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;
    @FXML
    private Text txtwarning;


    @FXML
    private Text txtSignin;


    public static String getUname1() {
        return uname1;
    }

    public static void setUname1(String uname1) {
        LoggedIncontroller.uname1 = uname1;
    }

    public static String getPass1() {
        return pass1;
    }

    public static void setPass1(String pass1) {
        LoggedIncontroller.pass1 = pass1;
    }

    public static String getUname2() {
        return uname2;
    }

    public static void setUname2(String uname2) {
        LoggedIncontroller.uname2 = uname2;
    }

    public static String getPass2() {
        return pass2;
    }

    public static void setPass2(String pass2) {
        LoggedIncontroller.pass2 = pass2;
    }

    private static Scene scene;
    static String uname1;
    static String pass1;
    static String uname2;
    static String pass2;
     int nbPlayers=0;
    @FXML
    private Button login1btn;

    @FXML
    private Button login2btn;




    public void login1(javafx.event.ActionEvent actionEvent)  throws ClassNotFoundException, SQLException {

            String uname = txtUsername.getText();
            String pass = txtPassword.getText();

            if (uname.equals("") && pass.equals("")) {
                txtwarning.setText("UserName Or Password Blank ");

            } else {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost/maze", "root", "");
                pst = connection.prepareStatement("select * from players where username = ? and  password =? ");
                pst.setString(1, uname);
                pst.setString(2, pass);
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtwarning.setText("Login1 Success");
                        setUname1(uname);
                        System.out.println(uname1);
                        setPass1(pass);
                        nbPlayers++;
                    login1btn.setDisable(true);
                } else {
                    txtPassword.setText("");
                    txtUsername.setText("");
                    txtwarning.setText("Login1 Failed");
                }


            }
        System.out.println(nbPlayers);
    }



    public void login2(ActionEvent actionEvent) throws ClassNotFoundException, SQLException, IOException {

        String uname = txtUsername.getText();
        String pass = txtPassword.getText();

        if (uname.equals("") && pass.equals("")) {
            txtwarning.setText("UserName Or Password Blank ");

        } else {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/maze", "root", "");
            pst = connection.prepareStatement("select * from players where username = ? and  password =? ");
            pst.setString(1, uname);
            pst.setString(2, pass);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtwarning.setText("Login2 Success");
                login2btn.setDisable(true);

                setUname2(uname);
                System.out.println(uname2);
                setPass2(pass);
                nbPlayers++;
                txtPassword.setText("");
                txtUsername.setText("");
            } else {

                txtPassword.setText("");
                txtUsername.setText("");
                txtwarning.setText("Login2 Failed");

            }
        }

        System.out.println(nbPlayers);
        if(nbPlayers==2){
            Window window =   ((Node)(actionEvent.getSource())).getScene().getWindow();
            if (window instanceof Stage){
                ((Stage) window).close();
            };
            Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
            Stage stage = new Stage();
            scene = new Scene(root);
            stage.setTitle("MAZE");
            stage.setScene(scene);
            stage.show();
        }
    }


    @FXML
    public void signin(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        txtSignin.setText("Sign In");


        String uname = txtUsername.getText();
        String pass = txtPassword.getText();

        if (uname.equals("") && pass.equals(""))
        {
            txtwarning.setText("Enter your username AND password ");

        }
        else {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/maze","root","");
            pst = connection.prepareStatement("INSERT INTO players ( password, username,score) VALUES (?,?,?) ");

            pst.setString(1, pass);
            pst.setString(2, uname);
            pst.setString(3, String.valueOf(0));

            pst.executeUpdate();

            txtwarning.setText("Added Successfully");
            txtPassword.setText("");
            txtUsername.setText("");



        }



    }
}

