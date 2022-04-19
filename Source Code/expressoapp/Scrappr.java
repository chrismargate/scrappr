/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressoapp;

import java.awt.image.BufferedImage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.imageio.ImageIO;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.sql.*;
import java.sql.Statement.*;

/**
 *
 * @author my pc
 */
//implements EventHandler<ActionEvent>
public class Scrappr implements EventHandler<ActionEvent>{

    // DATABASE INFO

    private static final String databasePath = "jdbc:mysql://localhost:3306/scrappr";
    private static final String databaseUsername = "root";
    private static final String databasePassword = "NewPassword";
//    Connection connection;

    private TabPane root = new TabPane();
    Image pinImage = new Image("images/push_pin_32px.png");
    ImageView amazonpinicon = new ImageView(pinImage);
    ImageView shopeepinicon = new ImageView(pinImage);
    ImageView lazadapinicon = new ImageView(pinImage);
    
    // Home Tab Components
    // SEARCH BAR
    AnchorPane searchpane = new AnchorPane();
    Label scrappr = new Label("SCRAPPR.");
    TextField searchfield = new TextField();
    Button searchbutton = new Button("Search");
    Button logoutbutton = new Button("Logout");
    
    // Pane for Shop Results
    AnchorPane shopspane = new AnchorPane();
    
    // Panes inside shopspane
    // Amazonpane Components
    AnchorPane amazonpane = new AnchorPane();
    Pane amazonproductpane = new Pane();
    ImageView amazonproductpic = new ImageView();   
    Label amazonproducttitle = new Label("Product Title");
    Label amazonproductrating = new Label("Rating: ");
    Label amazonproductratingvalue = new Label("0.0 ");
    Label amazonproductprice = new Label("Price: ");
    Label amazonproductpricevalue = new Label("0.0 ");
    
    Button amazonpinbutton = new Button();
    Button prevbuttonamazon = new Button("Previous Button");
    Button nextbuttonamazon = new Button("Next Button");
    int amazonStartProduct = 1;
    
    
    // Shopeepane Components
    AnchorPane shopeepane = new AnchorPane();
    Pane shopeeproductpane;
    ImageView shopeeproductpic;
    Label shopeeproducttitle;
    Hyperlink shopeeproductlink;

    Label shopeeproductrating;
    Label shopeeproductratingvalue;

    Label shopeeproductprice;
    Label shopeeproductpricevalue;

    Button shopeepinbutton;
    Button prevbuttonshopee = new Button("Previous Button");
    Button nextbuttonshopee = new Button("Next Button");
    int shopeeStartProduct = 1;
    
    int activeIndex;
    
    
    // Lazadapane Components
    AnchorPane lazadapane = new AnchorPane();
    Pane lazadaproductpane;
    ImageView lazadaproductpic;
    Label lazadaproducttitle;
    Hyperlink lazadaproductlink;

    Label lazadaproductrating;
    Label lazadaproductratingvalue;
    Label lazadaproductprice;
    Label lazadaroductpricevalue;

    Button lazadapinbutton;
    Button prevbuttonlazada = new Button("Previous Button");
    Button nextbuttonlazada = new Button("Next Button");
    int lazadaStartProduct = 1;
    
    // Tabs panes
    AnchorPane homepane = new AnchorPane();
    AnchorPane watchlistpane = new AnchorPane();
    AnchorPane accountpane = new AnchorPane();
    AnchorPane aboutuspane = new AnchorPane();
    AnchorPane calluspane = new AnchorPane();
    
    // Tabs
    Tab hometab = new Tab("Home");
    Tab watchlisttab = new Tab("Watchlist");
    Tab accounttab = new Tab("Account");
    Tab aboutustab = new Tab("About Us");
    Tab callustab = new Tab("Call Us");

    // Components for Watchlist Tab
    ScrollPane watchlistscrollpane = new ScrollPane();
    Pane watchlistproductpane = new Pane();
    
    ImageView watchlistproductpic = new ImageView();   
    Label watchlistproducttitle = new Label("Product Title");
    Label watchlistproductrating = new Label("Rating: ");
    Label watchlistproductprice = new Label("Price: ");

    // Components for Account Tab
    Pane editaccountpane = new Pane();
    Label edityouraccountlbl = new Label("Edit Your Account");
    Label usernamelbl = new Label("Username: ");
    Label pwlbl = new Label("Password: ");      
    Label emaillbl = new Label("Email: "); 
    TextField usernamefield = new TextField();
    TextField pwfield = new TextField();
    TextField emailfield = new TextField();
    Button editaccountbutton = new Button("Edit");
    Button saveaccountbutton = new Button("Save");

    float nextprevButtonPositionY = 605;    

