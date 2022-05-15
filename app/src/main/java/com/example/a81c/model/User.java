package com.example.a81c.model;

public class User {

    public Integer UID;
    public String Username;
    public String FullName;
    public String Password;
    public User(Integer UID, String Username, String FullName, String Password){
        this.UID = UID;
        this.Username = Username;
        this.FullName = FullName;
        this.Password = Password;
    }
    public User(){
    }


}
