package com.example.climbingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.database.entities.Comment;
import com.example.climbingapp.recyclerview.CommentsCardAdapter;
import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommentsBoulderViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private CommentsCardAdapter adapter;
    private ClimbingAppRepository repository;
    private Fragment fragment;
    private SelectedBoulderViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ClimbingAppRepository(getActivity().getApplication());
        fragment = this;
        model = new ViewModelProvider(getActivity()).get(SelectedBoulderViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments_boulder_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView(view);
    }

    private void setRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.comments_recyclerview);
        adapter = new CommentsCardAdapter(fragment);
        recyclerView.setAdapter(adapter);
        populateCommentList();

    }

    private void populateCommentList() {
        repository.getCommentsOnBoulder(model.getSelected().getValue().id).observe(this,comments -> {
            List<Comment> nonEmptyComments = new ArrayList<>();
            for(Comment c:comments){
                c.updateValues(getActivity().getApplication(),fragment,adapter,comments.indexOf(c));
                if(!c.text.equals("null")){
                    nonEmptyComments.add(c);
                }
            }
            adapter.setData(nonEmptyComments);
            adapter.notifyDataSetChanged();
        });
    }
}