    // Components for About us Tab
    AnchorPane aboutustextpane = new AnchorPane();
    Text aboutustext = new Text("\nWhat do Scrappr. do?\n\n\nScrappr. is a small application that will help you(users) to find your desired item that is\navailable and can be found on the following e-commerce websites: Lazada, Shopee, and Amazon.\n\nOur goal is to make it easier for you to compare the products’ prices, user ratings, and\ndescriptions without having you go to each e-commerce websites directly. We also would like\nto help you keep track of your desired product/s through the use of a watchlist.\n\nABOUT US\nScrappr. is a small application that aims to help customers to find their desired product/s easier\nand faster. Search for products from Lazada, Shopee, and Amazon in just one application.\nHaving a variety of products from different categories.\n\nOur goal:\nTo make it easier for the customers to compare the products' prices, user rating, and description.\nTo help the customers minimize the time it takes to look for a specific product across different\ne-commerce websites.\nTo give the ability to the customers to bookmark listings of their desired product.");

    // Components for Call us Tab
    AnchorPane callustextpane = new AnchorPane();
    Text callustext = new Text("Contact Us: 09183643582(SMART)\n\nor email us: jerudrian@gmail.com\n\nFacebook: Adrian Esguerra\n\nTwitter: @Ionusz");

    // SEARCHERS
    ShopeeSearcher shopeeSearcher;
    LazadaSearcher lazadaSearcher;
    AmazonSearcher amazonSearcher;
    
    static Stage mainwindow;

//    @Override
    public void start() throws Exception{
       mainwindow = new Stage();
       System.setProperty("webdriver.chrome.driver","C:/Users/Chris/Documents/VSCodeJavaProjects/chromedriver_win32/chromedriver.exe");
//        connection = DriverManager.getConnection(databasePath,databaseUsername,databasePassword);
        
        shopeeSearcher = new ShopeeSearcher(shopeeStartProduct,10);
        lazadaSearcher = new LazadaSearcher(lazadaStartProduct,10);

    //    amazonSearcher = new AmazonSearcher();
    //    Thread amazonThread = new Thread(amazonSearcher);

        //Pane for HomeTab
        homepane.setStyle("-fx-background-color: #131921;");
        homepane.getChildren().addAll(searchpane,logoutbutton,searchbutton,searchfield,scrappr,shopspane);
        
        //Pane for WatchlistTab
        watchlistpane.setStyle("-fx-background-color: #131921;");

        //Pane for AccountTab
        accountpane.setStyle("-fx-background-color: #131921;");

        //Pane for AboutusTab
        aboutuspane.setStyle("-fx-background-color: #131921;");

        //Pane for CallusTab
        calluspane.setStyle("-fx-background-color: #131921;");

        configureNavigationBar();
        
        drawShopsPane();

        drawAmazonPane();

        drawLazadaPane();

        drawShopeePane();

//        generateShopeeResults();

//        generateLazadaResults();

        // TABS CONFIGURATION
        configureWatchlistTab();

        configureAccountTab();
        
        configureAboutUsTab();
        
        configureContactUsTab();

        hometab.setClosable(false);
        hometab.setContent(homepane);
        
        watchlisttab.setClosable(false);
        watchlisttab.setContent(watchlistpane);
        
        accounttab.setClosable(false);
        accounttab.setContent(accountpane);
        
        aboutustab.setClosable(false);
        aboutustab.setContent(aboutuspane);
        
        callustab.setClosable(false);
        callustab.setContent(calluspane);

        // SHOW
        root.getTabs().addAll(hometab,watchlisttab,accounttab,aboutustab,callustab);  
        Scene scene = new Scene(root, 1350, 720);     
        mainwindow.setTitle("Scrappr.");
        mainwindow.setResizable(false);
        mainwindow.setScene(scene);
        mainwindow.setOnCloseRequest(e -> {
            mainwindow.close();
        });
        mainwindow.show();
    }
    
