package roboniania.com.roboniania_android.api.model;

import java.io.Serializable;

/**
 * Created by Mateusz on 13.11.2016.
 */
public class Account implements Serializable {

    private String id;
    private String username;
    private String name;
    private String surname;
    private String email;

    public Account() {
    }

    public Account(String id, String username, String name, String surname, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

