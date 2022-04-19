/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressoapp;

import java.io.IOException;
// import java.sql.Connection;
// import java.sql.DriverManager;
import java.sql.Statement.*;

import java.sql.*;

/**
 *
 * @author Chris
 */
public class Main {
    
    static boolean loggedIn = false;
    static boolean accountExistsVar = false;
    private static final String databasePath = "jdbc:mysql://localhost:3306/scrappr";
    private static final String databaseUsername = "root";
    private static final String databasePassword = "NewPassword";
    
    public static void main(String[]args)throws Exception{
//       System.setProperty("webdriver.chrome.driver","C:/Users/Chris/Documents/VSCodeJavaProjects/chromedriver_win32/chromedriver.exe");
        
//        System.out.println(accountExists("chrissamuel231@gmail.com","password123"));

//        LazadaSearcher lazadaSearcher = new LazadaSearcher();
//        lazadaSearcher.setSearch("mouse");
//        new Thread(lazadaSearcher).start();

        // shopeeSearcher.start();
           
        // String query = "UPDATE scrappr.customers SET customer_email = 'chrissamuel231@gmail.com' WHERE customer_id = 1";
        // // String query = "INSERT INTO scrappr.customers(customer_email,customer_pass) VALUES (\"chrismargate231@gmail.com\",\"password123\")";
        // String username = "root";
        // String password = "NewPassword";
        // String url = "jdbc:mysql://localhost:3306/scrappr";

        // try {
        //     Connection connection = DriverManager.getConnection(url,username,password);
            
        //     System.out.println("Connected");

        //     Statement statement = connection.createStatement();
            
        //     int numberOfRows = statement.executeUpdate(query);

        //     System.out.println(numberOfRows);
        //     // stmt.executeQuery(query);

        // }
        // catch(Exception e){
        //     System.out.println(e);
        // }
        
   

        //  shopeeSearcher.start();
        
        
//        new Thread(lazadaSearcher).start();

        // for(Product productItem: ShopeeSearcher.getProductsList()){
        //     System.out.println(productItem.getProductLink());
        //     System.out.println(productItem.getProductName());
        //     System.out.println(productItem.getProductPrice());
        //     System.out.println(productItem.getProductRating());
        // }

//        AmazonSearcher amazonSearcher = new AmazonSearcher(1,10);
//        amazonSearcher.search("mouse");
        
    //    LazadaSearcher lazadaSearcher = new LazadaSearcher(1,10);
    //    lazadaSearcher.search("monitor");
        
    }
    
    public static boolean accountExists(String enteredUsername,String enteredPassword)throws SQLException,ClassNotFoundException{
      
        Connection connection = DriverManager.getConnection(databasePath,databaseUsername,databasePassword);
        Statement statement = connection.createStatement();
        final String query = "SELECT * FROM customers;";
        ResultSet resultSet = statement.executeQuery(query);
        
        while(resultSet.next()){
            
            System.out.println(resultSet.getString("customer_id"));
            System.out.println(resultSet.getString("customer_email"));
            System.out.println(resultSet.getString("customer_pass"));
            System.out.println();
            if(enteredUsername.equals(resultSet.getString("customer_email"))){
                if(enteredPassword.equals(resultSet.getString("customer_pass"))){
                    return true;
                }
            }
            }
        return false;
    }
}
