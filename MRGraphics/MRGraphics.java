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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Database.GenericDatabaseConnection;
import Database.UserDatabaseConnection;

public class MRGraphics {
    JFrame frame;
    JPanel window;

    JButton login_submit;
    JTextField login_user;
    JPasswordField login_pass;



    GenericDatabaseConnection gdc;
    UserDatabaseConnection udc;

    public MRGraphics(){
        frame = new JFrame("MusicRecs by Jake Runyan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(200,200));
        JFrame.setDefaultLookAndFeelDecorated(true);

        JPanel login_page = new JPanel();
        JPanel home_page = new JPanel();
        JPanel reccomendation_page = new JPanel();

        login_submit = new JButton("Login");
        login_user = new JTextField("Username");
        login_pass = new JPasswordField("Password");

        login_submit.addActionListener(new LoginSubmitListener());
        login_page.add(login_user);
        login_page.add(login_pass);
        login_page.add(login_submit);

        window = new JPanel(new CardLayout());
        window.add(login_page, "Log In");
        window.add(home_page, "Home");
        window.add(reccomendation_page, "");
        frame.add(window);

        gdc = new GenericDatabaseConnection();
        udc = null;

        frame.pack();
        frame.setVisible(true);
    }

    private class LoginSubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae){
            String user = login_user.getText();
            String pass = new String(login_pass.getPassword());
            try{
                ResultSet rs = gdc.verifyUser(user,pass);
                String pw = null;
                while(rs.next()){
                    pw = rs.getString(5);
                }
                if(pw == null){
                    System.out.println("User does not exist, popping up an alert for incorrect login. (TODO).");
                    //TODO: Throw some popup for incorrect login
                }else{   
                    System.out.println("Correct PW: " + pw + " Supplied PW: " + pass);
                    if (pw.equals(pass)){
                        System.out.println("User is verified, logging them in (TODO).");
                        //TODO: Login and swap card
                    }else{
                        System.out.println("User is not verified (provided incorrect password), popping up an alert for incorrect login. (TODO).");
                        //TODO: Throw some popup for incorrect login
                    }
                }   
            }catch(SQLException e){
                System.out.println("Failed to check user against database: " + e.getMessage());
            }
        }
    }
}