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

    @Query("SELECT * FROM USER")
    List<User> getUsers();

    @Query("SELECT * FROM COMMENT")
    LiveData<List<Comment>> getComments();

    @Query("SELECT * FROM completed_boulder")
    List<CompletedBoulder> getCompletedBoulders();

    @Query("SELECT * FROM boulder")
    LiveData<List<Boulder>> getBoulders();

    @Query("SELECT * FROM TRACCIATURA_BOULDER")
    List<TracciaturaBoulder> getTracciature();

//insert:
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertComment(Comment comment);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertCompletedBoulder(CompletedBoulder compBoulder);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertBoulder(Boulder boulder);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertTracciatura(TracciaturaBoulder tracciaturaBoulder);

    @Query("select * from boulder ")
    List<BoulderAndTracciatura> getBoulderAndTracciatura();

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
            "join user u on (c.user_id == u.id)" +
            "where u.id == :user_id " +
            "and b.id == :boulder_id")
    LiveData<List<Boulder>> isBoulderCompletedByUser(int user_id,int boulder_id);

    @Query("select c.*," +
            "(select u.username from user u where u.id = c.user_id) as username," +
            "cb.number_of_tries," +
            "cb.date " +
            "from comment c join completed_boulder cb on (cb.comment_id = c.id) " +
            "where c.text is not null " +
            "and cb.boulder_id = :boulder_id")
    LiveData<List<Comment.CommentUpdated>> getCommentsOnBoulder(int boulder_id);

    @Query("select * from comment c join completed_boulder cb on (c.id == cb.comment_id) " +
            "where c.id == :id")
    LiveData<CompletedBoulder> getCompletionOfBoulderFromComment(int id);

    @Query("select * from user where id == :id")
    LiveData<User> getUserFromId(int id);

    @Query("select * from user where username = :username")
    LiveData<List<User>> getUserFromUsername(String username);


    @Query("select b.*, u.username as user, " +
            "            (select " +
            "            CASE " +
            "            when EXISTS ( SELECT * FROM completed_boulder cb where cb.boulder_id = b.id and cb.user_id = :id) then 1  " +
            "            else 0 " +
            "            END) as checked ," +
            "            (select count(*) from completed_boulder where boulder_id = b.id) as completions," +
            "            (select CASE " +
            "            when (select avg(comm.rating)  from completed_boulder compb join comment comm on (compb.comment_id = comm.id) where compb.boulder_id = b.id) is not null then  " +
            "            (select avg(comm.rating)  from completed_boulder compb join comment comm on (compb.comment_id = comm.id) where compb.boulder_id = b.id) " +
            "            else 0 " +
            "            END) as rating " +
            "            from boulder b " +
            "            join tracciatura_boulder t on (t.boulder_id = b.id) " +
            "            JOIN user u on (t.user_id = u.id) " +
            "            where u.id in (select user_id from completed_boulder where boulder_id = b.id)")
    LiveData<List<Boulder.BoulderUpdated>> getBouldersCompletedByUser(int id);


    @Query("select b.*, u.username as user,\n" +
            "(select " +
            "CASE " +
            " when EXISTS ( SELECT * FROM completed_boulder cb where cb.boulder_id = b.id and cb.user_id = :currentUserId) then 1  " +
            " else 0 " +
            " END) as checked ," +
            "(select count(*) from completed_boulder where boulder_id = b.id) as completions, " +
            "(select CASE " +
            " when (select avg(comm.rating)  from completed_boulder compb join comment comm on (compb.comment_id = comm.id) where compb.boulder_id = b.id) is not null then  " +
            " (select avg(comm.rating)  from completed_boulder compb join comment comm on (compb.comment_id = comm.id) where compb.boulder_id = b.id) " +
            " else 0 " +
            "  END) as rating " +
            "from boulder b " +
            "join tracciatura_boulder t on (t.boulder_id = b.id) " +
            "JOIN user u on (t.user_id = u.id) ")
    LiveData<List<Boulder.BoulderUpdated>> getBoulderUpdated(int currentUserId);

    @Query("select b.*, u.username as tracciatore, " +
            "(select  " +
            " CASE " +
            " when EXISTS ( SELECT * FROM completed_boulder cb where cb.boulder_id = b.id and cb.user_id = :currentUserId) then 1  " +
            " else 0 " +
            " END) as checked , " +
            "(select count(*) from completed_boulder where boulder_id = b.id) as completions, " +
            "(select CASE " +
            "when (select avg(comm.rating)  from completed_boulder compb join comment comm on (compb.comment_id = comm.id) where compb.boulder_id = b.id) is not null then  " +
            " (select avg(comm.rating)  from completed_boulder compb join comment comm on (compb.comment_id = comm.id) where compb.boulder_id = b.id) " +
            " else 0 " +
            " END) as rating, " +
            "n.date as completion_date, n.number_of_tries,n.rating as rating_user,n.grade as grade_user ,n.text as comment_text " +
            "from   boulder b " +
            "join (select  cb2.boulder_id,cb2.date,cb2.number_of_tries,c2.rating,c2.grade,c2.text from completed_boulder cb2 join comment c2 on(cb2.comment_id = c2.id) where cb2.user_id = :currentUserId  ) n on(b.id = n.boulder_id) " +
            "join tracciatura_boulder t on (t.boulder_id = b.id) " +
            "JOIN user u on (t.user_id = u.id);")
    LiveData<List<Boulder.BoulderLogbook>>  getBouldersLogbook(int currentUserId);
    

}