    public void configureNavigationBar(){
        searchpane.setLayoutX(0.0);
        searchpane.setLayoutY(0.0);
        searchpane.setPrefHeight(40.0);
        searchpane.setPrefWidth(1350.0);
        searchpane.setStyle("-fx-background-color: #232F3E;");
        
        scrappr.setFont(Font.font("Segoe Script", 18));
        scrappr.setTextFill(Color.WHITE);
        scrappr.setLayoutX(66.0);
        scrappr.setLayoutY(3.0);
        scrappr.setPrefWidth(158.0);
        scrappr.setPrefHeight(34.0);
        
        searchfield.setLayoutX(250.0);
        searchfield.setLayoutY(8.0);
        searchfield.setPrefWidth(231.0);
        searchfield.setPrefHeight(25.0);
//        searchfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
//
//           public void handle(KeyEvent event) {
//               if(event.getCode().equals(KeyCode.ENTER)){
//                    System.out.println("You pressed enter on the search field");
//                    shopeeSearcher.setSearch(searchfield.getText());
//                    amazonStartProduct = 1;
//                    shopeeStartProduct = 1;
//                    lazadaStartProduct = 1;
//                    try{
//                        shopeeSearcher = new ShopeeSearcher(shopeeStartProduct,10);
//                        shopeeSearcher.start();
//                        shopeeSearcher.join();
//                        System.out.println(shopeeSearcher.isAlive());
//                        System.out.println(shopeeSearcher.currentThread().getState());
//                        System.out.println(Thread.currentThread().getState());
//                    }catch(Exception e){
//                        System.out.println("Exception block");
//                        e.printStackTrace();
//                    }finally{
//                        System.out.println("Finally block");
//                        generateShopeeResults();
//                    }
//               }
//           }
//        });
        
        searchbutton.setFont(Font.font("Arial Black", 12));
        searchbutton.setLayoutX(500.0);
        searchbutton.setLayoutY(8.0);
        searchbutton.setPrefWidth(91.0);
        searchbutton.setPrefHeight(25.0);
        searchbutton.setOnAction(this);
//                new EventHandler<ActionEvent>() {
// 
//        public void handle(ActionEvent event) {
//
//                    System.out.println("You pressed the search button");
//                    shopeeSearcher.setSearch(searchfield.getText());
//                    amazonStartProduct = 1;
//                    shopeeStartProduct = 1;
//                    lazadaStartProduct = 1;
//                    try {
//                        shopeeSearcher.start();
//                        
//                        while(shopeeSearcher.isAlive()){
//                            Thread.sleep(1000);
//                        }
//                        generateShopeeResults();
////                        throw new Exception();
//                    }catch(Exception e){   
//                        
//                    }
//
//        }
//        });
        
        logoutbutton.setFont(Font.font("Arial Black", 12));
        logoutbutton.setLayoutX(1200.0);
        logoutbutton.setLayoutY(7.0);
        logoutbutton.setPrefWidth(91.0);
        logoutbutton.setPrefHeight(25.0);
        logoutbutton.setOnAction(new EventHandler<ActionEvent>() {
 
        public void handle(ActionEvent event) {
            System.out.println("You clicked logout button");
            mainwindow.close();
            Login.activeCustomerID = 0;
            Login.loggedIn = false;
            Login.mainwindow.show();
        }
        });
    }
    
    public void drawShopsPane(){
        shopspane.setLayoutX(0.0);
        shopspane.setLayoutY(40.0);
        shopspane.setPrefWidth(1040.0);
        shopspane.setPrefHeight(600.0);
        shopspane.setStyle("-fx-background-color: cyan");
        shopspane.getChildren().addAll(amazonpane,shopeepane,lazadapane);
    }
    
    public void drawAmazonPane(){
        amazonpane.setLayoutX(0.0);
        amazonpane.setLayoutY(0.0);
        amazonpane.setPrefWidth(450.0);
        amazonpane.setPrefHeight(635.0);
        amazonpane.setStyle("-fx-background-color: white");
                
        amazonproductpane.setPrefHeight(60.0);
        amazonproductpane.setPrefWidth(450.0);
        amazonproductpane.setLayoutX(0);
        amazonproductpane.setLayoutY(100);
        amazonproductpane.setStyle("-fx-background-color: gray;");
            
        amazonproductpic.setLayoutX(5.0);
        amazonproductpic.setLayoutY(5.0);
        amazonproductpic.setFitHeight(50.0);          //SAKA NA ALISIN UNG COMMENTS PAG NAILAGAY NA UNG MGA PICTURES
        amazonproductpic.setFitWidth(80.0);
        amazonproductpic.setPickOnBounds(true);
        amazonproductpic.setPreserveRatio(true);
        
        amazonproducttitle.setLayoutX(90.0);
        amazonproducttitle.setLayoutY(5.0);
        amazonproducttitle.setFont(Font.font("System Bold", 12));
        
        amazonproductrating.setLayoutX(90.0);
        amazonproductrating.setLayoutY(25.0);
        amazonproductratingvalue.setLayoutX(130.0);
        amazonproductratingvalue.setLayoutY(25.0);
        
        amazonproductprice.setLayoutX(90.0);
        amazonproductprice.setLayoutY(40.0);
        amazonproductpricevalue.setLayoutX(130.0);
        amazonproductpricevalue.setLayoutY(40.0);
        
        amazonpinbutton.setGraphic(amazonpinicon);
        amazonpinbutton.setLayoutX(400.0);
        amazonpinbutton.setLayoutY(18.0);
        amazonpinbutton.setMnemonicParsing(false);
        amazonpinbutton.setOnAction(new EventHandler<ActionEvent>() {

        public void handle(ActionEvent event) {
            System.out.println("You clicked pin button");
        }
        });
        amazonpinbutton.setPrefWidth(40.0);
        amazonpinbutton.setPrefHeight(40.0);
        amazonproductpane.getChildren().addAll(amazonproducttitle,amazonproductrating,amazonproductprice,amazonpinbutton,amazonproductpic); 
            
        prevbuttonamazon.setLayoutX(50.0);
        prevbuttonamazon.setLayoutY(nextprevButtonPositionY);
        prevbuttonamazon.setMnemonicParsing(false);
        prevbuttonamazon.setOnAction(new EventHandler<ActionEvent>() {

        public void handle(ActionEvent event) {
            System.out.println("You clicked previous button");
        }
        });
        
        nextbuttonamazon.setLayoutX(250.0);
        nextbuttonamazon.setLayoutY(nextprevButtonPositionY);
        nextbuttonamazon.setMnemonicParsing(false);
        nextbuttonamazon.setOnAction(new EventHandler<ActionEvent>() {

        public void handle(ActionEvent event) {
            System.out.println("You clicked next button");
        }
        });
        amazonpane.getChildren().addAll(prevbuttonamazon,nextbuttonamazon);
    }

