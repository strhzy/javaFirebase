package com.example.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    private List<Object> dataList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, Object item);
    }

    public AdminAdapter(List<Object> dataList, Context context, OnItemClickListener onItemClickListener) {
        this.dataList = dataList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = dataList.get(position);
        if (item instanceof User) {
            User user = (User) item;
            holder.textView.setText("User: " + user.getEmail() + " - Role: " + user.getRole());
        } else if (item instanceof Appointment) {
            Appointment appointment = (Appointment) item;
            holder.textView.setText("Appointment: " + appointment.getClientName() + " - " + appointment.getServiceName());
        } else if (item instanceof Service) {
            Service service = (Service) item;
            holder.textView.setText("Service: " + service.getName());
        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
