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
            String sql = "CREATE TABLE IF NOT EXISTS USERS(uid integer,firstname TEXT,lastname TEXT,username TEXT PRIMARY KEY,password TEXT);";
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
        String sql = "SELECT * FROM USERS u WHERE u.username = '" + user + "';";
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Executed SQL: " + sql);
            return rs;
        } catch (SQLException e) {
            System.out.println("Failed to verify user " + user + ": " + e.getMessage());
            return null;
        }
    }

    public boolean createUser(String firstname, String lastname, String user, String pass){
        int new_uid;
        try{
            String sql = "SELECT COUNT(username) FROM USERS;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            new_uid = rs.getInt(1) + 1;
        }catch(SQLException e){
            System.out.println("Failed to query database: " + e.getMessage());
            return false;
        }

        String sql = "INSERT INTO USERS VALUES( " + new_uid + ",'" + firstname + "','" + lastname + "','" + user + "','" + pass + "');";
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Executed SQL: " + sql);
            return true;
        } catch (SQLException e) {
            System.out.println("User " + user + " could not be created: " + e.getMessage());
            return false;
        }
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
