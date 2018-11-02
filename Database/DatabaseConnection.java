/**
 * MusicRecs.java
 * @author Jake Runyan
 * https://github.com/runyanjake/musicrecs/
 * Represents a user's connection to their corresponding database table of recommendations.
 * An instance should be created/destroyed as the user logs in/out.
 */

package Database;

import java.sql.*;
import java.util.Date;
import java.util.TimeZone;

import java.text.SimpleDateFormat;

public class DatabaseConnection {
    Connection conn;
    int userID;
    public DatabaseConnection(int uid){
        //TODO: process userID and ensure it's legit otherwise fail.
        userID = uid;
        try{
            Class.forName("org.sqlite.JDBC");
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFound Error Caught Upon Database Connection: " + e.getMessage());
        }
        String url = "jdbc:sqlite:./data/musicrecs.sqlite";
        try{
            conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.out.println("SQLite Error Caught Upon Connection: " + e.getMessage());
        }finally{
            //table setup
            String sql = "CREATE TABLE IF NOT EXISTS USER_" + userID + "_RECS(id_num integer PRIMARY_KEY,date_added TEXT PRIMARY_KEY,rec_type TEXT,artist TEXT,album TEXT,song TEXT,link TEXT);";
            try{
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            } catch (SQLException e) {
                System.out.println("Failed to create/verify database for user " + userID + ": " + e.getMessage());
            }
        }
        System.out.println("A new Database Connection abstraction was created for user " + userID + ".");
    }

    public boolean addEntry(String rec_type, String artist, String album, String song, String link){
        String sql = "SELECT COUNT(id_num) FROM USER_" + userID + "_RECS;";
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int tableSize = rs.getInt(1);
            
            Date d = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy;HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = "" + dateFormat.format(d);

            sql = "INSERT INTO USER_" + userID + "_RECS VALUES (" + (tableSize+1) + ",'" + date + "','" + rec_type + "','" + artist + "','" + album + "','" + song + "','" + link + "');";
            boolean success = executeSQL(sql);
            if(!success) return false;
            System.out.println("Executed SQL: " + sql);
        } catch (SQLException e) {
            System.out.println("Failed to add entry to database for user " + userID + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean removeEntry(){

        return true;
    }

    public ResultSet getEntries(){
        String sql = "SELECT * FROM USER_" + userID + "_RECS;";
        return querySQL(sql);
    }

    private boolean executeSQL(String sql){
        if(conn == null){ return false; }
        try{
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create/verify database for user " + userID + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    private ResultSet querySQL(String sql){
        if(conn == null) return null;
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs == null) return null;
            System.out.println("Executed SQL: " + sql);
            return rs;
        } catch (SQLException e) {
            System.out.println("Failed to add entry to database for user " + userID + ": " + e.getMessage());
            return null;
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
