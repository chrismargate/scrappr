/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressoapp;

import javafx.application.Application;
import static javafx.application.Application.launch;
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
import javax.swing.*;

import java.sql.*;
import java.sql.Statement.*;
import javafx.scene.control.PasswordField;

/**
 *
 * @author my pc
 */
public class Register implements EventHandler<ActionEvent>{
    
    
    static Stage mainwindow;
    static boolean isOpen = false;
    private static final String databasePath = "jdbc:mysql://localhost:3306/scrappr";
    private static final String databaseUsername = "root";
    private static final String databasePassword = "NewPassword";
    Connection connection;
    Statement statement;
//    static boolean isClosed = true;
    TextField registerfnamefield;
    TextField registerlnamefield;
    TextField registeremailfield ;
    TextField registerusernamefield;
    PasswordField registerpwfield;
    PasswordField confirmregisterpwfield;
    Button registeraccountbutton;
    Button cancelaccountbutton;
    
    
    public void start(){
        mainwindow = new Stage();
        
        isOpen = true;
        
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #232F3E;");
        
        //Labels For Register Window
        Label createyouraccountlbl = new Label("Create Your Account");
        createyouraccountlbl.setFont(Font.font("Bodoni MT Black", 25));
        createyouraccountlbl.setTextFill(Color.WHITE);
        createyouraccountlbl.setLayoutX(265.0);
        createyouraccountlbl.setLayoutY(100.0);
        Label registerfnamelbl = new Label("First Name: ");
        registerfnamelbl.setFont(Font.font("Arial Black", 15));
        registerfnamelbl.setTextFill(Color.WHITE);
        registerfnamelbl.setLayoutX(140.0);
        registerfnamelbl.setLayoutY(140.0);
        Label registerlnamelbl = new Label("Last Name: ");
        registerlnamelbl.setFont(Font.font("Arial Black", 15));
        registerlnamelbl.setTextFill(Color.WHITE);
        registerlnamelbl.setLayoutX(140.0);
        registerlnamelbl.setLayoutY(180.0);
        Label registeremaillbl = new Label("Email: ");
        registeremaillbl.setFont(Font.font("Arial Black", 15));
        registeremaillbl.setTextFill(Color.WHITE);
        registeremaillbl.setLayoutX(140.0);
        registeremaillbl.setLayoutY(220.0);
        Label registerusernamelbl = new Label("Username: ");
        registerusernamelbl.setFont(Font.font("Arial Black", 15));
        registerusernamelbl.setTextFill(Color.WHITE);
        registerusernamelbl.setLayoutX(140.0);
        registerusernamelbl.setLayoutY(260.0);
        Label registerpwlbl = new Label("Password: ");
        registerpwlbl.setFont(Font.font("Arial Black", 15));
        registerpwlbl.setTextFill(Color.WHITE);
        registerpwlbl.setLayoutX(140.0);
        registerpwlbl.setLayoutY(300.0);
        Label confirmregisterpwlbl = new Label("Confirm Password: ");
        confirmregisterpwlbl.setFont(Font.font("Arial Black", 15));
        confirmregisterpwlbl.setTextFill(Color.WHITE);
        confirmregisterpwlbl.setLayoutX(140.0);
        confirmregisterpwlbl.setLayoutY(340.0);
        
        //TextFields for Register Window
        registerfnamefield = new TextField();
        registerfnamefield.setLayoutX(310.0);
        registerfnamefield.setLayoutY(140.0);
        registerfnamefield.setPrefHeight(25.0);
        registerfnamefield.setPrefWidth(233.0);
        
        registerlnamefield = new TextField();
        registerlnamefield.setLayoutX(310.0);
        registerlnamefield.setLayoutY(180.0);
        registerlnamefield.setPrefHeight(25.0);
        registerlnamefield.setPrefWidth(233.0);
        
        registeremailfield = new TextField();
        registeremailfield.setLayoutX(310.0);
        registeremailfield.setLayoutY(220.0);
        registeremailfield.setPrefHeight(25.0);
        registeremailfield.setPrefWidth(233.0);
        
        registerusernamefield = new TextField();
        registerusernamefield.setLayoutX(310.0);
        registerusernamefield.setLayoutY(260.0);
        registerusernamefield.setPrefHeight(25.0);
        registerusernamefield.setPrefWidth(233.0);
        
        registerpwfield = new PasswordField();
        registerpwfield.setLayoutX(310.0);
        registerpwfield.setLayoutY(300.0);
        registerpwfield.setPrefHeight(25.0);
        registerpwfield.setPrefWidth(233.0);
        
        confirmregisterpwfield = new PasswordField();
        confirmregisterpwfield.setLayoutX(310.0);
        confirmregisterpwfield.setLayoutY(340.0);
        confirmregisterpwfield.setPrefHeight(25.0);
        confirmregisterpwfield.setPrefWidth(233.0);
        
        //Buttons for Register Window
        registeraccountbutton = new Button("Register");
        registeraccountbutton.setLayoutX(330.0);
        registeraccountbutton.setLayoutY(380.0);
        registeraccountbutton.setPrefHeight(34.0);
        registeraccountbutton.setPrefWidth(120.0);
        registeraccountbutton.setStyle("-fx-background-color: blue;");
        registeraccountbutton.setTextFill(Color.WHITE);
        registeraccountbutton.setFont(Font.font("Arial Black", 16));
        registeraccountbutton.setOnAction(this);
        
        cancelaccountbutton = new Button("Cancel");
        cancelaccountbutton.setLayoutX(330.0);
        cancelaccountbutton.setLayoutY(430.0);
        cancelaccountbutton.setPrefHeight(34.0);
        cancelaccountbutton.setPrefWidth(120.0);
        cancelaccountbutton.setStyle("-fx-background-color: red;");
        cancelaccountbutton.setTextFill(Color.WHITE);
        cancelaccountbutton.setFont(Font.font("Arial Black", 16));
        cancelaccountbutton.setOnAction(this);
        
        
        root.getChildren().addAll(createyouraccountlbl,registerfnamefield,registerlnamefield,registerfnamelbl,registeremailfield,registerlnamelbl,registeremaillbl,confirmregisterpwfield,confirmregisterpwlbl,registerusernamelbl,registerpwlbl,registerusernamefield,registerpwfield,registeraccountbutton,cancelaccountbutton);
        Scene scene = new Scene(root, 800, 500);
        
        mainwindow.setTitle("Register");
        mainwindow.setScene(scene);
        mainwindow.setOnCloseRequest(e -> {
                Register.isOpen = false;
            }
        );
        mainwindow.show();
    }
    
