package com.suhel.kotha;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public interface ReadCallback {

        void onComplete(Object object);

    }

    public interface WriteCallback {

        void onComplete();

    }

    private static final String DATABASE_NAME = "KothaDB";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public synchronized void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Users ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userName TEXT NOT NULL, firstName TEXT NOT NULL, lastName TEXT NOT NULL, eMail TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Messages ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fromUser TEXT NOT NULL, toUser TEXT NOT NULL, message TEXT, dateCreated DATE NOT NULL )");
    }

    @Override
    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Messages");
        onCreate(db);
    }

    public synchronized void addUser(ParseUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO Users(userName, firstName, lastName, eMail) " +
                "VALUES(" +
                "'" + user.getUsername() + "', " +
                "'" + user.getString(Kotha.FIELD_FIRST_NAME) + "', " +
                "'" + user.getString(Kotha.FIELD_LAST_NAME) + "', " +
                "'" + user.getEmail() + "')");
    }

    public synchronized void addUsers(List<ParseUser> users, final WriteCallback callback) {

        SQLiteDatabase db = this.getWritableDatabase();

        new AsyncTask<Object, Void, Void>() {

            @Override
            protected Void doInBackground(Object... params) {
                SQLiteDatabase db = (SQLiteDatabase) params[0];
                List<ParseUser> users = (List<ParseUser>) params[1];
                for (ParseUser user : users) {
                    db.execSQL("INSERT INTO Users(userName, firstName, lastName, eMail) " +
                            "VALUES(" +
                            "'" + user.getUsername() + "', " +
                            "'" + user.getString(Kotha.FIELD_FIRST_NAME) + "', " +
                            "'" + user.getString(Kotha.FIELD_LAST_NAME) + "', " +
                            "'" + user.getEmail() + "')");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (callback != null)
                    callback.onComplete();
            }

        }.execute(db, users);

    }

    public synchronized List<ParseUser> loadUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String currentUserName = ParseUser.getCurrentUser().getUsername();
        List<ParseUser> lstUsers = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE userName != '" + currentUserName + "'", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()) {
            ParseUser tempUser = new ParseUser();
            tempUser.setUsername(cursor.getString(cursor.getColumnIndex("userName")));
            tempUser.setEmail(cursor.getString(cursor.getColumnIndex("eMail")));
            tempUser.put(Kotha.FIELD_FIRST_NAME, cursor.getString(cursor.getColumnIndex("firstName")));
            tempUser.put(Kotha.FIELD_LAST_NAME, cursor.getString(cursor.getColumnIndex("lastName")));
            lstUsers.add(tempUser);
            cursor.moveToNext();
        }
        return lstUsers;
    }

    public synchronized void insertMessage(String from, String to, String message, Date date, final WriteCallback callback) {
        SQLiteDatabase db = this.getWritableDatabase();

        new AsyncTask<Object, Void, Void>() {

            @Override
            protected Void doInBackground(Object... params) {
                SQLiteDatabase db = (SQLiteDatabase) params[0];
                String from = (String) params[1];
                String to = (String) params[2];
                String message = (String) params[3];
                Date date = (Date) params[4];
                db.execSQL("INSERT INTO Messages(fromUser, toUser, message, dateCreated) VALUES('"
                        + from + "', '" + to + "', '" + message + "', '"
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + "')");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (callback != null)
                    callback.onComplete();
            }

        }.execute(db, from, to, message, date);
    }

    public synchronized void insertMessage(ChatMessage message, final WriteCallback callback) {
        SQLiteDatabase db = this.getWritableDatabase();

        new AsyncTask<Object, Void, Void>() {

            @Override
            protected Void doInBackground(Object... params) {
                SQLiteDatabase db = (SQLiteDatabase) params[0];
                String from = (String) params[1];
                String to = (String) params[2];
                String message = (String) params[3];
                Date date = (Date) params[4];
                db.execSQL("INSERT INTO Messages(fromUser, toUser, message, dateCreated) VALUES('"
                        + from + "', '" + to + "', '" + message + "', '"
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + "')");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (callback != null)
                    callback.onComplete();
            }

        }.execute(db, message.fromUser, message.toUser, message.message, message.date);
    }

    public synchronized void insertMessages(List<ChatMessage> messages, final WriteCallback callback) {

        SQLiteDatabase db = this.getWritableDatabase();

        new AsyncTask<Object, Void, Void>() {

            @Override
            protected Void doInBackground(Object... params) {
                SQLiteDatabase db = (SQLiteDatabase) params[0];
                List<ChatMessage> messages = (List<ChatMessage>) params[1];
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (ChatMessage message : messages) {
                    db.execSQL("INSERT INTO Messages(fromUser, toUser, message, dateCreated) VALUES('"
                            + message.fromUser + "', '" + message.toUser + "', '" + message.message + "', '"
                            + formatDate.format(message.date) + "')");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (callback != null)
                    callback.onComplete();
            }

        }.execute(db, messages);

    }

    public synchronized void getLastDate(String userName, final ReadCallback callback) {

        SQLiteDatabase db = this.getReadableDatabase();
        String currentUser = ParseUser.getCurrentUser().getUsername();

        new AsyncTask<Object, Void, Date>() {

            @Override
            protected Date doInBackground(Object... params) {
                SQLiteDatabase db = (SQLiteDatabase) params[0];
                String userName = (String) params[1];
                String currentUser = (String) params[2];

                Date lastDate = null;

                Cursor cursor = db.rawQuery("SELECT max(dateCreated) FROM Messages WHERE " +
                        "( fromUser='" + userName + "' AND fromUser='" + currentUser + "' ) OR " +
                        "( fromUser='" + currentUser + "' AND toUser='" + userName + "' )", null);
                cursor.moveToFirst();
                try {
                    lastDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                            parse(cursor.getString(cursor.getColumnIndex("dateCreated")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return lastDate;
            }

            @Override
            protected void onPostExecute(Date date) {
                super.onPostExecute(date);
                if (callback != null)
                    callback.onComplete(date);
            }
        }.execute(db, userName, currentUser);

    }

    public synchronized int getMessagesCount(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String currentUserName = ParseUser.getCurrentUser().getUsername();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM Messages " +
                "WHERE ((fromUser='" + currentUserName + "' AND toUser='" + userName + "') " +
                "OR (fromUser='" + userName + "' AND toUser='" + currentUserName + "'))", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public synchronized int getCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM Users", null);
        cursor.moveToFirst();
        return cursor.getInt(0) - 1;
    }

    public synchronized String fetchLastMessage(String userName) {

        SQLiteDatabase db = this.getReadableDatabase();
        String currentUserName = ParseUser.getCurrentUser().getUsername();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM Messages " +
                "WHERE ((fromUser='" + currentUserName + "' AND toUser='" + userName + "') " +
                "OR (fromUser='" + userName + "' AND toUser='" + currentUserName + "'))", null);
        cursor.moveToFirst();
        if(cursor.getInt(0) == 0)
            return "";
        cursor = db.rawQuery("SELECT message FROM Messages WHERE " +
                "(( fromUser='" + userName + "' AND toUser='" + currentUserName + "' ) OR " +
                "( fromUser='" + currentUserName + "' AND toUser='" + userName + "' )) AND " +
                "dateCreated = (SELECT max(dateCreated) FROM Messages)", null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public synchronized void fetchMessages(String userName, int page, long pageSize, final ReadCallback callback) {
        SQLiteDatabase db = this.getReadableDatabase();
        String currentUser = ParseUser.getCurrentUser().getUsername();

        new AsyncTask<Object, Void, List<ChatItem>>() {

            @Override
            protected List<ChatItem> doInBackground(Object... params) {

                SQLiteDatabase db = (SQLiteDatabase) params[0];
                String userName = (String) params[1];
                Integer page = (Integer) params[2];
                Long pageSize = (Long) params[3];
                String currentUserName = (String) params[4];

                List<ChatItem> arrChats = new ArrayList<>();
                Cursor cursor = db.rawQuery("SELECT * FROM Messages WHERE " +
                        "( fromUser='" + userName + "' AND toUser='" + currentUserName + "' ) OR " +
                        "( fromUser='" + currentUserName + "' AND toUser='" + userName + "' ) " +
                        "ORDER BY dateCreated DESC LIMIT " + pageSize + " OFFSET " + (page * pageSize), null);
                cursor.moveToLast();
                while (!cursor.isBeforeFirst()) {
                    if (cursor.getString(cursor.getColumnIndex("fromUser")).equals(currentUserName))
                        arrChats.add(new ChatItem(cursor.getString(cursor.getColumnIndex("message")), true));
                    else
                        arrChats.add(new ChatItem(cursor.getString(cursor.getColumnIndex("message")), false));
                    cursor.moveToPrevious();
                }
                return arrChats;
            }

            @Override
            protected void onPostExecute(List<ChatItem> chatItems) {
                super.onPostExecute(chatItems);
                if (callback != null) {
                    callback.onComplete(chatItems);
                }
            }
        }.execute(db, userName, userName, new Integer(page), new Long(pageSize), currentUser);
    }

    public synchronized void fetchMessages(String userName, int page, long pageSize, Date upto, final ReadCallback callback) {
        SQLiteDatabase db = this.getReadableDatabase();
        String currentUser = ParseUser.getCurrentUser().getUsername();

        new AsyncTask<Object, Void, List<ChatItem>>() {

            @Override
            protected List<ChatItem> doInBackground(Object... params) {

                SQLiteDatabase db = (SQLiteDatabase) params[0];
                String userName = (String) params[1];
                Integer page = (Integer) params[2];
                Long pageSize = (Long) params[3];
                Date upto = (Date) params[4];
                String currentUserName = (String) params[5];

                List<ChatItem> arrChats = new ArrayList<>();
                Cursor cursor = db.rawQuery("SELECT * FROM Messages WHERE " +
                        "(( fromUser='" + userName + "' AND toUser='" + currentUserName + "' ) OR " +
                        "( fromUser='" + currentUserName + "' AND toUser='" + userName + "' )) AND " +
                        "dateCreated <= '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(upto) + "' " +
                        "ORDER BY dateCreated DESC LIMIT " + pageSize + " OFFSET " + ((page - 1) * pageSize), null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (cursor.getString(cursor.getColumnIndex("fromUser")).equals(currentUserName))
                        arrChats.add(0, new ChatItem(cursor.getString(cursor.getColumnIndex("message")), true));
                    else
                        arrChats.add(0, new ChatItem(cursor.getString(cursor.getColumnIndex("message")), false));
                    cursor.moveToPrevious();
                }
                return arrChats;
            }

            @Override
            protected void onPostExecute(List<ChatItem> chatItems) {
                super.onPostExecute(chatItems);
                if (callback != null) {
                    callback.onComplete(chatItems);
                }
            }
        }.execute(db, userName, userName, new Integer(page), new Long(pageSize), upto, currentUser);
    }
}
