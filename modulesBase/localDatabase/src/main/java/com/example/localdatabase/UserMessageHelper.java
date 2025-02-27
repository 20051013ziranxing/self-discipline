package com.example.localdatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class UserMessageHelper extends SQLiteOpenHelper {
    public static final String TAG = "TestTT__LoginUpActivity";
    public static final String CREATE_USERS = "create table users ("
            + "id integer primary key autoincrement, "
            + "userName text, "
            + "userPassword text, "
            + "userEmail text ,"
            + "isActive integer default 1 )";
    private Context mcontext;
    public UserMessageHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, 1);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS);
        /*Toast.makeText(mcontext, "成功创建用户信息表", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        onCreate(db);
    }

    public boolean insert(String userName, String userPassword, String userEmail,boolean isActive) {
        /*queryAllUser();*/
        Log.d(TAG, "我进行了添加");
        SQLiteDatabase db;
        try {
            db = getWritableDatabase();
            Log.d(TAG, "我进行了添加1");
            ContentValues contentValues = new ContentValues();
            Log.d(TAG, "我进行了添加2");
            contentValues.put("userName", userName);
            contentValues.put("userPassword", userPassword);
            contentValues.put("userEmail", userEmail);
            contentValues.put("isActive", isActive ? 1 : 0);
            db.delete("users", null, null);
            Log.d(TAG, userEmail + "ppp");
            clearUsersTable();
            long result = db.insert("users", null, contentValues);
            if (result != -1) {
                Log.d(TAG, "我添加成功了");
                queryAllUser();
            } else {
                Log.d(TAG, "我添加失败了");
                Log.d(TAG, "ContentValues: " + contentValues.toString());
                queryAllUser();
            }
            return result != -1;
        } catch (SQLiteException e) {
            Log.e(TAG, String.valueOf(e)+"我添加失败了");
            return false;
        }
    }
    //清空表的操作
    public void clearUsersTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from users");
    }
    public int queryAllUser() {
        Log.d(TAG, "我要开始查询所有的用户信息了");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "我查询到所有的用户信息了");
        Cursor cursor = db.query("users", null, null, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String userName = cursor.getString(cursor.getColumnIndex("userName"));
                @SuppressLint("Range") String userPassword = cursor.getString(cursor.getColumnIndex("userPassword"));
                @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex("userEmail"));
                @SuppressLint("Range") int userAction = cursor.getInt(cursor.getColumnIndex("isActive"));
                Log.d(TAG, "userName:" + userName + ";userPassword:" + userPassword + ";userEmail:" + userEmail);
                return userAction;
            } while (cursor.moveToNext());
        }
        return 0;
    }

    @SuppressLint("Range")
    public String queryUser(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("users", null, "userEmail = ?", new String[]{userEmail}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("userPassword"));
        } else {
            return null;
        }
    }
    @SuppressLint("Range")
    public boolean checkUserActiveStatus() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int isActive = cursor.getInt(cursor.getColumnIndex("isActive"));
            cursor.close();
            return isActive == 1;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean updateIsActiveToZero() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isActive", 0);
        int rowsAffected = db.update("users", values, null, null);
        db.close();
        return rowsAffected > 0;
    }
}
