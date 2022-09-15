package com.example.climbingapp;

import static org.junit.Assert.assertEquals;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.climbingapp.database.ClimbingDAO;
import com.example.climbingapp.database.ClimbingRoomDatabase;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private ClimbingDAO dao;
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void setup(){
        dao = ClimbingRoomDatabase.getDatabase(InstrumentationRegistry.getInstrumentation().getContext()).getDao();
    }
    @Test
    public void test1(){
        dao.insertUser(new User(1,"username","M","L"));
        List<User> list = dao.getUsers();
        System.out.println(list);

    }

}