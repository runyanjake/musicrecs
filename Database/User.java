/**
 * User.java
 * @author Jake Runyan
 * https://github.com/runyanjake/musicrecs/
 * Abstracts a User object.
 */

package Database;

public class User {
    int uid;
    String username, password, firstname, lastname;
    public User(int id, String u, String p, String fn, String ln){
        uid = id;
        username = u;
        password = p;
        firstname = fn;
        lastname = ln;
    }
    public int getUID(){ return uid; }
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public String getFirstName(){ return firstname; }
    public String getLastName(){ return lastname; }
    
}