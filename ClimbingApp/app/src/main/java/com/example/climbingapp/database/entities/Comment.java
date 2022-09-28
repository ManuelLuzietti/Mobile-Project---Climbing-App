package com.example.climbingapp.database.entities;

import android.app.Application;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.climbingapp.ClimbingAppRepository;
import com.example.climbingapp.database.TypeConverters;
import com.example.climbingapp.recyclerview.CommentsCardViewHolder;

@Entity(tableName = "comment",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Comment {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @Ignore
    public Comment(int id, String text, int rating, String grade, int userId) {
        this.id = id;
        this.text = text;
        this.rating = rating;
        this.grade = grade;
        this.userId = userId;
    }
    public Comment( String text, int rating, String grade, int userId) {
        this.text = text;
        this.rating = rating;
        this.grade = grade;
        this.userId = userId;
    }

    public String text;

    public int rating;

    public String grade;

    @Ignore
    private String username;

    @Ignore
    private int numOfTries;
    @Ignore
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumOfTries() {
        return numOfTries;
    }

    public void setNumOfTries(int numOfTries) {
        this.numOfTries = numOfTries;
    }


    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public String getGrade() {
        return grade;
    }

    public int getUserId() {
        return userId;
    }

    @ColumnInfo(name = "user_id")
    public int userId;


    public void updateValues(Application application, Fragment fragment, RecyclerView.Adapter<CommentsCardViewHolder> adapter, int pos) {
        ClimbingAppRepository repository = new ClimbingAppRepository(application);
        repository.getCompletionOfBoulderFromComment(id).observe(fragment,completion ->{
            setNumOfTries(completion.numberOfTries);
            setDate(TypeConverters.toString(completion.date));
            adapter.notifyItemChanged(pos);
        });
        repository.getUserFromId(userId).observe(fragment,user -> {
            setUsername(user.username);
            adapter.notifyItemChanged(pos);
        });
    }
}
