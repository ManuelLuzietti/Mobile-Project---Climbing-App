package com.example.climbingapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.climbingapp.database.entities.Boulder;
import com.example.climbingapp.database.entities.Comment;
import com.example.climbingapp.database.entities.CompletedBoulder;
import com.example.climbingapp.database.entities.TracciaturaBoulder;
import com.example.climbingapp.database.entities.User;
import com.example.climbingapp.database.relationships.BoulderAndTracciatura;
import com.example.climbingapp.database.relationships.UserAndComment;

import java.util.List;

@Dao
public interface ClimbingDAO {
    @Query("SELECT * FROM USER where id == :id")
    List<UserAndComment> getUserComments(int id);

//    @Query("SELECT * FROM COMPLETAMENTO_BOULDER WHERE user_id == :user_id AND boulder_id == :boulder_id")
//    List<CompletamentoBoulderAndCommento> getComment(int user_id,int boulder_id);
//
//    @Query("SELECT * FROM COMPLETAMENTO_BOULDER WHERE user_id == :id")
//    List<UserAndCompletamentoBoulder> getUserCompletedBoulders(int id);
//
//    @Query("SELECT * FROM COMPLETAMENTO_BOULDER WHERE boulder_id == :id")
//    List<CompletamentoBoulderAndBoulder> getAllCompletionOfBoulder(int id);
//
//    @Query("SELECT * FROM BOULDER WHERE tracciatore_id == :id")
//    List<TracciaturaBoulder> getBouldersByTracciatore(int id);
//
    @Query("SELECT * FROM USER")
    List<User> getUsers();

    @Query("SELECT * FROM COMMENT")
    List<Comment> getComments();

    @Query("SELECT * FROM completed_boulder")
    List<CompletedBoulder> getCompletedBoulders();

    @Query("SELECT * FROM boulder")
    LiveData<List<Boulder>> getBoulders();

    @Query("SELECT * FROM TRACCIATURA_BOULDER")
    List<TracciaturaBoulder> getTracciature();

//insert:
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertComment(Comment comment);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCompletedBoulder(CompletedBoulder compBoulder);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertBoulder(Boulder boulder);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTracciatura(TracciaturaBoulder tracciaturaBoulder);

    @Query("select * from boulder ")
    List<BoulderAndTracciatura> getBoulderAndTracciatura();

//    @Query("select a.* from User a join tracciatura_boulder b on(a.id == b.user_id) " +
//            "join boulder c on (b.boulder_id == c.id)")
//    List<User> prova();

    @Query("select u.* " +
            "from User u join tracciatura_boulder t on(t.user_id == u.id)" +
            "join boulder b on (b.id == t.boulder_id) " +
            "where b.id == :id " +
            "limit 1")
    LiveData<User> getTracciatoreFromBoulder(int id);

    @Query("select * " +
            "from boulder b join completed_boulder c on (b.id == c.boulder_id)" +
            " where boulder_id == :id")
    LiveData<List<CompletedBoulder>> getCompletionsOfBoulder(int id);

    @Query("select b.* from boulder b join completed_boulder c on (b.id == c.boulder_id)" +
            "join user u on (c.user_id == c.user_id)" +
            "where u.id == :user_id " +
            "and b.id == :boulder_id")
    LiveData<List<Boulder>> isBoulderCompletedByUser(int user_id,int boulder_id);

    @Query("select * from comment c join completed_boulder cb on (c.id == cb.comment_id) " +
            "where cb.boulder_id == :boulder_id")
    LiveData<List<Comment>> getCommentsOnBoulder(int boulder_id);
}
