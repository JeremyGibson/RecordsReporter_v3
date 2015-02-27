package LoginMod.classes;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;

/**
 * Created by jgibson on 2/9/2015.
 */
public class User {
    private StringProperty first_name;
    private StringProperty last_name;
    private StringProperty email;
    private StringProperty password;
    private IntegerProperty uid;
    private IntegerProperty role;

    private HashMap<String, Integer> other_users = new HashMap<>(10);


    public User() {
        initializeProperties();
    }

    public User(String f_name, String l_name, String email) {
        initializeProperties();
        first_name.set(f_name);
        last_name.set(l_name);
        this.email.set(email);
    }

    private void initializeProperties() {
        first_name = new SimpleStringProperty();
        last_name = new SimpleStringProperty();
        email = new SimpleStringProperty();
        password = new SimpleStringProperty();
        uid = new SimpleIntegerProperty();
        role = new SimpleIntegerProperty();
    }

    public String getFirst_name() {
        return first_name.get();
    }

    public StringProperty first_nameProperty() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name.set(first_name);
    }

    public String getLast_name() {
        return last_name.get();
    }

    public StringProperty last_nameProperty() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name.set(last_name);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        String hash = DigestUtils.md5Hex(password + getEmail());
        this.password.set(hash);
    }

    public int getUid() {
        return uid.get();
    }

    public IntegerProperty uidProperty() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid.set(uid);
    }

    public int getRole() {
        return role.get();
    }

    public IntegerProperty roleProperty() {
        return role;
    }

    public void setRole(int role) {
        this.role.set(role);
    }

    public void setOther_users(HashMap<String, Integer> other_users) {
        this.other_users = other_users;
    }

    public HashMap<String, Integer> getOther_users() {
        return other_users;
    }

    public int sizeOfOtherUsers() {
        return other_users.size();
    }

    public Boolean otherUserSet() {
        if(other_users.size() > 0){
            return true;
        }
        return false;
    }
}
