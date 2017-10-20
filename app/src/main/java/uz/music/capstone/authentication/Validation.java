package uz.music.capstone.authentication;

/**
 * Created by Nemo on 10/20/2017.
 */

public class Validation {
    //client side validation starts -------------------------------
    public static boolean validateUserName(String username){
        if(username == null || username.length() <= 4){
            return false;
        }else {
            return true;
        }
    }

    public static boolean validateEmail(String email){
        if(email == null)
            return false;
        String regex = "^(.+)@(.+)$";
        return email.matches(regex);
    }

    public static boolean validatePassword(String password){
        if(password == null)
            return false;
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }

    public static boolean validateConfirmPassword(String p1, String p2){
        if(p1 == null || p2 == null)
            return false;
        if(p1.equals(p2)){
            return true;
        }
        return false;
    }
    //client side validation ends -------------------------------

}
