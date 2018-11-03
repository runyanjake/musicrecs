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

import Database.DatabaseConnection;
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
    JTextField suggestion_recipient;
    JButton suggestion_cancel;
    JButton suggestion_submit;
    JTextField suggestion_artist;
    JTextField suggestion_album;
    JTextField suggestion_song;
    JTextField suggestion_link;

    DatabaseConnection dc;

    String APP_TITLE = "MusicRecs by Jake Runyan";
    
    String LOGIN_BUTTON_TEXT = "Log In";
    String LOGOUT_BUTTON_TEXT = "Log Out";
    String SIGNUP_BUTTON_TEXT = "Sign Up";
    String SUBMIT_BUTTON_TEXT = "Submit";
    String MAKE_SUGGESTION_BUTTON_TEXT = "Make Suggestion";
    String MAKE_ANONYMOUS_SUGGESTION_BUTTON_TEXT = "Make Anonymous Suggestion";
    String CANCEL_BUTTON_TEXT = "Cancel";

    String USERNAME_BOX_DEFAULT_TEXT = "Username";
    String PASSWORD_BOX_DEFAULT_TEXT = "Password";
    String PASSWORD_VERIFY_BOX_DEFAULT_TEXT = "Verify Password";
    String FIRSTNAME_BOX_DEFAULT_TEXT = "First Name";
    String LASTNAME_BOX_DEFAULT_TEXT = "Last Name";
    String ARTIST_BOX_DEFAULT_TEXT = "Artist";
    String ALBUM_BOX_DEFAULT_TEXT = "Album";
    String SONG_BOX_DEFAULT_TEXT = "Song";
    String LINK_BOX_DEFAULT_TEXT = "Link";
    String RECIPIENT_USERNAME_BOX_DEFAULT_TEXT = "Recipient's Username";

    public MRGraphics(){
        frame = new JFrame(APP_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(200,200));
        JFrame.setDefaultLookAndFeelDecorated(true);

        JPanel landing_page = new JPanel();
        JPanel login_page = new JPanel();
        JPanel signup_page = new JPanel();
        JPanel home_page = new JPanel();
        JPanel suggestion_page = new JPanel();

        //Landing Page
        JButton landing_anonymous = new JButton("Leave Anonymous Suggestion");
        JButton landing_signup = new JButton(SIGNUP_BUTTON_TEXT);
        JButton landing_login = new JButton(LOGIN_BUTTON_TEXT);
        landing_anonymous.addActionListener(new gotoSuggestionListener());
        landing_signup.addActionListener(new gotoSignupListener());
        landing_login.addActionListener(new gotoLoginListener());
        landing_page.add(landing_anonymous);
        landing_page.add(landing_login);
        landing_page.add(landing_signup);

        //Login Page
        JButton login_goto_signup = new JButton(SIGNUP_BUTTON_TEXT);
        JButton login_submit = new JButton(LOGIN_BUTTON_TEXT);
        login_user = new JTextField(USERNAME_BOX_DEFAULT_TEXT);
        login_pass = new JPasswordField(PASSWORD_BOX_DEFAULT_TEXT);
        login_goto_signup.addActionListener(new gotoSignupListener());
        login_submit.addActionListener(new LoginActionListener());
        login_page.add(login_user);
        login_page.add(login_pass);
        login_page.add(login_submit);
        login_page.add(login_goto_signup);

        //Signup Page
        JButton signup_goto_login = new JButton(LOGIN_BUTTON_TEXT);
        JButton signup_submit = new JButton(SIGNUP_BUTTON_TEXT);
        signup_fn = new JTextField(FIRSTNAME_BOX_DEFAULT_TEXT);
        signup_ln = new JTextField(LASTNAME_BOX_DEFAULT_TEXT);
        signup_user = new JTextField(USERNAME_BOX_DEFAULT_TEXT);
        signup_pass = new JTextField(PASSWORD_BOX_DEFAULT_TEXT);
        signup_pass_verify = new JTextField(PASSWORD_VERIFY_BOX_DEFAULT_TEXT);
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
        JButton home_logout = new JButton(LOGOUT_BUTTON_TEXT);
        JButton home_goto_suggestion = new JButton(MAKE_SUGGESTION_BUTTON_TEXT);
        
        home_goto_suggestion.addActionListener(new gotoSuggestionListener());
        home_logout.addActionListener(new LogoutActionListener());
        
        home_page.add(home_goto_suggestion);
        home_page.add(home_logout);

        //Suggestion Page
        String[] rec_types = {"Song", "Album", "Artist", "Playlist"};
        suggestion_rec_type = new JComboBox(rec_types);
        suggestion_recipient = new JTextField(RECIPIENT_USERNAME_BOX_DEFAULT_TEXT);
        suggestion_cancel = new JButton(CANCEL_BUTTON_TEXT);
        suggestion_artist = new JTextField(ARTIST_BOX_DEFAULT_TEXT);
        suggestion_album = new JTextField(ALBUM_BOX_DEFAULT_TEXT);
        suggestion_song = new JTextField(SONG_BOX_DEFAULT_TEXT);
        suggestion_link = new JTextField(LINK_BOX_DEFAULT_TEXT);
        suggestion_submit = new JButton(SUBMIT_BUTTON_TEXT);
        suggestion_submit.addActionListener(new submitRecActionListener());
        suggestion_cancel.addActionListener(new gotoHomeListener());
        suggestion_page.add(suggestion_recipient);
        suggestion_page.add(suggestion_rec_type);
        suggestion_page.add(suggestion_artist);
        suggestion_page.add(suggestion_album);
        suggestion_page.add(suggestion_song);
        suggestion_page.add(suggestion_link);
        suggestion_page.add(suggestion_cancel);
        suggestion_page.add(suggestion_submit);
        if(user == null){
            JButton suggestion_goto_login = new JButton(LOGIN_BUTTON_TEXT);
            JButton suggestion_goto_signup = new JButton(SIGNUP_BUTTON_TEXT);
            suggestion_goto_login.addActionListener(new gotoLoginListener());
            suggestion_goto_signup.addActionListener(new gotoSignupListener());
            suggestion_page.add(suggestion_goto_login);
            suggestion_page.add(suggestion_goto_signup);
        }
        

        window = new JPanel(new CardLayout());
        window.add(landing_page, "landing");
        window.add(login_page, "login");
        window.add(home_page, "home");
        window.add(suggestion_page, "recs");
        window.add(signup_page, "signup");
        window.add(suggestion_page, "suggestion");
        frame.add(window);

        dc = new DatabaseConnection(-1);
        user = null;

        frame.pack();
        frame.setVisible(true);
    }

    private void resetLoginPage(){
        login_user.setText(USERNAME_BOX_DEFAULT_TEXT);
        login_pass.setText(PASSWORD_BOX_DEFAULT_TEXT);
    }

    private void resetSignupPage(){
        signup_fn.setText(FIRSTNAME_BOX_DEFAULT_TEXT);
        signup_ln.setText(LASTNAME_BOX_DEFAULT_TEXT);
        signup_user.setText(USERNAME_BOX_DEFAULT_TEXT);
        signup_pass.setText(PASSWORD_BOX_DEFAULT_TEXT);
        signup_pass_verify.setText(PASSWORD_VERIFY_BOX_DEFAULT_TEXT);
    }

    private void resetHomePage(){
        //nothing yet(should reset user values based on stored user instance)
    }

    private void resetSuggestionPage(){
        suggestion_artist.setText(ARTIST_BOX_DEFAULT_TEXT);
        suggestion_album.setText(ALBUM_BOX_DEFAULT_TEXT);
        suggestion_song.setText(SONG_BOX_DEFAULT_TEXT);
        suggestion_link.setText(LINK_BOX_DEFAULT_TEXT);
        suggestion_recipient.setText(RECIPIENT_USERNAME_BOX_DEFAULT_TEXT);
    }

    private boolean login(){
        if(user != null){
            System.out.println("ERROR: Bad Flow, a user is already logged in.");
        }
        String form_user = login_user.getText();
        String form_pass = new String(login_pass.getPassword());
        try{
            ResultSet rs = dc.verifyUser(form_user,form_pass);
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
                    dc.setUser(user.getUID());
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

    private void logout(){
        dc.setUser(-1);;
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
                resetHomePage();
                CardLayout layout_ref = (CardLayout)window.getLayout();
                frame.setTitle(user.getFirstName() + "'s Home");
                layout_ref.show(window, "home");
            }
        }
    }

    private class SignupActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            if(!signup_pass.getText().equals(signup_pass_verify.getText())){ return; }
            int new_user_id = dc.createUser(signup_fn.getText(), signup_ln.getText(), signup_user.getText(), signup_pass.getText());
            user = dc.getUser(new_user_id);
            if(new_user_id > 0){
                resetHomePage();
                CardLayout layout_ref = (CardLayout)window.getLayout();
                frame.setTitle(user.getFirstName() + "'s Home");
                layout_ref.show(window, "home");
            }
        }
    }

    //attempts to submit a recommendation 
    private class submitRecActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            System.out.println("Trynna submit a thang.");
            if(user == null){
                //submitting as anonymous
            }else{
                int validRecipient = dc.userExists(suggestion_recipient.getText());
                System.out.println("User " + suggestion_recipient.getText() + " valid? : " + validRecipient);
                if(validRecipient != -1){
                    dc.addEntry(suggestion_recipient.getText(),(String)suggestion_rec_type.getSelectedItem(), suggestion_artist.getText(), suggestion_album.getText(), suggestion_song.getText(), suggestion_link.getText());
                }else{
                    //do something when user doesn't exist
                }

            }
        }
    }

    //moves to signup page when invoked
    private class gotoSignupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            resetSignupPage();
            CardLayout layout_ref = (CardLayout)window.getLayout();
            frame.setTitle("Sign Up");
            layout_ref.show(window, "signup");
        }
    }

    //moves to login page when invoked
    private class gotoLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            resetLoginPage();
            CardLayout layout_ref = (CardLayout)window.getLayout();
            frame.setTitle("Login");
            layout_ref.show(window, "login");
        }
    }

    //moves to create-a-suggestion page when invoked
    private class gotoSuggestionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            resetSuggestionPage();
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
                resetHomePage();
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