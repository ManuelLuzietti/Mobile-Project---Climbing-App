package com.example.climbingapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.example.climbingapp.database.entities.Boulder;
import com.example.climbingapp.database.entities.Comment;
import com.example.climbingapp.database.entities.CompletedBoulder;
import com.example.climbingapp.database.entities.TracciaturaBoulder;
import com.example.climbingapp.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Boulder.class, Comment.class, User.class, CompletedBoulder.class, TracciaturaBoulder.class},version = 1)
public abstract class ClimbingRoomDatabase extends androidx.room.RoomDatabase {

    public abstract ClimbingDAO getDao();
    private static volatile ClimbingRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static ClimbingRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (ClimbingRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),ClimbingRoomDatabase.class,"climbing_database").build();
                }
            }
        }
        return INSTANCE;
    }

    public static void disableForeignKeysConstraints(Context context){
        databaseWriteExecutor.execute(() -> {
                    ClimbingRoomDatabase.getDatabase(context).compileStatement("PRAGMA foreign_keys = OFF");
                }
        );
    }
    public static void enableForeignKeysConstraints(Context context){
        databaseWriteExecutor.execute(() -> {
                    ClimbingRoomDatabase.getDatabase(context).compileStatement("PRAGMA foreign_keys = ON");
                }
        );
    }
}
