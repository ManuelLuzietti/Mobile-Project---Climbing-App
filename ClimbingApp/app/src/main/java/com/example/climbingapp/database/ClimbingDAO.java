package com.example.climbingapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.climbingapp.Comment;
import com.example.climbingapp.CompletamentoBoulderAndBoulder;
import com.example.climbingapp.CompletamentoBoulderAndCommento;
import com.example.climbingapp.TracciaturaBoulder;
import com.example.climbingapp.User;
import com.example.climbingapp.UserAndCompletamentoBoulder;

import java.util.List;

@Dao
public interface ClimbingDAO {
    @Query("SELECT * FROM user where id == :id")
    List<Comment> getUserComments(int id);

    @Query("SELECT * FROM COMPLETAMENTO_BOULDER WHERE user_id == :user_id AND boulder_id == :boulder_id")
    List<CompletamentoBoulderAndCommento> getComment(int user_id,int boulder_id);

    @Query("SELECT * FROM COMPLETAMENTO_BOULDER WHERE user_id == :id")
    List<UserAndCompletamentoBoulder> getUserCompletedBoulders(int id);

    @Query("SELECT * FROM COMPLETAMENTO_BOULDER WHERE boulder_id == :id")
    List<CompletamentoBoulderAndBoulder> getAllCompletionOfBoulder(int id);

    @Query("SELECT * FROM BOULDER WHERE tracciatore_id == :id")
    List<TracciaturaBoulder> getBouldersByTracciatore(int id);

    @Query("SELECT * FROM USER")
    List<User> getUsers();

    @Insert
    void insertUser(User user);

}
