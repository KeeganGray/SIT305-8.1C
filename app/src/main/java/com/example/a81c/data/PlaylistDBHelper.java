package com.example.a81c.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.a81c.model.Playlist;
import com.example.a81c.util.PlaylistUtil;

public class PlaylistDBHelper extends SQLiteOpenHelper {
    public PlaylistDBHelper(@Nullable Context context) {
        super(context, PlaylistUtil.DATABASE_NAME, null, PlaylistUtil.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + PlaylistUtil.TABLE_NAME + "(" + PlaylistUtil.PID + " INTEGER PRIMARY KEY , " + PlaylistUtil.USERID + " TEXT, " + PlaylistUtil.URL + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + PlaylistUtil.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_USER_TABLE, new String[]{PlaylistUtil.TABLE_NAME});

        onCreate(sqLiteDatabase);
    }

    public long insertNew(Playlist newPlaylist){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlaylistUtil.PID, newPlaylist.PID);
        contentValues.put(PlaylistUtil.USERID, newPlaylist.UserID);
        contentValues.put(PlaylistUtil.URL, newPlaylist.URL);
        long newRowId = db.insert(PlaylistUtil.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }
}
