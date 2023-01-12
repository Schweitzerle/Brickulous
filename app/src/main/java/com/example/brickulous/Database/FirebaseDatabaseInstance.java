package com.example.brickulous.Database;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseInstance {
        private static FirebaseDatabaseInstance instance;
        private final FirebaseDatabase firebaseDatabase;

        private FirebaseDatabaseInstance() {
            firebaseDatabase = FirebaseDatabase.getInstance("https://brickulous-46986-default-rtdb.europe-west1.firebasedatabase.app/");
        }



        public static FirebaseDatabaseInstance getInstance() {
            if (instance == null) {
                instance = new FirebaseDatabaseInstance();
            }
            return instance;
        }

        public FirebaseDatabase getFirebaseDatabase() {
            return firebaseDatabase;
        }
    }