    public void drawLazadaPane(){

        lazadapane.setLayoutX(900.0);
        lazadapane.setLayoutY(0.0);
        lazadapane.setPrefWidth(450.0);
        lazadapane.setPrefHeight(635.0);
        lazadapane.setStyle("-fx-background-color: white");
        //Products will be inserted here
//        lazadaproductpane.setPrefHeight(60.0);
//        lazadaproductpane.setPrefWidth(347.0);
//        lazadaproductpane.setStyle("-fx-background-color: gray;"); 
//
//        lazadaproductpic.setLayoutX(5.0);
//        lazadaproductpic.setLayoutY(5.0);
//        lazadaproductpic.setFitHeight(50.0);          
//        lazadaproductpic.setFitWidth(80.0);
//        lazadaproductpic.setPickOnBounds(true);
//        lazadaproductpic.setPreserveRatio(true);
//
//        lazadaproducttitle.setLayoutX(90.0);
//        lazadaproducttitle.setLayoutY(5.0);
//        lazadaproducttitle.setFont(Font.font("System Bold", 12));
//        
//        lazadaproductrating.setLayoutX(90.0);
//        lazadaproductrating.setLayoutY(25.0);
//        
//        lazadaproductratingvalue.setLayoutX(145.0);
//        lazadaproductratingvalue.setLayoutY(25.0);
//        
//        lazadaproductprice.setLayoutX(90.0);
//        lazadaproductprice.setLayoutY(40.0);
//        
//        lazadaroductpricevalue.setLayoutX(145.0);
//        lazadaroductpricevalue.setLayoutY(40.0);
//        
//        lazadapinbutton.setGraphic(lazadapinicon);
//        lazadapinbutton.setLayoutX(296.0);
//        lazadapinbutton.setLayoutY(18.0);
//        lazadapinbutton.setMnemonicParsing(false);
//        lazadapinbutton.setOnAction(new EventHandler<ActionEvent>() {
//
//        public void handle(ActionEvent event) {
//            System.out.println("You clicked pin button");
//        }
//        });
//        lazadapinbutton.setPrefWidth(40.0);
//        lazadapinbutton.setPrefHeight(40.0);
//            lazadaproductpane.getChildren().addAll(lazadaproducttitle,lazadaproductrating,lazadaproductprice,lazadapinbutton,lazadaproductpic); 
        
        prevbuttonlazada.setLayoutX(50.0);
        prevbuttonlazada.setLayoutY(nextprevButtonPositionY);
        prevbuttonlazada.setMnemonicParsing(false);
        prevbuttonlazada.setOnAction(new EventHandler<ActionEvent>() {

        public void handle(ActionEvent event) {
            System.out.println("You clicked previous button");
        }
        });
        
        nextbuttonlazada.setLayoutX(250.0);
        nextbuttonlazada.setLayoutY(nextprevButtonPositionY);
        nextbuttonlazada.setMnemonicParsing(false);
        nextbuttonlazada.setOnAction(new EventHandler<ActionEvent>() {

        public void handle(ActionEvent event) {
            System.out.println("You clicked next button");
        }
        });
        lazadapane.getChildren().addAll(prevbuttonlazada,nextbuttonlazada);
    }

    public void drawShopeePane(){
        
        shopeepane.setLayoutX(450.0);
        shopeepane.setLayoutY(0.0);
        shopeepane.setPrefWidth(450.0);
        shopeepane.setPrefHeight(635.0);
        shopeepane.setStyle("-fx-background-color: white");

        prevbuttonshopee.setLayoutX(50.0);
        prevbuttonshopee.setLayoutY(605.0);
        prevbuttonshopee.setMnemonicParsing(false);
        prevbuttonshopee.setOnAction(this);
        
        nextbuttonshopee.setLayoutX(250.0);
        nextbuttonshopee.setLayoutY(605.0);
        nextbuttonshopee.setMnemonicParsing(false);
        nextbuttonshopee.setOnAction(this);
            
        shopeepane.getChildren().addAll(prevbuttonshopee,nextbuttonshopee);
    }

