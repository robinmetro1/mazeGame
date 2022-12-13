package Controllers;

import com.mysql.jdbc.Driver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javax.swing.*;
import java.awt.*;

import javafx.scene.control.TextField;


import java.sql.*;



public class LoggedIncontroller {
    Connection connection;
    PreparedStatement pst;
     ResultSet rs;



    @FXML
    private Label lbl_close;

    @FXML
    private Label lblErrors;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;
    @FXML
    private Text txtwarning;

    @FXML
    private Button btnSignin;





    public void login(javafx.event.ActionEvent actionEvent)  throws ClassNotFoundException, SQLException {
        String uname = txtUsername.getText();
        String pass = txtPassword.getText();

        if (uname.equals("") && pass.equals(""))
        {
            txtwarning.setText("UserName Or Password Blank ");

        }
        else {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/maze","root","");
            pst = connection.prepareStatement("select * from players where username = ? and  password =? ");
            pst.setString(1,uname);
            pst.setString(2,pass);
            rs = pst.executeQuery();
            if (rs.next()){
                txtwarning.setText("Login Success");

            }
            else {

                txtPassword.setText("");
                txtUsername.setText("");
                txtwarning.setText("Login Failed");


            }


        }
    }
    @FXML
    public void signup(ActionEvent actionEvent) {
    }
}
