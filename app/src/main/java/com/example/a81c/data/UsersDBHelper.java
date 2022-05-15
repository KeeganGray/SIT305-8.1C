package com.example.a81c.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.a81c.model.User;
import com.example.a81c.util.UserUtil;

public class UsersDBHelper extends SQLiteOpenHelper {
    public UsersDBHelper(@Nullable Context context) {
        super(context, UserUtil.DATABASE_NAME, null, UserUtil.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + UserUtil.TABLE_NAME + "(" + UserUtil.UID + " INTEGER PRIMARY KEY , " + UserUtil.USERNAME + " TEXT, " + UserUtil.FULLNAME + " TEXT, " + UserUtil.PASSWORD + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + UserUtil.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_USER_TABLE, new String[]{UserUtil.TABLE_NAME});

        onCreate(sqLiteDatabase);
    }

    public long insertNew(User newUser){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserUtil.UID, newUser.UID);
        contentValues.put(UserUtil.USERNAME, newUser.Username);
        contentValues.put(UserUtil.FULLNAME, newUser.FullName);
        contentValues.put(UserUtil.PASSWORD, newUser.Password);
        long newRowId = db.insert(UserUtil.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }
}
