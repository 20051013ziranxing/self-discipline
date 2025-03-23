package com.example.localdatabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.localdatabase.bean.UserTables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListOfLabelsHelper extends SQLiteOpenHelper {
    public static final String TAG = "TestTT_ListOfLabelsHelper";
    public static final String CREATE_USER_LABEL = "create table userLabel (" +
            "id integer primary key autoincrement," +
            "userName text," +
            "userId text," +
            "userListings text," +
            "userLabels text )";
    private Context mcontext;
    public ListOfLabelsHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "userLabelMessage", null, 1);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_LABEL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists userLabel");
        onCreate(db);
    }

    //添加
    public boolean insert(UserTables userTables) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userName", userTables.getUserName());
        values.put("userId", userTables.getUserId());
        // 将userListings列表序列化为JSON字符串
        if (userTables.getUserListings() != null) {
            JSONArray userListingsArray = new JSONArray();
            for (UserTables.UserListing listing : userTables.getUserListings()) {
                JSONObject listingObj = new JSONObject();
                try {
                    listingObj.put("listingColor", listing.getListingColor());
                    listingObj.put("listingName", listing.getListingName());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                userListingsArray.put(listingObj);
            }
            values.put("userListings", userListingsArray.toString());
        }
        // 将userLabels列表序列化为JSON字符串
        if (userTables.getUserLabels() != null) {
            JSONArray userLabelsArray = new JSONArray();
            for (UserTables.UserLabel label : userTables.getUserLabels()) {
                JSONObject labelObj = new JSONObject();
                try {
                    labelObj.put("labelColor", label.getLabelColor());
                    labelObj.put("labelName", label.getLabelName());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                userLabelsArray.put(labelObj);
            }
            values.put("userLabels", userLabelsArray.toString());
        }
        long l = db.insert("userLabel", null, values);
        db.close();
        return l != -1;
    }

    public List<UserTables> getAllUserLabels() {
        List<UserTables> userTablesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userLabel", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String userName = cursor.getString(cursor.getColumnIndex("userName"));
                @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex("userId"));
                @SuppressLint("Range") String userListingsStr = cursor.getString(cursor.getColumnIndex("userListings"));
                @SuppressLint("Range") String userLabelsStr = cursor.getString(cursor.getColumnIndex("userLabels"));
                // 反序列化userListings
                List<UserTables.UserListing> userListings = null;
                List<UserTables.UserLabel> userLabels = null;
                if (userListingsStr != null) {
                    try {
                        JSONArray userListingsArray = new JSONArray(userListingsStr);
                        userListings = new ArrayList<>();
                        for (int i = 0; i < userListingsArray.length(); i++) {
                            JSONObject listingObj = userListingsArray.getJSONObject(i);
                            UserTables.UserListing listing = new UserTables.UserListing(
                                    listingObj.getString("listingColor"),
                                    listingObj.getString("listingName")
                            );
                            userListings.add(listing);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 反序列化userLabels
                if (userLabelsStr != null) {
                    try {
                        JSONArray userLabelsArray = new JSONArray(userLabelsStr);
                        userLabels = new ArrayList<>();
                        for (int i = 0; i < userLabelsArray.length(); i++) {
                            JSONObject labelObj = userLabelsArray.getJSONObject(i);
                            UserTables.UserLabel label = new UserTables.UserLabel(
                                    labelObj.getString("labelColor"),
                                    labelObj.getString("labelName")
                            );
                            userLabels.add(label);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                UserTables userTables = new UserTables(userName, userId, userListings, userLabels);
                userTablesList.add(userTables);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userTablesList;
    }

    public UserTables findUserTablesByUserId(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userLabel WHERE userId = ?", new String[]{userId});
        UserTables userTables = null;
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String userName = cursor.getString(cursor.getColumnIndex("userName"));
            @SuppressLint("Range") String userListingsStr = cursor.getString(cursor.getColumnIndex("userListings"));
            @SuppressLint("Range") String userLabelsStr = cursor.getString(cursor.getColumnIndex("userLabels"));
            // 反序列化userListings
            List<UserTables.UserListing> userListings = null;
            List<UserTables.UserLabel> userLabels = null;
            try {
                if (userListingsStr != null) {
                    JSONArray userListingsArray = new JSONArray(userListingsStr);
                    userListings = new ArrayList<>();
                    for (int i = 0; i < userListingsArray.length(); i++) {
                        JSONObject listingObj = userListingsArray.getJSONObject(i);
                        UserTables.UserListing listing = new UserTables.UserListing(
                                listingObj.getString("listingColor"),
                                listingObj.getString("listingName")
                        );
                        userListings.add(listing);
                    }
                }
                // 反序列化userLabels
                if (userLabelsStr != null) {
                    JSONArray userLabelsArray = new JSONArray(userLabelsStr);
                    userLabels = new ArrayList<>();
                    for (int i = 0; i < userLabelsArray.length(); i++) {
                        JSONObject labelObj = userLabelsArray.getJSONObject(i);
                        UserTables.UserLabel label = new UserTables.UserLabel(
                                labelObj.getString("labelColor"),
                                labelObj.getString("labelName")
                        );
                        userLabels.add(label);
                    }
                }
                userTables = new UserTables(userName, userId, userListings, userLabels);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return userTables;
    }

    public boolean updateUserListings(String userId, List<UserTables.UserListing> newListings, final CallbackLocal callbackLocal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // 将newListings列表序列化为JSON字符串
        JSONArray userListingsArray = new JSONArray();
        for (UserTables.UserListing listing : newListings) {
            JSONObject listingObj = new JSONObject();
            try {
                listingObj.put("listingColor", listing.getListingColor());
                listingObj.put("listingName", listing.getListingName());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            userListingsArray.put(listingObj);
        }
        values.put("userListings", userListingsArray.toString());

        int rowsAffected = db.update("userLabel", values, "userId = ?", new String[]{userId});
        db.close();
        if (rowsAffected > 0) {
            callbackLocal.onSucceed();
        } else {
            callbackLocal.onFailed();
        }
        return rowsAffected > 0;
    }

    public boolean updateUserLabels(String userId, List<UserTables.UserLabel> newLabels) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // 将newLabels列表序列化为JSON字符串
        JSONArray userLabelsArray = new JSONArray();
        for (UserTables.UserLabel label : newLabels) {
            JSONObject labelObj = new JSONObject();
            try {
                labelObj.put("labelColor", label.getLabelColor());
                labelObj.put("labelName", label.getLabelName());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            userLabelsArray.put(labelObj);
        }
        values.put("userLabels", userLabelsArray.toString());

        int rowsAffected = db.update("userLabel", values, "userId = ?", new String[]{userId});
        db.close();
        return rowsAffected > 0;
    }

    public int deleteUserTablesByUserId(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleteCount = db.delete("userLabel", "userId = ?", new String[]{userId});
        db.close();
        return deleteCount;
    }

    public interface CallbackLocal {
        public void onSucceed();
        public void onFailed();
    }
}