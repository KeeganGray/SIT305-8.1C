package com.example.a81c;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a81c.data.PlaylistDBHelper;
import com.example.a81c.data.UsersDBHelper;
import com.example.a81c.model.Playlist;
import com.example.a81c.model.User;
import com.example.a81c.util.PlaylistUtil;
import com.example.a81c.util.UserUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    PlaylistDBHelper playlistdbHelper;
    UsersDBHelper usersdbHelper;
    FragmentContainerView fragmentContainerView;
    EditText urlIn, fullNameIn, usernameIn, passwordIn, passwordConfirmIn, usernameLogin, passwordLogin;
    Integer currentUser;
    YouTubePlayerView youtubePlayer;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = 0;
        fullNameIn = findViewById(R.id.fullNameIn);
        usernameIn = findViewById(R.id.usernameIn);
        passwordIn = findViewById(R.id.passwordIn);
        passwordConfirmIn = findViewById(R.id.passwordConfirmIn);

        usernameLogin = findViewById(R.id.usernameLogin);
        passwordLogin = findViewById(R.id.passwordLogin);

        playButton = findViewById(R.id.playButton);
        youtubePlayer = (YouTubePlayerView) findViewById(R.id.youtubePlayer);

        fragmentContainerView = findViewById(R.id.fragmentContainerView);

        playlistdbHelper = new PlaylistDBHelper(getApplicationContext());
        usersdbHelper = new UsersDBHelper(getApplicationContext());
        urlIn = findViewById(R.id.urlIn);


        setContentView(R.layout.activity_main);
    }

    public void playYoutube (View view){
        urlIn = findViewById(R.id.urlIn);
        String URL = urlIn.getText().toString();

        Intent playIntent = new Intent(this, PlayActivity.class);
        playIntent.putExtra("key",URL);
        startActivity(playIntent);
    }

    public void selectFragment(View view){
        Fragment fragment = null;
        Fragment fHomeFragment = new HomeFragment();
        Fragment fSignupFragment = new SignupFragment();
        Fragment fDefaultFragment = new DefaultFragment();
        Fragment fMyPlaylist = new MyPlaylist();

        switch (view.getId()){
            case R.id.loginButton:
            case R.id.playlistAddButton:
            case R.id.playlistBackButton:
                fragment = fHomeFragment;
                break;
            case R.id.signupButton:
                fragment = fSignupFragment;
                break;
            case R.id.playlistButton:
                fragment = fMyPlaylist;
                break;
            default:
                fragment = fDefaultFragment;
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment).commit();
    }

    public List<Playlist> playlistsGet(){
        SQLiteDatabase db = playlistdbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PlaylistUtil.TABLE_NAME, null);

        ArrayList<Playlist> table = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                table.add(new Playlist(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        return table;
    }

    public List<Playlist> playlistGet(){
        SQLiteDatabase db = playlistdbHelper.getReadableDatabase();
        String[] args = {currentUser.toString()};
        Cursor cursor = db.rawQuery("select * from " + PlaylistUtil.TABLE_NAME + " where " + PlaylistUtil.USERID + "=?", args, null);

        ArrayList<Playlist> table = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                table.add(new Playlist(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        return table;
    }

    public void playlistAdd(View view){

        urlIn = findViewById(R.id.urlIn);
        String url = urlIn.getText().toString();
        if(urlIn.length()<1){
            Toast.makeText(getApplicationContext(), "Ensure all fields are filled!", Toast.LENGTH_LONG).show();
        }//elseif condition
        else{
            Integer len = playlistsGet().size();
            Playlist newPlaylist = new Playlist(len, this.currentUser, url);
            playlistdbHelper.insertNew(newPlaylist);
            selectFragment(view);
        }
    }

    public List<User> usersGet(){
        SQLiteDatabase db = usersdbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + UserUtil.TABLE_NAME, null);

        ArrayList<User> table = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                table.add(new User(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        return table;
    }

    public void signUp(View view){

        String fni, uni, pwi, pci;
        fullNameIn = findViewById(R.id.fullNameIn);
        usernameIn = findViewById(R.id.usernameIn);
        passwordIn = findViewById(R.id.passwordIn);
        passwordConfirmIn = findViewById(R.id.passwordConfirmIn);
        fni = fullNameIn.getText().toString();
        uni = usernameIn.getText().toString();
        pwi = passwordIn.getText().toString();
        pci = passwordConfirmIn.getText().toString();

        if(fni.length()<1 || uni.length()<1 || pwi.length()<1 || pci.length()<1){
            Toast.makeText(getApplicationContext(), "Ensure all fields are filled!", Toast.LENGTH_LONG).show();
        }
        else if(pwi.equals(pci)){
            Integer len = usersGet().size();
            User newUser = new User(len, fni, uni, pwi);
            usersdbHelper.insertNew(newUser);
            selectFragment(view);
        }
        else{
            Toast.makeText(getApplicationContext(), "Ensure passwords match!", Toast.LENGTH_LONG).show();
        }

    }
    public void logIn(View view){
        usernameLogin = findViewById(R.id.usernameLogin);
        passwordLogin = findViewById(R.id.passwordLogin);

        List<User> table = usersGet();
        String un = usernameLogin.getText().toString();
        String pw = passwordLogin.getText().toString();
        Integer foundUID = 0;
        Boolean found = false;
        Boolean matched = false;
        for(int i = 0; i < table.size(); i++){
            if (table.get(i).Username.equals(un)){
                found = true;
                if (table.get(i).Password.equals(pw)){
                    matched = true;
                    foundUID = table.get(i).UID;
                }
            }
        }
        if(!found || !matched){
            Toast.makeText(getApplicationContext(), "Username or password not found", Toast.LENGTH_LONG).show();
        }
        else
        {
            this.currentUser = foundUID;
            selectFragment(view);
        }
    }
}