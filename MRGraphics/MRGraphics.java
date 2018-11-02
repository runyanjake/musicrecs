/**
 * MRGraphics.java
 * @author Jake Runyan
 * https://github.com/runyanjake/musicrecs/
 * Instantiates a GUI based on the Java graphics library.
 */

package MRGraphics;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Database.GenericDatabaseConnection;
import Database.UserDatabaseConnection;
import Database.User;

public class MRGraphics {
    User user;

    JFrame frame;
    JPanel window;

    JTextField login_user;
    JPasswordField login_pass;

    JTextField signup_fn;
    JTextField signup_ln;
    JTextField signup_user;
    JTextField signup_pass;
    JTextField signup_pass_verify;

    
    JComboBox suggestion_rec_type;
    JButton suggestion_cancel;
    JTextField suggestion_artist;
    JTextField suggestion_album;
    JTextField suggestion_song;
    JTextField suggestion_link;

    GenericDatabaseConnection gdc;
    UserDatabaseConnection udc;

    String APP_TITLE = "MusicRecs by Jake Runyan";

    public MRGraphics(){
        frame = new JFrame(APP_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(200,200));
        JFrame.setDefaultLookAndFeelDecorated(true);

        JPanel landing_page = new JPanel();
        JPanel login_page = new JPanel();
        JPanel signup_page = new JPanel();
        JPanel home_page = new JPanel();
        JPanel reccomendation_page = new JPanel();

        //Landing Page
        JButton landing_anonymous = new JButton("Leave Anonymous Suggestion");
        JButton landing_signup = new JButton("Sign Up");
        JButton landing_login = new JButton("Login");
        landing_anonymous.addActionListener(new gotoSuggestionListener());
        landing_signup.addActionListener(new gotoSignupListener());
        landing_login.addActionListener(new gotoLoginListener());
        landing_page.add(landing_anonymous);
        landing_page.add(landing_login);
        landing_page.add(landing_signup);

        //Login Page
        JButton login_goto_signup = new JButton("Sign Up");
        JButton login_submit = new JButton("Login");
        login_user = new JTextField("Username");
        login_pass = new JPasswordField("Password");
        login_goto_signup.addActionListener(new gotoSignupListener());
        login_submit.addActionListener(new LoginActionListener());
        login_page.add(login_user);
        login_page.add(login_pass);
        login_page.add(login_submit);
        login_page.add(login_goto_signup);

        //Signup Page
        JButton signup_goto_login = new JButton("Login");
        JButton signup_submit = new JButton("Sign Up");
        signup_fn = new JTextField("First Name");
        signup_ln = new JTextField("Last Name");
        signup_user = new JTextField("Username");
        signup_pass = new JTextField("Password");
        signup_pass_verify = new JTextField("Verify Password");
        signup_goto_login.addActionListener(new gotoLoginListener());
        signup_submit.addActionListener(new SignupActionListener());
        signup_page.add(signup_fn);
        signup_page.add(signup_ln);
        signup_page.add(signup_user);
        signup_page.add(signup_pass);
        signup_page.add(signup_pass_verify);
        signup_page.add(signup_submit);
        signup_page.add(signup_goto_login);

        //Home Page
        JButton home_logout = new JButton("Log Out");
        JButton home_goto_suggestion = new JButton("Make a Suggestion");
        
        home_goto_suggestion.addActionListener(new gotoSuggestionListener());
        home_logout.addActionListener(new LogoutActionListener());
        
        home_page.add(home_goto_suggestion);
        home_page.add(home_logout);

        //Suggestion Page
        String[] rec_types = {"Song", "Album", "Artist", "Playlist"};
        suggestion_rec_type = new JComboBox(rec_types);
        suggestion_cancel = new JButton("Cancel");
        suggestion_artist = new JTextField("Artist");
        suggestion_album = new JTextField("Album");
        suggestion_song = new JTextField("Song");
        suggestion_link = new JTextField("Link");
        suggestion_cancel.addActionListener(new gotoHomeListener());
        reccomendation_page.add(suggestion_rec_type);
        reccomendation_page.add(suggestion_artist);
        reccomendation_page.add(suggestion_album);
        reccomendation_page.add(suggestion_song);
        reccomendation_page.add(suggestion_link);
        reccomendation_page.add(suggestion_cancel);
        if(user == null){
            JButton suggestion_goto_login = new JButton("Login");
            JButton suggestion_goto_signup = new JButton("Sign Up");
            suggestion_goto_login.addActionListener(new gotoLoginListener());
            suggestion_goto_signup.addActionListener(new gotoSignupListener());
            reccomendation_page.add(suggestion_goto_login);
            reccomendation_page.add(suggestion_goto_signup);
        }
        

        window = new JPanel(new CardLayout());
        window.add(landing_page, "landing");
        window.add(login_page, "login");
        window.add(home_page, "home");
        window.add(reccomendation_page, "recs");
        window.add(signup_page, "signup");
        window.add(reccomendation_page, "suggestion");
        frame.add(window);

        gdc = new GenericDatabaseConnection();
        udc = null;
        user = null;

        frame.pack();
        frame.setVisible(true);
    }

