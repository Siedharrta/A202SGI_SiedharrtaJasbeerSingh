package com.example.notepad;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private Context mContext;
    private List<Task> mTaskList;

    public TasksAdapter(Context context, List<Task> taskList) {
        this.mContext = context;
        this.mTaskList = taskList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Task task = mTaskList.get(position);
        holder.mTextViewTask.setText(task.getTask());
        holder.mTextViewDesc.setText(task.getDesc());
        holder.mTextViewFinishBy.setText(task.getFinishBy());

        if (task.isFinished())
            holder.mTextViewStatus.setText("Completed");
        else
            holder.mTextViewStatus.setText("Not Completed");
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextViewStatus, mTextViewTask, mTextViewDesc, mTextViewFinishBy;

        public TasksViewHolder(View itemView) {
            super(itemView);

            mTextViewStatus = itemView.findViewById(R.id.mTextViewStatus);
            mTextViewTask = itemView.findViewById(R.id.mTextViewTask);
            mTextViewDesc = itemView.findViewById(R.id.mTextViewDesc);
            mTextViewFinishBy = itemView.findViewById(R.id.mTextViewFinishBy);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Task task = mTaskList.get(getAdapterPosition());

            Intent intent = new Intent(mContext, UpdateTaskActivity.class);
            intent.putExtra("task", task);

            mContext.startActivity(intent);
        }
    }
}