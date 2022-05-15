package com.example.a81c.model;

public class Playlist {

    public Integer PID;
    public Integer UserID;
    public String URL;
    public Playlist(Integer PID, Integer UserID, String URL){
        this.PID=PID;
        this.UserID=UserID;
        this.URL=URL;

    }
    public Playlist(){
    }


}