    public boolean login(){
        if(udc != null){
            System.out.println("ERROR: Bad Flow, a user is already logged in.");
        }
        String form_user = login_user.getText();
        String form_pass = new String(login_pass.getPassword());
        try{
            ResultSet rs = gdc.verifyUser(form_user,form_pass);
            int uid = -1;
            String fn = null;
            String ln = null;
            String usr = null;
            String pw = null;
            while(rs.next()){ uid = rs.getInt(1);
                              fn = rs.getString(2);
                              ln = rs.getString(3);
                              usr = rs.getString(4);
                              pw = rs.getString(5); }
            if(pw == null){
                System.out.println("User does not exist, popping up an alert for incorrect login. (TODO).");
                //TODO: Throw some popup for incorrect login
                return false;
            }else{
                if (pw.equals(form_pass)){
                    System.out.println("User is verified, logging them in (TODO).");
                    //TODO: Login and swap card
                    user = new User(uid,fn,ln,usr,pw);
                    udc = new UserDatabaseConnection(user.getUID());
                    return true;
                }else{
                    System.out.println("User is not verified (provided incorrect password), popping up an alert for incorrect login. (TODO).");
                    //TODO: Throw some popup for incorrect login
                    return false;
                }
            }   
        }catch(SQLException e){
            System.out.println("Failed to check user against database: " + e.getMessage());
            return false;
        }
    }

    public void logout(){
        udc = null;
        user = null;
    }
    
    private class LogoutActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            logout();
            CardLayout layout_ref = (CardLayout)window.getLayout();
            frame.setTitle(APP_TITLE);
            layout_ref.show(window, "landing");
        }
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            logout(); //just in case
            boolean success = login();
            if(success){
                CardLayout layout_ref = (CardLayout)window.getLayout();
                frame.setTitle(user.getFirstName() + "'s Home");
                layout_ref.show(window, "home");
            }
        }
    }

    private class SignupActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            //TODO
        }
    }

    //moves to signup page when invoked
    private class gotoSignupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            CardLayout layout_ref = (CardLayout)window.getLayout();
            frame.setTitle("Sign Up");
            layout_ref.show(window, "signup");
        }
    }

    //moves to login page when invoked
    private class gotoLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            CardLayout layout_ref = (CardLayout)window.getLayout();
            frame.setTitle("Login");
            layout_ref.show(window, "login");
        }
    }

    //moves to create-a-suggestion page when invoked
    private class gotoSuggestionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            CardLayout layout_ref = (CardLayout)window.getLayout();
            frame.setTitle("Submit a Suggestion");
            layout_ref.show(window, "suggestion");
        }
    }

    //moves to home page when USER LOGGED IN & invoked, 
    //moves to landing page when USER NOT LOGGED IN & invoked
    private class gotoHomeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            if(user != null){
                CardLayout layout_ref = (CardLayout)window.getLayout();
                frame.setTitle(user.getFirstName() + "'s Home");
                layout_ref.show(window, "home");
            }else{
                CardLayout layout_ref = (CardLayout)window.getLayout();
                frame.setTitle(APP_TITLE);
                layout_ref.show(window, "landing");
            }
        }
    }
}