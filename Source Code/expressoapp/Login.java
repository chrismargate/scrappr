/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressoapp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;
import java.sql.Statement.*;
import javafx.scene.control.PasswordField;
import javax.swing.JOptionPane;

/**
 *
 * @author my pc
 */
public class Login extends Application implements EventHandler<ActionEvent>{
    
    static int activeCustomerID;
    static boolean loggedIn = false;
    static boolean accountExistsVar = false;
    private static final String databasePath = "jdbc:mysql://localhost:3306/scrappr";
    private static final String databaseUsername = "root";
    private static final String databasePassword = "NewPassword";
    Connection connection;
    Statement statement;
    
    
//    
    Button loginaccountbutton;
    Button cancelaccountbutton;
    Button createaccountbutton;
    TextField loginusernamefield;
    PasswordField loginpwfield;
    
    static Register register = new Register();
    
    
    static Stage mainwindow;
    
    public void sampleEventHandler(ActionEvent event) throws SQLException,ClassNotFoundException{
        
    }
    
    @Override
    public void start(Stage primaryStage)throws SQLException,ClassNotFoundException {
//        Stage window = new Stage();

        mainwindow = primaryStage;
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #232F3E;");
        
        connection =  DriverManager.getConnection(databasePath,databaseUsername,databasePassword);
        statement = connection.createStatement();
        
        //Labels for Login Window
        Label loginyouraccountlbl = new Label("Login Your Account");
        loginyouraccountlbl.setFont(Font.font("Bodoni MT Black", 25));
        loginyouraccountlbl.setTextFill(Color.WHITE);
        loginyouraccountlbl.setLayoutX(265.0);
        loginyouraccountlbl.setLayoutY(150.0);
        Label loginusernamelbl = new Label("Username: ");
        loginusernamelbl.setFont(Font.font("Arial Black", 15));
        loginusernamelbl.setTextFill(Color.WHITE);
        loginusernamelbl.setLayoutX(190.0);
        loginusernamelbl.setLayoutY(200.0);
        Label loginpwlbl = new Label("Password: ");
        loginpwlbl.setFont(Font.font("Arial Black", 15));
        loginpwlbl.setTextFill(Color.WHITE);
        loginpwlbl.setLayoutX(190.0);
        loginpwlbl.setLayoutY(250.0);
        Label createaccountlbl = new Label("Not yet registered? Register here: ");
        createaccountlbl.setFont(Font.font("Arial Black", 15));
        createaccountlbl.setTextFill(Color.WHITE);
        createaccountlbl.setLayoutX(160.0);
        createaccountlbl.setLayoutY(300.0);
        //TextFields fpr Login Window
        loginusernamefield = new TextField();
        
        
        loginusernamefield.setLayoutX(310.0);
        loginusernamefield.setLayoutY(200.0);
        loginusernamefield.setPrefHeight(25.0);
        loginusernamefield.setPrefWidth(233.0);
        
        loginpwfield = new PasswordField();
        loginpwfield.setLayoutX(310.0);
        loginpwfield.setLayoutY(250.0);
        loginpwfield.setPrefHeight(25.0);
        loginpwfield.setPrefWidth(233.0);
        //Button
        
        createaccountbutton = new Button("Register Now!");
        createaccountbutton.setLayoutX(470.0);
        createaccountbutton.setLayoutY(295.0);
        createaccountbutton.setPrefHeight(34.0);
        createaccountbutton.setPrefWidth(160.0);
        createaccountbutton.setStyle("-fx-background-color: blue;");
        createaccountbutton.setTextFill(Color.WHITE);
        createaccountbutton.setFont(Font.font("Arial Black", 16));
        createaccountbutton.setOnAction(this);
        
        loginaccountbutton = new Button("Login");
        loginaccountbutton.setLayoutX(330.0);
        loginaccountbutton.setLayoutY(350.0);
        loginaccountbutton.setPrefHeight(34.0);
        loginaccountbutton.setPrefWidth(120.0);
        loginaccountbutton.setStyle("-fx-background-color: green;");
        loginaccountbutton.setTextFill(Color.WHITE);
        loginaccountbutton.setFont(Font.font("Arial Black", 16));
        loginaccountbutton.setOnAction(this);
        
        cancelaccountbutton = new Button("Cancel");
        cancelaccountbutton.setLayoutX(330.0);
        cancelaccountbutton.setLayoutY(400.0);
        cancelaccountbutton.setPrefHeight(34.0);
        cancelaccountbutton.setPrefWidth(120.0);
        cancelaccountbutton.setStyle("-fx-background-color: red;");
        cancelaccountbutton.setTextFill(Color.WHITE);
        cancelaccountbutton.setFont(Font.font("Arial Black", 16));
        cancelaccountbutton.setOnAction(this);
         
        root.getChildren().addAll(loginyouraccountlbl,createaccountbutton,createaccountlbl,loginusernamelbl,loginpwlbl,loginusernamefield,loginpwfield,loginaccountbutton,cancelaccountbutton);
        Scene scene = new Scene(root, 800, 500);
        
        mainwindow.setTitle("Login");
        mainwindow.setScene(scene);
        mainwindow.setOnCloseRequest(e -> {
            if(Register.isOpen == true){
                Register.mainwindow.close();
            }
        });
        mainwindow.show();
    }
    
