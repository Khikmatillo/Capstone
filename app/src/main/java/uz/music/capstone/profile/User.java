package uz.music.capstone.profile;

import java.util.ArrayList;

/**
 * Created by Nemo on 10/7/2017.
 */



public class User {

    private String name;
    private String mail;
    private String password;
    private String password_confirm;

    private String city;
    private String country;
    private ArrayList<User> followers;
    private ArrayList<User> following;
    private String about;
    private String info;
    private byte[] image;


    public static final int TYPE_LOGIN = 1, TYPE_CREATE = 2, TYPE_FORGET = 3, TYPE_RESET = 4, TYPE_CHANGE = 5;
    public static final String KEY_TYPE = "type_call", KEY_USERNAME = "username", KEY_EMAIL = "email",
                            KEY_PASSWORD1 = "password1", KEY_PASSWORD2 = "password2";

    public User(String name, String mail, String password, String password_confirm) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.password_confirm = password_confirm;
        followers = new ArrayList<User>();
        following = new ArrayList<User>();
    }

    public User(String name, String mail, String password, String password_confirm, String city,
                String country, ArrayList<User> followers, ArrayList<User> following, String about, String info, byte[] image) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.password_confirm = password_confirm;
        this.city = city;
        this.country = country;
        this.followers = followers;
        this.following = following;
        this.about = about;
        this.info = info;
        this.image = image;
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

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }

    public ArrayList<User> getFollowing() {
        return following;
    }

    public String getAbout() {
        return about;
    }

    public String getInfo() {
        return info;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void addFollower(User follower) {
        followers.add(follower);
    }

    public void addFollowing(User user) {
        this.following.add(user);
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
