/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressoapp;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author my pc
 */
public class SampleClass extends Application {
    
    Button newButton;
    
    @Override
    public void start(Stage primaryStage) throws IOException {     
        primaryStage.setTitle("Scrappr.");

//        primaryStage.setResizable(false);
        newButton = new Button();
        newButton.setText("Click me");
        
        AnchorPane anchorLayout = new AnchorPane();
        
        
        anchorLayout.getChildren().add(newButton);
        
        Scene scene = new Scene(anchorLayout,300,250);
        
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