    @Override
    public void handle(ActionEvent event){
        if(event.getSource()==registeraccountbutton){
            
            System.out.println("Register account button clicked");
            String firstName,lastName;
            String email,username,password;
            String confirmPassword;
            
            firstName = registerfnamefield.getText();
            lastName = registerlnamefield.getText();
            email = registeremailfield.getText();
            username = registerusernamefield.getText();
            password = registerpwfield.getText();
            confirmPassword = confirmregisterpwfield.getText();
            
            for(int i = 0; i < firstName.length();i++){
                try{
                    if(Character.isDigit(firstName.charAt(i))){
                    throw new InvalidInputException();
                    }
                    if(String.valueOf(firstName.charAt(i)).matches("[^a-zA-Z0-9]")){
                        throw new InvalidInputException();
                    }
                    if(firstName.trim().length()== 0){
                        throw new InvalidInputException();
                    }
                }catch(InvalidInputException e){
                    JOptionPane.showMessageDialog(null,"First name must not have any numbers or special characters!","Invalid Input",JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            for(int i = 0; i < lastName.length();i++){
                try{
                    if(Character.isDigit(lastName.charAt(i))){
                    throw new InvalidInputException();
                }
                if(String.valueOf(lastName.charAt(i)).matches("[^a-zA-Z0-9]")){
                    throw new InvalidInputException();
                }
                if(lastName.trim().length()== 0){
                    throw new InvalidInputException();
                }
                }catch(InvalidInputException e){
                    JOptionPane.showMessageDialog(null,"Last name must not have any numbers or special characters!","Invalid Input",JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            for(int i = 0; i < email.length();i++){
                try{
                    if(email.trim().length()== 0){
                        throw new InvalidInputException();
                    }
                }catch(InvalidInputException e){
                    JOptionPane.showMessageDialog(null,"Email must be atleast 8 characters long!","Invalid Input",JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            for(int i = 0; i < username.length();i++){
                try{
                    if(Character.isSpace(username.charAt(i))){
                        throw new InvalidInputException();
                    }
                    
                    if(username.trim().length()== 0){
                        throw new InvalidInputException();
                    }
                }catch(InvalidInputException e){
                    JOptionPane.showMessageDialog(null,"Username must be atleast 8 characters long!","Invalid Input",JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            try{
                if(usernameExists(username)){
                    throw new DuplicateUsername();
                };
            }catch(SQLException e){
                
            }
            catch(ClassNotFoundException  se){
                
            }catch(DuplicateUsername du){
                JOptionPane.showMessageDialog(null,"Username already exists! Please try again!","Invalid Input!",JOptionPane.WARNING_MESSAGE);
                return;
            }catch(Exception e){
                e.printStackTrace();
            }
            
            for(int i = 0; i < password.length();i++){
                try{
                    if(password.trim().length()== 0){
                        throw new InvalidInputException();
                    }
                }catch(InvalidInputException e){
                    JOptionPane.showMessageDialog(null,"Password must be atleast 8 characters long!","Invalid Input",JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            try{
                if(!password.equals(confirmPassword)){
                    throw new InvalidInputException();
                }
            }catch(InvalidInputException e){
                    JOptionPane.showMessageDialog(null,"Password must match confirm password field!","Invalid Input",JOptionPane.WARNING_MESSAGE);
                    return;
                }
            
            try{
                registerAccount(firstName,lastName,email,username,password);
            }catch(SQLException e){
                
            }
            catch(ClassNotFoundException  se){
                
            }catch(Exception e){
                e.printStackTrace();
            }
            
            JOptionPane.showMessageDialog(null,"Account creation sucessful! You may now log into your account!","Register successful",JOptionPane.PLAIN_MESSAGE);
            mainwindow.close();
        }
        if(event.getSource()==cancelaccountbutton){
            System.out.println("Cancel account button clicked");
            Register.isOpen = false;
            mainwindow.close();
        }
    }

    
    public void registerAccount(String firstName,String lastName, String email, String username, String password) throws SQLException,ClassNotFoundException{
        try{
            final String query = String.format("INSERT INTO customers(customer_email,customer_pass,first_name,last_name,username) VALUES ('%s','%s','%s','%s','%s')",email,password,firstName,lastName,username);
            connection = DriverManager.getConnection(databasePath,databaseUsername,databasePassword);
            statement = connection.createStatement();
            statement.execute(query);
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
    }
    
    public boolean usernameExists(String username)throws SQLException,ClassNotFoundException{
        
        try{
            
            connection = DriverManager.getConnection(databasePath,databaseUsername,databasePassword);
            statement = connection.createStatement();
            final String query = String.format("SELECT username FROM customers WHERE username = '%s'",username);
            ResultSet resultSet = statement.executeQuery(query);
        
            while(resultSet.next()){
                if(resultSet.getString("username").equals(username)){
                    return true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return true;
        }
        
        return false;
    }
        
    public class InvalidInputException extends Exception{
        
    }
    public class DuplicateUsername extends Exception{
        
    }
    
}
