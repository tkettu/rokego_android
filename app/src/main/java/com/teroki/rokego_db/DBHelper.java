package com.teroki.rokego_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.teroki.rokego_objects.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tero on 27.9.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RokegoDB.db";
    private static final String SPORTS_TABLE_NAME = "Sports";
    private static final String SPORTS_COLUMN_ID = "id";
    private static final String SPORTS_COLUMN_NAME = "name";
    private static final String SPORTS_COLUMN_TYPE = "type";
    private static final String SPORTS_COLUMN_DISTANCE = "distance";
    private static final String SPORTS_COLUMN_TIME = "time";
    private static final String SPORTS_COLUMN_DATE = "date";

    private String LOG_TAG = "DBHelper";
    // NOTE https://stackoverflow.com/questions/7363112/best-way-to-work-with-dates-in-android-sqlite#comment18657781_7363557

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SPORTS_TABLE = "CREATE TABLE " + SPORTS_TABLE_NAME + "(" +
                SPORTS_COLUMN_ID + " INTEGER PRIMARY KEY," + SPORTS_COLUMN_NAME + " TEXT," +
                SPORTS_COLUMN_TYPE + " TEXT DEFAULT ''," +
                SPORTS_COLUMN_DISTANCE + " REAL," + SPORTS_COLUMN_TIME + " TEXT," +
                SPORTS_COLUMN_DATE + " INTEGER)";
        db.execSQL(CREATE_SPORTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF EXISTS " + SPORTS_TABLE_NAME);
        onCreate(db);*/
        if (newVersion > oldVersion){
            db.execSQL("ALTER TABLE " + SPORTS_TABLE_NAME + " ADD COLUMN " + SPORTS_COLUMN_TYPE + " TEXT DEFAULT ''");
        }
    }

    public void addExercise(Exercise exercise){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SPORTS_COLUMN_NAME, exercise.getName());
        values.put(SPORTS_COLUMN_TYPE, exercise.getType());
        values.put(SPORTS_COLUMN_DISTANCE, exercise.getDistance());
        values.put(SPORTS_COLUMN_TIME, exercise.getTime());
        values.put(SPORTS_COLUMN_DATE, exercise.getDate());



        db.insert(SPORTS_TABLE_NAME, null, values);
        db.close();
    }

    public Exercise getExercise(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SPORTS_TABLE_NAME, new String[]{SPORTS_COLUMN_ID, SPORTS_COLUMN_NAME,
                        SPORTS_COLUMN_TYPE, SPORTS_COLUMN_DISTANCE, SPORTS_COLUMN_TIME,
                        SPORTS_COLUMN_DATE}, SPORTS_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Exercise exercise = new Exercise(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3)), cursor.getString(4),
                Long.parseLong(cursor.getString(5)));
        cursor.close();
        db.close();
        return exercise;
    }

    public List<Exercise> getExercises(){
        List<Exercise> exerciseList = new ArrayList<Exercise>();

        String selectQuery = "SELECT * FROM " + SPORTS_TABLE_NAME +
                " ORDER BY " + SPORTS_COLUMN_DATE +" DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                Exercise exercise = new Exercise();
                exercise.setId(Integer.parseInt(cursor.getString(0)));
                exercise.setName(cursor.getString(cursor.getColumnIndex(SPORTS_COLUMN_NAME)));
                exercise.setType(cursor.getString(cursor.getColumnIndex(SPORTS_COLUMN_TYPE)));
                exercise.setDistance(Double.parseDouble(cursor.getString(cursor.getColumnIndex(SPORTS_COLUMN_DISTANCE))));
                exercise.setTime(cursor.getString(cursor.getColumnIndex(SPORTS_COLUMN_TIME)));
                exercise.setDate(Long.parseLong(cursor.getString(cursor.getColumnIndex(SPORTS_COLUMN_DATE))));

                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return exerciseList;
    }

    /**
     * Returns filtered Exercise list
     * @param name Names of the sports to show
     * @param sDate Start date of sports
     * @param eDate End date
     * @param minDist Minimum distance
     * @param maxDist Maximum distance
     * @param minTime Minimum time as hh:mm:ss
     * @param maxTime maximum time as hh:mm:ss
     * @return
     */
    public List<Exercise> getExercises(String[] name, String sDate, String eDate,
                                       double minDist, double maxDist,
                                       String minTime, String maxTime){

        //Array to (a[0],[a[1],...a[n],)
        String names = "(";
        int len = name.length;
        for (int i = 0; i < len -1; i++) {
            names += "'"+name[i]+"', ";
        }

        names += "'"+name[len-1]+"')";
        Log.d(LOG_TAG, names);
        //SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + SPORTS_TABLE_NAME + " WHERE " +
                SPORTS_COLUMN_NAME + " IN " + names;

        return getExercisesFromQuery(selectQuery);
        //return null;
    }

    private List<Exercise> getExercisesFromQuery(String selectQuery) {
        List<Exercise> exerciseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                Exercise exercise = new Exercise();
                exercise.setId(Integer.parseInt(cursor.getString(0)));
                exercise.setName(cursor.getString(cursor.getColumnIndex(SPORTS_COLUMN_NAME)));
                exercise.setType(cursor.getString(cursor.getColumnIndex(SPORTS_COLUMN_TYPE)));
                exercise.setDistance(Double.parseDouble(cursor.getString(cursor.getColumnIndex(SPORTS_COLUMN_DISTANCE))));
                exercise.setTime(cursor.getString(cursor.getColumnIndex(SPORTS_COLUMN_TIME)));
                exercise.setDate(Long.parseLong(cursor.getString(cursor.getColumnIndex(SPORTS_COLUMN_DATE))));

                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exerciseList;

    }

    public List<String> exerciseList(){
        List<Exercise> exes = getExercises();
        List<String> sExes = new ArrayList<>();

        for (Exercise e: exes){
            String s = e.getName() + ", time: " + e.getTime() + " distance: " + e.getDistance() +
                    " date: " + e.formatDate();
            sExes.add(s );
        }

        return  sExes;
    }
    public int countExercises(){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + SPORTS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    //Todo sort list

    public int updateExercise(Exercise exercise){return 0;}

    public boolean deleteExercise(Exercise exercise){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SPORTS_TABLE_NAME, SPORTS_COLUMN_ID +"="+exercise.getId(), null) > 0;
        //close db ??
    }

    /*public DBHelper open(){
        SQLiteDatabase db = this.getReadableDatabase();
        return this;
    }

    public void close(){

    }*/

}

