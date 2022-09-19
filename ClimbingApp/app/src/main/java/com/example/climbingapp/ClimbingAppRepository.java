package com.example.climbingapp;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.climbingapp.database.ClimbingDAO;
import com.example.climbingapp.database.ClimbingRoomDatabase;
import com.example.climbingapp.database.entities.Boulder;
import com.example.climbingapp.database.entities.Comment;
import com.example.climbingapp.database.entities.CompletedBoulder;
import com.example.climbingapp.database.entities.TracciaturaBoulder;
import com.example.climbingapp.database.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClimbingAppRepository {

    private ClimbingDAO dao;
    private final String url = "http://192.168.1.134/climbingAppWebServer/";
    private Application application;
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    private List<String> tableNames = new ArrayList<>();

    public ClimbingAppRepository(Application application) {
        this.application = application;
        dao = ClimbingRoomDatabase.getDatabase(application.getApplicationContext()).getDao();
        tableNames.add("boulder");
        tableNames.add("comment");
        tableNames.add("completed_boulder");
        tableNames.add("tracciatura_boulder");
        tableNames.add("user");
    }

    public void updateDB() {

        StringRequest request = new StringRequest(Request.Method.GET, url + "?method=dump", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    insertInDB(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

        VolleySingleton.getInstance(application.getApplicationContext()).add(request);
    }



    private void insertInDB(JSONObject data) {
        ClimbingRoomDatabase.disableForeignKeysConstraints(application.getApplicationContext());
        for (String table : tableNames) {
            try {
                JSONArray jsonArray = (JSONArray) data.get(table);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    switch (table) {
                        case "boulder":
                            ClimbingRoomDatabase.databaseWriteExecutor.execute(() -> {
                                try {
                                    dao.insertBoulder(new Boulder(object.getInt("id"),
                                            object.getString("name"),
                                            object.getInt("rating"),
                                            object.getString("grade"),
                                            Utils.stringToDate((String) object.get("date")),
                                            object.getInt("is_official") == 1,
                                            object.getString("img")
                                    ));
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }

                            });
                            break;
                        case "comment":
                            ClimbingRoomDatabase.databaseWriteExecutor.execute(() ->
                            {

                                try {
                                    dao.insertComment(new Comment(object.getInt("id"),
                                            object.getString("text"),
                                            object.getInt("rating"),
                                            object.getString("grade"),
                                            object.getInt("user_id")));
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            });
                            break;
                        case "completed_boulder":
                            ClimbingRoomDatabase.databaseWriteExecutor.execute(()->{
                                try {
                                    dao.insertCompletedBoulder(new CompletedBoulder(object.getInt("id"),
                                            object.getInt("user_id"),
                                            object.getInt("boulder_id"),
                                            Utils.stringToDate(object.getString("date")),
                                            object.getInt("comment_id")

                                            ));
                                }catch (Exception exception){
                                    exception.printStackTrace();
                                }
                            });
                            break;
                        case "tracciatura_boulder":
                            ClimbingRoomDatabase.databaseWriteExecutor.execute(()->{
                                try{
                                    dao.insertTracciatura(new TracciaturaBoulder(object.getInt("id_tracciatura"),
                                            object.getInt("user_id"),
                                            object.getInt("boulder_id")));
                                }catch (Exception exception){
                                    exception.printStackTrace();
                                }
                            });
                            break;
                        case "user":
                            ClimbingRoomDatabase.databaseWriteExecutor.execute(()->{
                                try{
                                    dao.insertUser(new User(object.getInt("id"),
                                            object.getString("username"),
                                            object.getString("first_name"),
                                            object.getString("last_name")));
                                } catch(Exception exception){
                                    exception.printStackTrace();
                                }

                            });
                            break;
                        default:
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ClimbingRoomDatabase.enableForeignKeysConstraints(application.getApplicationContext());
    }

    public void insertBoulder(Boulder boulder) {
        ClimbingRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertBoulder(boulder);
        });
    }

    public void insertUser(User user) {
        ClimbingRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertUser(user);
        });
    }

    public void insertComment(Comment comment) {
        ClimbingRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertComment(comment);
        });
    }

    public void insertCompletedBoulder(CompletedBoulder completedBoulder) {
        ClimbingRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertCompletedBoulder(completedBoulder);
        });
    }

    public void insertTracciaturaBoulder(TracciaturaBoulder tracciaturaBoulder) {
        ClimbingRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertTracciatura(tracciaturaBoulder);
        });
    }

}
