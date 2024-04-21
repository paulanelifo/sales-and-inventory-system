//made by paul 42 and R


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Pololoers
 */
import java.sql.*;
public class Main {
    public static Connection getConnection(){
        try {
                // Connect to the SQLite database
                Connection connection = DriverManager.getConnection("jdbc:sqlite:myDb.db");
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
    }
    public static void main(String[] args){
        /*
        //CREATE ADMIN
        UserAuth userAuth = new UserAuth();
        userAuth.createUser("admin", "1234");
        */
        frmAuth.frmAuth();
    }
}
