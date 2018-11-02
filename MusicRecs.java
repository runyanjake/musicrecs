/**
 * MusicRecs.java
 * @author Jake Runyan
 * https://github.com/runyanjake/musicrecs/
 */

import Database.DatabaseConnection;
import MRGraphics.MRGraphics;
import java.sql.ResultSet;
import java.sql.SQLException;

class MusicRecs {
    public static void main(String args[]){
        // System.out.println("Hello World");
        // int userID = 1;
        // DatabaseConnection c = new DatabaseConnection(userID);
        // c.addEntry("song", "Tobee The Cat", "May May", "All My Mays", "spoti.fy/2bcafd4abfbfdbfd4bf4dbf4dbf22fbdf3de");
        // try{
        //     ResultSet rs = c.getEntries();
        //     System.out.println("User " + userID + "'s Recommendation Table:");
        //     while(rs.next()){
        //         System.out.println("[" + rs.getInt(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4) + ", " + rs.getString(5) + ", " + rs.getString(6) + ", " + rs.getString(7) + "]");
        //     }
        // }catch(SQLException e){
        //     System.out.println("Caught SQLException during query for user " + userID + ": " + e.getMessage());
        // }
        
        MRGraphics app = new MRGraphics();
    }
}
