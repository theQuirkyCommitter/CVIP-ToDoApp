package com.droidamsel.doApp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidamsel.doApp.Adapter.doAdapter;
import com.droidamsel.doApp.Model.doModel;
import com.droidamsel.doApp.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private doAdapter taskAdapter;
    private List<doModel> taskList;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        RecyclerView taskRecyclerView = findViewById(R.id.recyclerTasks);
        FloatingActionButton fab = findViewById(R.id.savetask);
        db = new DatabaseHandler(MainActivity.this);

        taskList = db.getAllTask(); // Retrieve tasks from the database
        Collections.reverse(taskList); // Reverse the order of tasks
        taskAdapter  = new doAdapter(this,db);

        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        taskAdapter.setTask(taskList); // Set the task list for the adapter

        fab.setOnClickListener(view -> addNewTask.newInstance().show(getSupportFragmentManager(), addNewTask.tag));

        ItemTouchHelper.Callback callback = new itemSlide(taskAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTask(); // Update the task list from the database
        Collections.reverse(taskList); // Reverse the order of tasks
        taskAdapter.setTask(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}
