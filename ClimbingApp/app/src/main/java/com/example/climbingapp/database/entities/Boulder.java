package com.example.climbingapp.database.entities;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.climbingapp.ClimbingAppRepository;
import com.example.climbingapp.recyclerview.BoulderCardViewHolder;

import java.util.Date;
import java.util.List;

@Entity(tableName = "boulder", indices = {@Index(value = {"name"}, unique = true), @Index(value = {"img"}, unique = true)})
public class Boulder {
    public Boulder(int id, String name, @NonNull String grade, @NonNull Date date, boolean isOfficial, @NonNull String img) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.date = date;
        this.isOfficial = isOfficial;
        this.img = img;
    }

    @PrimaryKey
    public int id;
    public String name;
    @NonNull
    public String grade;
    @NonNull
    @TypeConverters(com.example.climbingapp.database.TypeConverters.class)
    public Date date;
    @ColumnInfo(name = "is_official")
    public boolean isOfficial;
    @NonNull
    public String img;

    @Ignore
    public int rating;
    @Ignore
    public int repeats;
    @Ignore
    public boolean checked;
    @Ignore
    public String user;

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

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public void setChecked(boolean checked) {
        checked = checked;
    }

    public void updateValues(Application application, Fragment fragment, RecyclerView.Adapter<BoulderCardViewHolder> adapter, int pos) {
        ClimbingAppRepository repository = new ClimbingAppRepository(application);
        repository.getTracciatoreFromBoulder(id).observe(fragment, (User u) -> {
            if (u != null) {
                setPlaceUser(u.username);
                adapter.notifyItemChanged(pos);
            }
        });
        repository.getCompletionsOfBoulder(id).observe(fragment, (List<CompletedBoulder> l) -> {
            setPlaceRepeats(l.size());
            adapter.notifyItemChanged(pos);
        });
        int id_user = fragment.getActivity().getPreferences(Context.MODE_PRIVATE).getInt("id", -1);
        if (id_user != -1) {
            repository.getBoulderIfCompletedByUser(id_user, id).observe(fragment,(List<Boulder> b)->{
                if (b.size()!=0){
                    checked = true;
                } else{
                    checked = false;
                }
                adapter.notifyItemChanged(pos);
            });
        } else {
            checked = false;
        }
        repository.getCommentsOnBoulder( id).observe(fragment,(List<Comment> lc) ->{
            double media = 0;
            for (Comment c: lc){
                media += c.rating;
            }
            media = media / lc.size();
            rating = (int)media;
            adapter.notifyItemChanged(pos);
        });


    }
}