    @Override
    public void handle(ActionEvent event){
        if(event.getSource() == loginaccountbutton){
            System.out.println("login button");
            String enteredUsername = loginusernamefield.getText();
            String enteredPassword = loginpwfield.getText();
            try{
                if(accountExists(enteredUsername,enteredPassword)){
                    accountExistsVar = true;
                }
                System.out.println(accountExistsVar);
            }catch(SQLException se){
                System.out.println("SQLException");
            }catch(ClassNotFoundException ce){
                
            }catch(Exception e){
                
            }
        }
        if(event.getSource()==cancelaccountbutton){
            System.out.println("in cancel button");
            if(Register.isOpen == true){
                Register.mainwindow.close();
            }
            mainwindow.close();
        }
        
        if(event.getSource()==createaccountbutton){
            if(Register.isOpen == false){
                register.start();
            }
            
//            mainwindow.close();
        }
    }
    
    public boolean accountExists(String enteredUsername,String enteredPassword)throws Exception,SQLException,ClassNotFoundException{
        
 
//        
        final String query = "SELECT * FROM customers;";
        ResultSet resultSet = statement.executeQuery(query);
        boolean isFound = false;
        while(resultSet.next()){
            
            System.out.println(resultSet.getString("customer_id"));
            System.out.println(resultSet.getString("customer_email"));
            System.out.println(resultSet.getString("customer_pass"));
            System.out.println();
            if(enteredUsername.equals(resultSet.getString("customer_email"))|| enteredUsername.equals(resultSet.getString("username"))){
                isFound = true;
                if(enteredPassword.equals(resultSet.getString("customer_pass"))){
                    System.out.println("Logged in");
                    activeCustomerID = Integer.parseInt(resultSet.getString("customer_id"));
                    loggedIn = true;
                    mainwindow.close();
                    new Scrappr().start();
                    if(Register.isOpen == true){
                         Register.mainwindow.close();
                    }
                    wait();
                    mainwindow.show();
                    return true;
                }else{
                    JOptionPane.showMessageDialog(null,"Password you have entered is incorrect!", "Incorrect Password", JOptionPane.WARNING_MESSAGE);
                    return false;
                }//if else password
            }
         
            }
        
        if(isFound==false){
                        JOptionPane.showMessageDialog(null,"Account does not exist!", "Login Error", JOptionPane.WARNING_MESSAGE);
                        return false;
                }
        return false;
    }
    
    public static void main(String[]args){
        launch(args);
    }

}
