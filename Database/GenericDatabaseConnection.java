/**
 * MusicRecs.java
 * @author Jake Runyan
 * https://github.com/runyanjake/musicrecs/
 * Represents a user's connection to their corresponding database table of recommendations.
 * An instance should be created/destroyed as the user logs in/out.
 */

package Database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Date;
import java.util.TimeZone;

import java.text.SimpleDateFormat;

public class GenericDatabaseConnection {
    Connection conn;

    //Default connection that sets up user table and verifies filestructure.
    public GenericDatabaseConnection(){
        try{
            String databaseFolder = "./data";
            if(!Files.isDirectory(Paths.get(databaseFolder))){
                Files.createDirectory(Paths.get(databaseFolder));
            }
        }catch(IOException e){
            System.out.println("Failed to create data folder, filesystem not intact. Failing with error: " + e.getMessage());
            return;
        }

        try{
            Class.forName("org.sqlite.JDBC");
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFound Error Caught Upon Database Connection: " + e.getMessage());
        }

        String url = "jdbc:sqlite:./data/musicrecs.sqlite";
        try{
            if(conn == null)
                conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.out.println("SQLite Error Caught Upon Connection: " + e.getMessage());
        }finally{
            String sql = "CREATE TABLE IF NOT EXISTS USERS(uid integer,firstname TEXT,lastname TEXT,username TEXT PRIMARY_KEY,password TEXT);";
            try{
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            } catch (SQLException e) {
                System.out.println("Failed to create user table: " + e.getMessage());
                return;
            }
        }

    }

    public ResultSet verifyUser(String user, String pass){
        return null;
    }

    public boolean createUser(){
        return false;
    }

    public void closeConnection(){
        /*Note: should be called before deletion/calling finalize()*/
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("SQLite Error Caught Upon Closure: " + ex.getMessage());
        }
    }
}
