package com.example.brickulous.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseBase {


    private final FirebaseDatabase database;

    private DatabaseBase() {
        this.database = FirebaseDatabase.getInstance();
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    private static DatabaseBase databaseBase;
    public static DatabaseBase get() {
        if (databaseBase == null){
            databaseBase = new DatabaseBase();
        }
        return databaseBase;
    }

}
