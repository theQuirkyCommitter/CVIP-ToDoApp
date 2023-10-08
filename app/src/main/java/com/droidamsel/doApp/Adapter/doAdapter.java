package com.droidamsel.doApp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidamsel.doApp.MainActivity;
import com.droidamsel.doApp.Model.doModel;
import com.droidamsel.doApp.R;
import com.droidamsel.doApp.Utils.DatabaseHandler;
import com.droidamsel.doApp.addNewTask;

import java.util.List;

public class doAdapter extends RecyclerView.Adapter<doAdapter.ViewHolder> {
    private List<doModel> doList;
    private final MainActivity activity;
    private final DatabaseHandler db;

    public doAdapter(MainActivity activity, DatabaseHandler db) {
        this.activity = activity;
        this.db = db;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final doModel item = doList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
               db.updateStatus(item.getId() , 1);
            }else
               db.updateStatus(item.getId() , 0);
        });
    }
    public boolean toBoolean(int num){
        return num!=0;
    }
    public Context getContext() {
        return activity;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setTask(List<doModel> doList) {
        this.doList = doList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        if (doList != null && position >= 0 && position < doList.size()) {
            doModel item = doList.get(position);
            if (item != null && db != null) {
                    db.deleteTask(item.getId());
                    doList.remove(position);
                    notifyItemRemoved(position);
                }
        }
    }
    public void editItem(int position) {
        if (doList != null && position >= 0 && position < doList.size()) {
            doModel item = doList.get(position);

            if (item != null && activity != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", item.getId());
                bundle.putString("task", item.getTask());

                addNewTask task = new addNewTask();
                task.setArguments(bundle);
                task.show(activity.getSupportFragmentManager(), task.getTag());
            }
        }
    }

    @Override
    public int getItemCount() {
        return doList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.docheck);
        }
    }

}

