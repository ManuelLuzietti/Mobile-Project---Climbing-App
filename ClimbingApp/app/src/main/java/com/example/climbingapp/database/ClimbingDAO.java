package com.example.climbingapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
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

import java.util.Date;
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
            "join user u on (c.user_id == u.id)" +
            "where u.id == :user_id " +
            "and b.id == :boulder_id")
    LiveData<List<Boulder>> isBoulderCompletedByUser(int user_id,int boulder_id);

    @Query("select * from comment c join completed_boulder cb on (c.id == cb.comment_id) " +
            "where cb.boulder_id == :boulder_id")
    LiveData<List<Comment>> getCommentsOnBoulder(int boulder_id);

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
            "            (select count(*) from completed_boulder where boulder_id = b.id) as repeats," +
            "            (select CASE " +
            "            when (select avg(comm.rating)  from completed_boulder compb join comment comm on (compb.comment_id = comm.id) where compb.boulder_id = b.id) is not null then  " +
            "            (select avg(comm.rating)  from completed_boulder compb join comment comm on (compb.comment_id = comm.id) where compb.boulder_id = b.id) " +
            "            else 0 " +
            "            END) as rating " +
            "            from boulder b " +
            "            join tracciatura_boulder t on (t.boulder_id = b.id) " +
            "            JOIN user u on (t.user_id = u.id) " +
            "            where u.id in (select user_id from completed_boulder where boulder_id = b.id)")
    LiveData<List<BoulderUpdated>> getBouldersCompletedByUser(int id);


    @Query("select b.*, u.username as user,\n" +
            "(select " +
            "CASE " +
            " when EXISTS ( SELECT * FROM completed_boulder cb where cb.boulder_id = b.id and cb.user_id = :currentUserId) then 1  " +
            " else 0 " +
            " END) as checked ," +
            "(select count(*) from completed_boulder where boulder_id = b.id) as repeats, " +
            "(select CASE " +
            " when (select avg(comm.rating)  from completed_boulder compb join comment comm on (compb.comment_id = comm.id) where compb.boulder_id = b.id) is not null then  " +
            " (select avg(comm.rating)  from completed_boulder compb join comment comm on (compb.comment_id = comm.id) where compb.boulder_id = b.id) " +
            " else 0 " +
            "  END) as rating " +
            "from boulder b " +
            "join tracciatura_boulder t on (t.boulder_id = b.id) " +
            "JOIN user u on (t.user_id = u.id) ")
    LiveData<List<BoulderUpdated>> getBoulderUpdated(int currentUserId);

    public static class BoulderUpdated{
        public int id;
        public String name;
        public String grade;
        @androidx.room.TypeConverters(com.example.climbingapp.database.TypeConverters.class)
        public Date date;
        @ColumnInfo(name = "is_official")
        public boolean isOfficial;
        public String img;

        public String user;
        public int rating;
        public int repeats;
        public boolean checked;
        public int getId() {
            return id;
        }

        public String getPlaceName() {
            return name;
        }

        public String getPlaceUser() {
            return user;
        }

        public int getPlaceRepeats() {
            return repeats;
        }

        public String getPlaceGrade() {
            return grade;
        }

        public int getPlaceRating() {
            return rating;
        }

        public String getImg() {
            return img;
        }


        public boolean isChecked() {
            return checked;
        }

        public boolean isOfficial() {
            return isOfficial;
        }


        public void setPlaceName(String name) {
            this.name = name;
        }

        public void setPlaceUser(String user) {
            this.user = user;
        }

        public void setPlaceRepeats(int repeats) {
            this.repeats = repeats;
        }

        public void setPlaceGrade(String grade) {
            this.grade = grade;
        }

        public void setPlaceRating(int raa
        ) {
            this.rating = rating;
        }

        public void setImg( String img) {
            this.img = img;
        }

        public void setOfficial(boolean official) {
            isOfficial = official;
        }

        public void setChecked(boolean checked) {
            checked = checked;
        }


    }
}
