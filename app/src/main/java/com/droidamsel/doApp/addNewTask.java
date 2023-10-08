package com.droidamsel.doApp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.droidamsel.doApp.Model.doModel;
import com.droidamsel.doApp.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class addNewTask extends BottomSheetDialogFragment {
    public static final String tag ="ActionBottomDialog";

    private EditText newTaskText;
    private Button newTaskSaveButton;

    private DatabaseHandler db;

    public static addNewTask newInstance(){
        return new addNewTask();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newTaskText = requireView().findViewById(R.id.newTasktext);
        newTaskSaveButton = requireView().findViewById(R.id.taskSaveButton);

        db = new DatabaseHandler(getActivity());

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            assert task != null;
            if (task.length() > 0 ){
                newTaskSaveButton.setEnabled(false);
            }

            newTaskText.setTextColor(ContextCompat.getColor(requireContext(), R.color.base_color));
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().isEmpty()){ // Check if the text is empty or contains only spaces
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newTaskSaveButton.setEnabled(true); // Enable the button when there is valid text
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.base_color));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(v -> {
            String text = newTaskText.getText().toString();
            if (!text.trim().isEmpty()) {
            if (finalIsUpdate) {
                db.updateTask(bundle.getInt("id"), text);
            } else {
                doModel task = new doModel();
                task.setTask(text);
                task.setStatus(0);

                // Insert the new task into the database
                db.insertTask(task);
            }
            dismiss();
            } else {
                Toast.makeText(getActivity(), "Task cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