    public void configureWatchlistTab(){
        watchlistscrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        watchlistscrollpane.setPrefWidth(1350.0);
        watchlistscrollpane.setPrefHeight(670.0);

        watchlistproductpane.setPrefHeight(60.0);
        watchlistproductpane.setPrefWidth(450.0);
        watchlistproductpane.setStyle("-fx-background-color: gray;");

        watchlistproductpic.setLayoutX(5.0);
        watchlistproductpic.setLayoutY(5.0);
        watchlistproductpic.setFitHeight(50.0);          
        watchlistproductpic.setFitWidth(80.0);
        watchlistproductpic.setPickOnBounds(true);
        watchlistproductpic.setPreserveRatio(true);

        watchlistproducttitle.setLayoutX(90.0);
        watchlistproducttitle.setLayoutY(5.0);
        watchlistproducttitle.setFont(Font.font("System Bold", 12));

        watchlistproductrating.setLayoutX(90.0);
        watchlistproductrating.setLayoutY(25.0);

        watchlistproductprice.setLayoutX(90.0);
        watchlistproductprice.setLayoutY(40.0);
        watchlistproductpane.getChildren().addAll(watchlistproducttitle,watchlistproductrating,watchlistproductprice); //SAKA NA ILAGAY UNG amazonproductpic PAG NAILAGAY NA UNG MGA PICTURES
        watchlistpane.getChildren().addAll(watchlistscrollpane,watchlistproductpane);
    }

    public void configureAccountTab(){
        //Components for AccountTab

        editaccountpane.setLayoutX(460.0);
        editaccountpane.setLayoutY(202.0);
        editaccountpane.setPrefHeight(287.0);
        editaccountpane.setPrefWidth(399.0);
        editaccountpane.setStyle("-fx-background-color: #232F3E;");

        edityouraccountlbl.setFont(Font.font("Bodoni MT Black", 25));
        edityouraccountlbl.setTextFill(Color.WHITE);
        edityouraccountlbl.setLayoutX(88.0);
        edityouraccountlbl.setLayoutY(14.0);

        usernamelbl.setFont(Font.font("Arial Black", 15));
        usernamelbl.setTextFill(Color.WHITE);
        usernamelbl.setLayoutX(27.0);
        usernamelbl.setLayoutY(59.0);

        pwlbl.setFont(Font.font("Arial Black", 15));
        pwlbl.setTextFill(Color.WHITE);
        pwlbl.setLayoutX(27.0);
        pwlbl.setLayoutY(121.0);

        emaillbl.setFont(Font.font("Arial Black", 15));
        emaillbl.setTextFill(Color.WHITE);
        emaillbl.setLayoutX(27.0);
        emaillbl.setLayoutY(188.0);

        usernamefield.setLayoutX(137.0);
        usernamefield.setLayoutY(58.0);
        usernamefield.setPrefHeight(25.0);
        usernamefield.setPrefWidth(233.0);

        pwfield.setLayoutX(137.0);
        pwfield.setLayoutY(120.0);
        pwfield.setPrefHeight(25.0);
        pwfield.setPrefWidth(233.0);

        emailfield.setLayoutX(137.0);
        emailfield.setLayoutY(187.0);
        emailfield.setPrefHeight(25.0);
        emailfield.setPrefWidth(233.0);

        editaccountbutton.setLayoutX(51.0);
        editaccountbutton.setLayoutY(239.0);
        editaccountbutton.setMnemonicParsing(false);
        editaccountbutton.setOnAction(new EventHandler<ActionEvent>() {

        public void handle(ActionEvent event) {
            System.out.println("You clicked edit button");
        }
        });
        editaccountbutton.setPrefHeight(34.0);
        editaccountbutton.setPrefWidth(93.0);
        editaccountbutton.setStyle("-fx-background-color: blue;");
        editaccountbutton.setTextFill(Color.WHITE);
        editaccountbutton.setFont(Font.font("Arial Black", 16));

        saveaccountbutton.setLayoutX(292.0);
        saveaccountbutton.setLayoutY(239.0);
        saveaccountbutton.setMnemonicParsing(false);
        saveaccountbutton.setOnAction(new EventHandler<ActionEvent>() {

                public void handle(ActionEvent event) {
                    System.out.println("You clicked save button");
                }
                });
        saveaccountbutton.setPrefHeight(34.0);
        saveaccountbutton.setPrefWidth(93.0);
        saveaccountbutton.setStyle("-fx-background-color: green;");
        saveaccountbutton.setTextFill(Color.WHITE);
        saveaccountbutton.setFont(Font.font("Arial Black", 16));
        
        editaccountpane.getChildren().addAll(edityouraccountlbl,usernamelbl,pwlbl,emaillbl,usernamefield,pwfield,emailfield,editaccountbutton,saveaccountbutton);
        accountpane.getChildren().add(editaccountpane);
    }

