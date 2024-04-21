
/**
 *
 * @author Pololoers
 */
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
public class Fns {
    private static UserAuth userAuth = new UserAuth();
    
    public static int registerUser(String name, String pass){
        /*
        1 = success
        2 = if name is too short
        3 = if password is too short
        */
        if (name.length() < 4) {
        return 2; // Username is too short
        }
        if (pass.length() < 8) {
            return 3; // Password is too short
        }
        if (userAuth.usernameExists(name)) {
            return 4; // Username already exists
        }

        // Register user
        if (userAuth.createUser(name, pass)) {
            return 1; // Success
        } else {
            return 0; // Error occurred during registration
        }
    }
    public static boolean validLogin(String name, String pass){
        return userAuth.validateLogin(name, pass);
    }
    public static void btnLogin(String name, String pass, JFrame frm){
        /*
        sample execution in button action:
        Fns.btnLogin(jTextField1.getText(), jPasswordField1.getText(), this);
        */
        if(Fns.validLogin(name, pass)){
            JOptionPane.showMessageDialog(frm, "Login success!");
        }else{
            JOptionPane.showMessageDialog(frm, "Login failed.");
        }
    }
    public static void btnRegister(String name, String pass, JFrame frm){
        int reg = registerUser(name, pass);
        switch(reg){
            case 1: JOptionPane.showMessageDialog(frm, "Register success!");break;
            case 2: JOptionPane.showMessageDialog(frm, "Username should be atleast 4 characters.");break;
            case 3: JOptionPane.showMessageDialog(frm, "Password should be atleast 8 characters.");break;
            case 4: JOptionPane.showMessageDialog(frm, "Username already exists.");break;

            default: JOptionPane.showConfirmDialog(frm, "bruh");
        }
    }
}
