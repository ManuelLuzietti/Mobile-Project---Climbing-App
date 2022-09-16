package com.example.climbingapp;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.climbingapp.database.ClimbingDAO;
import com.example.climbingapp.database.ClimbingRoomDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private Context appContext;

    @Before
    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    }

    @Test
    public void test1() {
        ClimbingDAO dao = ClimbingRoomDatabase.getDatabase(appContext).getDao();
        ClimbingRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertUser(new User("username", "M", "L"));
            dao.insertBoulder(new Boulder("bho", 1, "7a", new Date(), true, "dataImg1"));
            dao.insertTracciatura(new TracciaturaBoulder(1, 1));
            List<User> users = dao.getUsers();
            List<Boulder> boulders = dao.getBoulders();
            List<TracciaturaBoulder> tracciature = dao.getTracciature();
            System.out.println("users");
            for (User u : users) {
                System.out.println(String.valueOf(u.id));
            }
            System.out.println("boulders");
            for (Boulder b : boulders) {
                System.out.println(String.valueOf(b.id));
            }
            System.out.println("tracciature");
            for (TracciaturaBoulder t : tracciature) {
                System.out.println(String.valueOf(t.idTracciatura));
            }
        });
    }

    @Test
    public void test2(){
        System.out.println("ciao");
        Log.d("CIao","bho");
    }
}