    public void configureAboutUsTab(){
        aboutustextpane.setLayoutX(390.0);
        aboutustextpane.setLayoutY(133.0);
        aboutustextpane.setPrefHeight(425.0);
        aboutustextpane.setPrefWidth(633.0);
        aboutustextpane.setStyle("-fx-background-color: #232F3E;");
        
        aboutustext.setLayoutX(2.0);
        aboutustext.setLayoutY(13.0);
        aboutustext.setFill(Color.WHITE);
        aboutustext.setStrokeType(StrokeType.OUTSIDE);
        aboutustext.setFont(Font.font("Arial Black", 12));
        aboutustext.setStrokeWidth(0.0);
        aboutustextpane.getChildren().add(aboutustext);
        aboutuspane.getChildren().add(aboutustextpane);
    }

    public void configureContactUsTab(){
        callustextpane.setLayoutX(440.0);
        callustextpane.setLayoutY(127.0);
        callustextpane.setPrefHeight(437.0);
        callustextpane.setPrefWidth(500.0);
        callustextpane.setStyle("-fx-background-color: #232F3E;");
        
        callustext.setLayoutX(2.0);
        callustext.setLayoutY(126.0);
        callustext.setFill(Color.WHITE);
        callustext.setStrokeType(StrokeType.OUTSIDE);
        callustext.setFont(Font.font("Arial Black", 24));
        callustext.setStrokeWidth(0.0);
        callustextpane.getChildren().add(callustext);
        calluspane.getChildren().add(callustextpane);
    }

