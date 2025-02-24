package com.example.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Service> serviceList;
    private Context context;
    private ServiceAdapter.OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position, Object item);
    }

    public ServiceAdapter(List<Service> serviceList, Context context, ServiceAdapter.OnItemClickListener onItemClickListener) {
        this.serviceList = serviceList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.textServiceName.setText(service.getName());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, service);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView textServiceId, textServiceName;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            textServiceId = itemView.findViewById(R.id.textServiceId);
            textServiceName = itemView.findViewById(R.id.textServiceName);
        }
    }
}