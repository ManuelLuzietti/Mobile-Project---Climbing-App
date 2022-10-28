package com.example.climbingapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

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
import java.util.concurrent.Future;

public class ClimbingAppRepository {

    private ClimbingDAO dao;
    private Application application;
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    private List<String> tableNames = new ArrayList<>();
    private LiveData<List<Boulder>> boulders;

    public ClimbingAppRepository(Application application) {
        this.application = application;
        dao = ClimbingRoomDatabase.getDatabase(application.getApplicationContext()).getDao();
        tableNames.add("boulder");
        tableNames.add("comment");
        tableNames.add("completed_boulder");
        tableNames.add("tracciatura_boulder");
        tableNames.add("user");
        boulders = dao.getBoulders();

    }

    public void updateDB() {

        executor.execute(()->{
            ClimbingRoomDatabase.getDatabase(application.getApplicationContext()).clearAllTables();
            StringRequest request = new StringRequest(Request.Method.GET, InternetManager.URL + "?method=dump", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        System.out.println(response);
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
        });

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
                                            object.getInt("comment_id"),
                                            object.getInt("number_of_tries")
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

    public Future<Long> insertBoulder(Boulder boulder) {
        return ClimbingRoomDatabase.databaseWriteExecutor.submit(()->{
            return dao.insertBoulder(boulder);
        });
//        ClimbingRoomDatabase.databaseWriteExecutor.execute(() -> {
//            dao.insertBoulder(boulder);
//        });
    }

    public Future<Long> insertUser(User user) {
        return ClimbingRoomDatabase.databaseWriteExecutor.submit(() -> {
            return dao.insertUser(user);
        });
    }

    public Future<Long> insertComment(Comment comment) {
        return ClimbingRoomDatabase.databaseWriteExecutor.submit(() -> {
           return dao.insertComment(comment);
        });
    }

    public Future<Long> insertCompletedBoulder(CompletedBoulder completedBoulder) {
        return ClimbingRoomDatabase.databaseWriteExecutor.submit(() -> {
            return dao.insertCompletedBoulder(completedBoulder);
        });
    }

    public Future<Long> insertTracciaturaBoulder(TracciaturaBoulder tracciaturaBoulder) {
        return ClimbingRoomDatabase.databaseWriteExecutor.submit(() -> {
            return dao.insertTracciatura(tracciaturaBoulder);
        });
    }

    public LiveData<List<Boulder>> getBoulders(){
        return boulders;
    }



    public  LiveData<User> getTracciatoreFromBoulder(int id){
        return dao.getTracciatoreFromBoulder(id);
    }

    public LiveData<List<CompletedBoulder>> getCompletionsOfBoulder(int id){
        return dao.getCompletionsOfBoulder(id);
    }

    public LiveData<List<Boulder>> getBoulderIfCompletedByUser(int id_user, int id_boulder){
        return dao.isBoulderCompletedByUser(id_user,id_boulder);
    }

    public LiveData<List<Comment.CommentUpdated>> getCommentsOnBoulder(int boulder_id){
        return dao.getCommentsOnBoulder(boulder_id);
    }

    public LiveData<List<Comment>> getComments() {
        return dao.getComments();
    }

    public LiveData<CompletedBoulder>  getCompletionOfBoulderFromComment(int commentId){
        return dao.getCompletionOfBoulderFromComment(commentId);
    }

    public LiveData<User> getUserFromId(int id){
        return dao.getUserFromId(id);
    }


    public LiveData<List<User>> getUserFromUsername(String username) {
        return dao.getUserFromUsername(username);
    }

    public LiveData<List<Boulder>> getBoulderIfPresent(String name){
        return  null;
    }

    public LiveData<List<Boulder.BoulderUpdated>> getBouldersCompletedByUser(int id){
        return dao.getBouldersCompletedByUser( id);
    }

    public LiveData<List<Boulder.BoulderUpdated>> getBouldersUpdated(int userId){
        return dao.getBoulderUpdated(userId);
    }


    public LiveData<List<Boulder.BoulderLogbook>> getBouldersLogbook(int userId){
        return dao.getBouldersLogbook(userId);
    }
}