    public void generateLazadaResults(){
        //        lazadaproducttitle.setLayoutX(90.0);
//        lazadaproducttitle.setLayoutY(5.0);
//        lazadaproducttitle.setFont(Font.font("System Bold", 12));
//
//        lazadaproducttitle.addEventHandler(MouseEvent.MOUSE_ENTERED, 
//            new EventHandler<MouseEvent>() {
//                @Override public void handle(MouseEvent e) {
//                    System.out.println("Mouse entered");
//                    lazadaproducttitle.setUnderline(true);
//                    // lazadaproducttitle.setText(lazadaproducttitle.getText());
//                }
//        });
//        lazadaproducttitle.addEventHandler(MouseEvent.MOUSE_EXITED, 
//            new EventHandler<MouseEvent>() {
//                @Override public void handle(MouseEvent e) {
//                    System.out.println("Mouse exited");
//                    lazadaproducttitle.setUnderline(false);
//                    // lazadaproducttitle.setText(lazadaproducttitle.getText());
//                }
//        });
        
    float increment = 0;
    String imageLink;
    URL imageURL;
    String productTitle;
    BufferedImage bufferedImage;
    Image image;
    
    for(Product productItem: lazadaSearcher.getProductsList()){
        
        imageLink = productItem.getProductImageLink();
        
        if(imageLink != null){
            try{
            imageURL = new URL(imageLink);
            bufferedImage = ImageIO.read(imageURL);
            image = javafx.embed.swing.SwingFXUtils.toFXImage(bufferedImage, null);
            lazadaproductpic = new ImageView(image);
            }catch(Exception exp) {
            System.out.println("Displaying image error");
            exp.printStackTrace();
            lazadaproductpic = new ImageView();
            }
        }else{
            lazadaproductpic = new ImageView();
        }
        
        lazadaproductpane = new Pane();
        lazadaproductpic = new ImageView();
        lazadaproducttitle = new Label("Product Title");

        lazadaproductrating = new Label("Rating: ");
        lazadaproductratingvalue = new Label();
        
        lazadaproductprice = new Label("Price: ");
        lazadaroductpricevalue = new Label();

        lazadapinbutton = new Button();

        lazadaproductpane.setPrefHeight(60.0);
        lazadaproductpane.setPrefWidth(450.0);
        lazadaproductpane.setLayoutY(increment);
        lazadaproductpane.setStyle("-fx-background-color: gray;"); 

        lazadaproductpic.setLayoutX(5.0);
        lazadaproductpic.setLayoutY(5.0);
        lazadaproductpic.setFitHeight(50.0);          
        lazadaproductpic.setFitWidth(80.0);
        lazadaproductpic.setPickOnBounds(true);
        lazadaproductpic.setPreserveRatio(true);
        
        productTitle = productItem.getProductName();
        
        if(productItem.getProductName().length() >= 43){
            productTitle = productItem.getProductName().substring(0, 45) + "..";
        }
        
        lazadaproductlink = new Hyperlink();
        lazadaproductlink.setText(productTitle);
        lazadaproductlink.setLayoutX(85.0);
        lazadaproductlink.setLayoutY(5.0);
        lazadaproductlink.setFont(Font.font("System Bold", 12));
        lazadaproductlink.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("disable-notifications");
                options.addArguments("--disable-infobars");
                options.addArguments("start-maximized");
                options.addArguments("disable-infobars");
                options.setHeadless(false);
                
                WebDriver driver = new ChromeDriver(options);
                Dimension driverDimensions = new Dimension(1920, 1080);
                driver.manage().window().setSize(driverDimensions);
                driver.manage().window().maximize();
                
                driver.get(productItem.getProductLink());
            }
        });

        lazadaproductrating.setLayoutX(90.0);
        lazadaproductrating.setLayoutY(25.0);
        
        lazadaroductpricevalue.setText(productItem.getProductPrice());
        lazadaproductratingvalue.setLayoutX(130.0);
        lazadaproductratingvalue.setLayoutY(25.0);
        
        lazadaproductprice.setLayoutX(90.0);
        lazadaproductprice.setLayoutY(40.0);
        
        lazadaroductpricevalue.setLayoutX(145.0);
        lazadaroductpricevalue.setLayoutY(40.0);
        
        lazadapinbutton.setGraphic(new ImageView(new Image("images/push_pin_32px.png")));
        lazadapinbutton.setLayoutX(385.0);
        lazadapinbutton.setLayoutY(8.0);
        lazadapinbutton.setMnemonicParsing(false);
        lazadapinbutton.setOnAction(new EventHandler<ActionEvent>() {

        public void handle(ActionEvent event) {
            System.out.println("You clicked pin button");
        }
        });
        lazadapinbutton.setPrefWidth(40.0);
        lazadapinbutton.setPrefHeight(40.0);
        lazadaproductpane.getChildren().addAll(lazadaproductlink,lazadaproductrating,lazadaproductprice,lazadapinbutton,lazadaproductpic,lazadaroductpricevalue,lazadaproductratingvalue); 
        lazadapane.getChildren().addAll(lazadaproductpane);
        increment += 60.125;
        }
    }
    
   public void generateShopeeResults(){
   float increment = 0;
   String imageLink;
   URL imageURL;
   String productTitle;
   BufferedImage bufferedImage;
   Image image;
   //        System.out.println(productItem.getProductLink());
//             System.out.println(productItem.getProductName());
//             System.out.println(productItem.getProductPrice());
//             System.out.println(productItem.getProductRating());
//   ArrayList<Button> shopeePinButtons = new ArrayList<>();

//    for(int i = 0; i < 10; i++)
//        shopeeproductlink.addEventHandler(MouseEvent.MOUSE_ENTERED, 
//            new EventHandler<MouseEvent>() {
//                @Override public void handle(MouseEvent e) {
//                    
//                    System.out.println("Mouse entered");
//                    shopeeproductlink.setStyle("-fx-color: cyan;");
//                }
//        });
//        shopeeproductlink.addEventHandler(MouseEvent.MOUSE_EXITED, 
//            new EventHandler<MouseEvent>() {
//                @Override public void handle(MouseEvent e) {
//                    System.out.println("Mouse exited");
//                    shopeeproductlink.setStyle("-fx-color: black;");
//                }
//        });
     
//        shopeeproducttitle.setText("Product Title");
//        // shopeeproducttitle.setText(Integer.toString(i));
//        shopeeproducttitle.addEventHandler(MouseEvent.MOUSE_ENTERED, 
//            new EventHandler<MouseEvent>() {
//                @Override public void handle(MouseEvent e) {
//                    System.out.println("Mouse entered");
//                    shopeeproducttitle.setUnderline(true);
//                    shopeeproducttitle.setText(shopeeproducttitle.getText());
//                }
//        });
//        shopeeproducttitle.addEventHandler(MouseEvent.MOUSE_EXITED, 
//            new EventHandler<MouseEvent>() {
//                @Override public void handle(MouseEvent e) {
//                    System.out.println("Mouse exited");
//                    shopeeproducttitle.setUnderline(false);
//                    shopeeproducttitle.setText(shopeeproducttitle.getText());
//                }
//        });
//        shopeeproducttitle.setLayoutX(90.0);
//        shopeeproducttitle.setLayoutY(5.0);
//        shopeeproducttitle.setFont(Font.font("System Bold", 12));
        
   for(Product productItem: shopeeSearcher.getProductsList()){
            
        imageLink = productItem.getProductImageLink();
        
        if(imageLink != null){
            try{
            imageURL = new URL(imageLink);
            bufferedImage = ImageIO.read(imageURL);
            image = javafx.embed.swing.SwingFXUtils.toFXImage(bufferedImage, null);
            shopeeproductpic = new ImageView(image);
            }catch(Exception exp) {
            System.out.println("Displaying image error");
            exp.printStackTrace();
            shopeeproductpic = new ImageView();
            }
        }else{
            shopeeproductpic = new ImageView();
        }
        
        
        shopeeproductpane = new Pane();
        shopeeproducttitle = new Label("Product Title");

        shopeeproductrating = new Label("Rating: ");
        shopeeproductratingvalue = new Label("0.0 ");

        shopeeproductprice = new Label("Price: ");
        shopeeproductpricevalue = new Label();

        shopeepinbutton = new Button();
        
        

        shopeeproductpane.setPrefHeight(60.0);
        shopeeproductpane.setPrefWidth(450.0);
        shopeeproductpane.setLayoutY(increment);
        
        shopeeproductpane.setStyle("-fx-background-color: gray;");
        
        shopeeproductpic.setLayoutX(5.0);
        shopeeproductpic.setLayoutY(5.0);
        shopeeproductpic.setFitHeight(50.0);
        shopeeproductpic.setFitWidth(80.0);
        shopeeproductpic.setSmooth(true);
        shopeeproductpic.setPickOnBounds(true);
        shopeeproductpic.setPreserveRatio(true);
        
        productTitle = productItem.getProductName();
        
        if(productItem.getProductName().length() >= 43){
            productTitle = productItem.getProductName().substring(0, 45) + "..";
        }
        shopeeproductlink = new Hyperlink();
        shopeeproductlink.setFont(Font.font("System Bold", 12));
        shopeeproductlink.setText(productTitle);

        shopeeproductlink.setLayoutX(85.0);
        shopeeproductlink.setLayoutY(5.0);
        shopeeproductlink.setFont(Font.font("System Bold", 12));

        shopeeproductlink.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("disable-notifications");
                options.addArguments("--disable-infobars");
                options.addArguments("start-maximized");
                options.addArguments("disable-infobars");
                options.setHeadless(false);
                
                WebDriver driver = new ChromeDriver(options);
                Dimension driverDimensions = new Dimension(1920, 1080);
                driver.manage().window().setSize(driverDimensions);
                driver.manage().window().maximize();
                
                driver.get(productItem.getProductLink());
            }
        });
   
        shopeeproductrating.setLayoutX(90.0);
        shopeeproductrating.setLayoutY(25.0);

        shopeeproductratingvalue.setLayoutX(130.0);
        shopeeproductratingvalue.setLayoutY(25.0);

        shopeeproductprice.setLayoutX(90.0);
        shopeeproductprice.setLayoutY(40.0);

        shopeeproductpricevalue.setText("₱" + productItem.getProductPrice());
        shopeeproductpricevalue.setLayoutX(130.0);
        shopeeproductpricevalue.setLayoutY(40.0);
        
        shopeepinbutton.setGraphic(new ImageView(new Image("images/push_pin_32px.png")));
        // shopeepinbutton.setGraphic(shopeepinicon);
        shopeepinbutton.setLayoutX(385.0);
        shopeepinbutton.setLayoutY(8.0);
        shopeepinbutton.setMnemonicParsing(false);
        // shopeepinbutton.setOnAction(this);
       shopeepinbutton.setOnAction(new EventHandler<ActionEvent>() {

       public void handle(ActionEvent event) {
            System.out.println("You clicked pin button");
       }
       });
        shopeepinbutton.setPrefWidth(40.0);
        shopeepinbutton.setPrefHeight(40.0);
