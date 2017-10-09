package uz.music.capstone.authentication;

/**
 * Created by Nemo on 10/7/2017.
 */

public class User {
    private String name;
    private String mail;
    private String password;
    private String password_confirm;

    public static final int TYPE_LOGIN = 1, TYPE_CREATE = 2, TYPE_FORGET = 3, TYPE_RESET = 4, TYPE_CHANGE = 5;
    public static final String KEY_TYPE = "type_call", KEY_USERNAME = "username", KEY_EMAIL = "email",
                            KEY_PASSWORD1 = "password1", KEY_PASSWORD2 = "password2";

    public User(String name, String mail, String password, String password_confirm) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.password_confirm = password_confirm;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword_confirm() {
        return password_confirm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPassword_confirm(String password_confirm) {
        this.password_confirm = password_confirm;
    }
}
