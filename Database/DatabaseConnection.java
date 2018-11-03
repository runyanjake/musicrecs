/**
 * GenericDatabaseConnection.java
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

import Database.User;

public class DatabaseConnection {
    

    //Default connection that sets up user table and verifies filestructure.
    public DatabaseConnection(){
        //TODO: process userID and ensure it's legit otherwise fail.
        try{
            String databaseFolder = "./data";
            if(!Files.isDirectory(Paths.get(databaseFolder))){
                Files.createDirectory(Paths.get(databaseFolder));
            }
        }catch(IOException e){
            System.out.println("Failed to create data folder, filesystem not intact. Failing with error: " + e.getMessage());
            return;
        }

        Connection conn = null;
        String url = "jdbc:sqlite:./data/musicrecs.sqlite";
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.out.println("SQLite Error Caught Upon Connection: " + e.getMessage());
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFound Error Caught Upon Database Connection: " + e.getMessage());
        }finally{
            String sql = "CREATE TABLE IF NOT EXISTS USERS(uid integer,firstname TEXT,lastname TEXT,username TEXT PRIMARY KEY,password TEXT);";
            try{
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            } catch (SQLException e) {
                System.out.println("Failed to verify USERS table: " + e.getMessage());
                return;
            }
        }
        System.out.println("A new Database Connection abstraction was created.");
    }

    public User getUser(String username){
        Connection conn = null;
        String url = "jdbc:sqlite:./data/musicrecs.sqlite";
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.out.println("SQLite Error Caught Upon Connection: " + e.getMessage());
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFound Error Caught Upon Database Connection: " + e.getMessage());
        }
        String sql = "SELECT * FROM USERS u WHERE u.username = '" + username + "';";
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Executed SQL: " + sql);
            User usr = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
            return usr;
        } catch (SQLException e) {
            System.out.println("User did not exist: " + e.getMessage());
            return null;
        }
    }

    public boolean verifyUser(String user, String pass){
        Connection conn = null;
        String url = "jdbc:sqlite:./data/musicrecs.sqlite";
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.out.println("SQLite Error Caught Upon Connection: " + e.getMessage());
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFound Error Caught Upon Database Connection: " + e.getMessage());
        }
        String sql = "SELECT * FROM USERS u WHERE u.username = '" + user + "' AND u.password = '" + pass + "';";
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Executed SQL: " + sql);
            if(rs.next())
                return true;
            return false;
        } catch (SQLException e) {
            System.out.println("Failed to verify user " + user + ": " + e.getMessage());
            return false;
        }
    }

    public User createUser(String firstname, String lastname, String user, String pass){
        Connection conn = null;
        String url = "jdbc:sqlite:./data/musicrecs.sqlite";
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.out.println("SQLite Error Caught Upon Connection: " + e.getMessage());
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFound Error Caught Upon Database Connection: " + e.getMessage());
        }
        int new_uid;
        try{
            String sql = "SELECT COUNT(username) FROM USERS;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            new_uid = rs.getInt(1) + 1;
        }catch(SQLException e){
            System.out.println("Failed to query database: " + e.getMessage());
            return null;
        }

        String sql = "INSERT INTO USERS VALUES( " + new_uid + ",'" + firstname + "','" + lastname + "','" + user + "','" + pass + "');";
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Executed SQL: " + sql);
            return new User(new_uid, user, pass, firstname, lastname);
        } catch (SQLException e) {
            System.out.println("User " + user + " could not be created: " + e.getMessage());
            return null;
        }
    }

    public boolean addEntry(String recipientUsername, String rec_type, String artist, String album, String song, String link){
        Connection conn = null;
        String url = "jdbc:sqlite:./data/musicrecs.sqlite";
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.out.println("SQLite Error Caught Upon Connection: " + e.getMessage());
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFound Error Caught Upon Database Connection: " + e.getMessage());
        }
        User recipient = getUser(recipientUsername);
        if(recipient == null){
            return false;
        }
        String sql = "SELECT COUNT(id_num) FROM USER_" + recipientUsername + "_RECS;";
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int tableSize = rs.getInt(1);
            
            Date d = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy;HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = "" + dateFormat.format(d);

            sql = "INSERT INTO USER_" + recipientUsername + "_RECS VALUES (" + (tableSize+1) + ",'" + date + "','" + rec_type + "','" + artist + "','" + album + "','" + song + "','" + link + "');";
            boolean success = executeSQL(sql);
            if(!success) return false;
            System.out.println("Executed SQL: " + sql);
        } catch (SQLException e) {
            System.out.println("Failed to add entry to database for user " + recipientUsername + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean removeEntry(){

        return true;
    }

    public ResultSet getEntries(int userID){ //TODO: change to username
        String sql = "SELECT * FROM USER_" + userID + "_RECS;";
        return querySQL(sql);
    }

    private boolean executeSQL(String sql){
        Connection conn = null;
        String url = "jdbc:sqlite:./data/musicrecs.sqlite";
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.out.println("SQLite Error Caught Upon Connection: " + e.getMessage());
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFound Error Caught Upon Database Connection: " + e.getMessage());
        }
        if(conn == null){ return false; }
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create/verify database: " + e.getMessage());
            return false;
        }
        return true;
    }

    private ResultSet querySQL(String sql){
        Connection conn = null;
        String url = "jdbc:sqlite:./data/musicrecs.sqlite";
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.out.println("SQLite Error Caught Upon Connection: " + e.getMessage());
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFound Error Caught Upon Database Connection: " + e.getMessage());
        }
        if(conn == null) return null;
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs == null) return null;
            System.out.println("Executed SQL: " + sql);
            return rs;
        } catch (SQLException e) {
            System.out.println("Failed to add entry to database: " + e.getMessage());
            return null;
        }
    }

    public void closeConnection(){
        Connection conn = null;
        String url = "jdbc:sqlite:./data/musicrecs.sqlite";
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.out.println("SQLite Error Caught Upon Connection: " + e.getMessage());
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFound Error Caught Upon Database Connection: " + e.getMessage());
        }
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