//        shopeePinButtons.add(shopeepinbutton);
//        someList.add("Hi" + Integer.toString(i));
        shopeeproductpane.getChildren().addAll(shopeeproductratingvalue,shopeeproductpricevalue,shopeeproductlink,shopeeproductrating,shopeeproductprice,shopeepinbutton,shopeeproductpic); 
                
        shopeepane.getChildren().addAll(shopeeproductpane);
        increment += 60.125; 
        }      
   
   }

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        launch(args);
//    }
    
     @Override
     public void handle(ActionEvent event) {
         if(event.getSource()==searchbutton){
             System.out.println("you pressed the search button");
                    amazonStartProduct = 1;
                    shopeeStartProduct = 1;
                    lazadaStartProduct = 1;
//                    lazadaSearcher.setStartProductNumberSearch(lazadaStartProduct);
//                    shopeeSearcher.setStartProductNumberSearch(shopeeStartProduct);
                    shopeeSearcher.setSearch(searchfield.getText());
                    lazadaSearcher.setSearch(searchfield.getText());
                    try {
                        Thread shopeeThread = new Thread(shopeeSearcher);
                        Thread lazadaThread = new Thread(lazadaSearcher);
                        
                        shopeeThread.start();
                        lazadaThread.start();
                        shopeeThread.join();
                        lazadaThread.join();
                        
                        generateLazadaResults();
                        generateShopeeResults();
                    }catch(Exception e){   
                        
                    }
         }
         
         if(event.getSource()==nextbuttonshopee){
            System.out.println("You clicked next button");
            shopeeStartProduct += 10;
            try {
                shopeeSearcher.setstartProductSearchNumber(shopeeStartProduct);
                Thread shopeeThread = new Thread(shopeeSearcher);
                shopeeThread.start();
                shopeeThread.join();
                        
                generateShopeeResults();
            }catch(Exception e){   
                        
            }
         }
         
         if(event.getSource()==prevbuttonshopee){
            System.out.println("You clicked previous button");
            if(shopeeStartProduct <= 1){
                return;
            }
            shopeeStartProduct -= 10;
            try {
                shopeeSearcher.setstartProductSearchNumber(shopeeStartProduct);
                Thread shopeeThread = new Thread(shopeeSearcher);
                shopeeThread.start();
                shopeeThread.join();
                        
                generateShopeeResults();
            }catch(Exception e){   
                        
            }
         }
         
         
     }
    
}
