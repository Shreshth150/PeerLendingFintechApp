package com.peerlending.securityapp.user.model;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private String username ;
    private String password ;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
