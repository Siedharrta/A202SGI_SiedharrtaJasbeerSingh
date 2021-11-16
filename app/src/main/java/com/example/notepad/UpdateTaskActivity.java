package com.example.notepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateTaskActivity extends AppCompatActivity {

    private EditText mEditTextTask, mEditTextDesc, mEditTextFinishBy;
    private CheckBox mCheckBoxFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        mEditTextTask = findViewById(R.id.editTextTask);
        mEditTextDesc = findViewById(R.id.editTextDesc);
        mEditTextFinishBy = findViewById(R.id.editTextFinishBy);
        mCheckBoxFinished = findViewById(R.id.checkBoxFinished);

        final Task task = (Task) getIntent().getSerializableExtra("task");

        loadTask(task);

        findViewById(R.id.mBtnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask(task);
            }
        });

        findViewById(R.id.mBtnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTaskActivity.this);
                builder.setTitle("Confirm Delete Task?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask(task);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void loadTask(Task task) {
        mEditTextTask.setText(task.getTask());
        mEditTextDesc.setText(task.getDesc());
        mEditTextFinishBy.setText(task.getFinishBy());
        mCheckBoxFinished.setChecked(task.isFinished());
    }

    private void updateTask(final Task task) {
        final String mTask = mEditTextTask.getText().toString().trim();
        final String mDesc = mEditTextDesc.getText().toString().trim();
        final String mFinishBy = mEditTextFinishBy.getText().toString().trim();

        if (mTask.isEmpty()) {
            mEditTextTask.setError("Please Enter Task Name");
            mEditTextTask.requestFocus();
            return;
        }

        if (mDesc.isEmpty()) {
            mEditTextDesc.setError("Please Enter Description");
            mEditTextDesc.requestFocus();
            return;
        }

        if (mFinishBy.isEmpty()) {
            mEditTextFinishBy.setError("Please Enter Finish Day");
            mEditTextFinishBy.requestFocus();
            return;
        }

        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                task.setTask(mTask);
                task.setDesc(mDesc);
                task.setFinishBy(mFinishBy);
                task.setFinished(mCheckBoxFinished.isChecked());
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Task Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        UpdateTask updateTask = new UpdateTask();
        updateTask.execute();
    }


    private void deleteTask(final Task task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Task Deleted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        DeleteTask deleteTask = new DeleteTask();
        deleteTask.execute();
    }
}
