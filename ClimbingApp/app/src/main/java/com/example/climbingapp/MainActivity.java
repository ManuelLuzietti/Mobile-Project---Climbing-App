package com.example.climbingapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.climbingapp.database.entities.Boulder;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private  InternetManager internetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        internetManager = new InternetManager(this,findViewById(R.id.main_activity_layout));


        //test1();
    }

//
//    public void test1(){
//        ClimbingDAO dao = ClimbingRoomDatabase.getDatabase(this).getDao();
//        ClimbingRoomDatabase.databaseWriteExecutor.execute(()->{
//            dao.insertUser(new User("username","M","L"));
//            dao.insertBoulder(new Boulder("bho",1,"7a",new Date(),true,"dataImg1"));
//            dao.insertTracciatura(new TracciaturaBoulder(1,1));
//            List<User> users = dao.getUsers();
//            List<Boulder> boulders = dao.getBoulders();
//            List<TracciaturaBoulder> tracciature = dao.getTracciature();
//            System.out.println("users");
//            for(User u : users){
//                System.out.println(u.id);
//            }
//            System.out.println("boulders");
//            for(Boulder b: boulders){
//                System.out.println(b.id);
//            }
//            System.out.println("tracciature");
//            for(TracciaturaBoulder t: tracciature){
//                System.out.println(t.idTracciatura);
//            }
//            List<BoulderAndTracciatura> bat = dao.getBoulderAndTracciatura();
//            System.out.println("boulder and tracciatura");
//            for(BoulderAndTracciatura bbb: bat){
//                System.out.print(bbb.boulder.id);
//                System.out.print(bbb.tracciaturaBoulder.idTracciatura);
//                System.out.println("ok");
//            }
//
//        });
//    }
//    public void test2(){
//        ClimbingDAO  dao = ClimbingRoomDatabase.getDatabase(this).getDao();
//        ClimbingRoomDatabase.databaseWriteExecutor.execute(()-> {
//            List<User> l = dao.prova();
//            for(User u: l){
//                System.out.println(u.id);
//            }
//        });
//    }
//
//    public void test3(){
//        ClimbingDAO  dao = ClimbingRoomDatabase.getDatabase(this).getDao();
//        ClimbingRoomDatabase.databaseWriteExecutor.execute(()-> {
//            dao.insertComment(new Comment("testo commento",3,"7b",1));
//            dao.insertCompletamentoBoulder(new CompletamentoBoulder(1,1,new Date(),1));
//            List<CompletamentoBoulder>  l = dao.getCompletedBoulders();
//            System.out.println("Completamento boulders");
//            for(CompletamentoBoulder c : l){
//                System.out.println(c.commentId);
//            }
//        });
//    }

    public void test4() {
        ClimbingAppRepository repo = new ClimbingAppRepository(getApplication());
        if(internetManager.isNetworkConnected()){
            repo.updateDB();
        } else {
            internetManager.getSnackbar().show();
        }
    }

    public void test5(){

        SharedPreferences pref = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username","Lux");
        editor.putInt("id",1);
        editor.apply();
    }
    public  void test6(){
        new ClimbingAppRepository(getApplication()).insertBoulder(new Boulder(4,"long journey","7B",new Date(),false,"cato.jpeg"));
    }


    @Override
    protected void onStart() {
        super.onStart();
        internetManager.registerNetworkCallback(this);
    }

    @Override
    protected void onStop() {
        //TODO: STOPPPARE RICHIESTE VOLLEY CON TAG
        super.onStop();
        internetManager.unregisterNetworkCallback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        test4();
        //test5();
        //test8();
        //test6();
    }

    private void test8() {
        getSharedPreferences("global_pref",MODE_PRIVATE).edit().remove("userId").commit();
        System.out.println(getSharedPreferences("global_pref",MODE_PRIVATE).getInt("userId",-1));
    }

}