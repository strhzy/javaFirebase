package com.example.firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointmentList;

    public AppointmentAdapter(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        // Форматирование даты и времени
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String dateStr = dateFormat.format(appointment.getDate());
        String timeStr = timeFormat.format(appointment.getTime());

        holder.textAppointmentId.setText("ID: " + appointment.getId());
        holder.textClientName.setText("Client: " + appointment.getClientName());
        holder.textServiceName.setText("Service: " + appointment.getServiceName());
        holder.textDateTime.setText("Date: " + dateStr + ", Time: " + timeStr);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView textAppointmentId, textClientName, textServiceName, textDateTime;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textAppointmentId = itemView.findViewById(R.id.textAppointmentId);
            textClientName = itemView.findViewById(R.id.textClientName);
            textServiceName = itemView.findViewById(R.id.textServiceName);
            textDateTime = itemView.findViewById(R.id.textDateTime);
        }
    }
}