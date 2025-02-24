package com.example.firebase;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    private ServiceAdapter serviceAdapter;
    private AppointmentAdapter appointmentAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Service> serviceList = new ArrayList<>();
    private List<Appointment> appointmentList = new ArrayList<>();
    private Button appoint;
    private RecyclerView services, appointments;
    private DatePicker datePicker;
    private TimePicker timePicker;
    Date date = new Date();
    private Service service;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        user = (User) getIntent().getSerializableExtra("user");

        appoint = findViewById(R.id.appoint);
        services = findViewById(R.id.serviceView);
        appointments = findViewById(R.id.appointmentsView);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(Boolean.TRUE);

        serviceAdapter = new ServiceAdapter(serviceList, this, (position, item)->{
            service = (Service) item;
            Toast.makeText(this, "Выбрана услуга: " + service.getName(), Toast.LENGTH_SHORT).show();
        });

        appointmentAdapter = new AppointmentAdapter(appointmentList);

        appoint.setOnClickListener(v -> addAppointment());

        loadData();

    }

    private void loadData(){
        db.collection("appointments").whereEqualTo("client_id",user.getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            appointmentList.clear();
            for (var doc : queryDocumentSnapshots) {
                Appointment appointment = doc.toObject(Appointment.class);
                if (appointment != null) {
                    appointment.setId(doc.getId());
                    appointmentList.add(appointment);
                }
            }
            appointmentAdapter.notifyDataSetChanged();
        });
        db.collection("services").get().addOnSuccessListener(queryDocumentSnapshots -> {
           serviceList.clear();
            for (var doc : queryDocumentSnapshots) {
                Service service1 = doc.toObject(Service.class);
                if (service1 != null) {
                    service1.setId(doc.getId());
                    serviceList.add(service1);
                }
            }
            serviceAdapter.notifyDataSetChanged();
        });
    }

    private void addAppointment(){
        if(service == null){
            Toast.makeText(this, "Выберите услугу!", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> appointmentMap = new HashMap<>();
            Appointment appointment = new Appointment();
            appointment.setClientId(user.getId());
            appointment.setClientName(user.getEmail());
            appointment.setServiceId(service.getId());
            appointment.setServiceName(service.getName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(datePicker);
            appointment.setDate(formattedDate);
            appointmentMap.put("client_id", appointment.getClientId());
            appointmentMap.put("client_name", appointment.getClientName());
            appointmentMap.put("service_id", appointment.getServiceId());
            appointmentMap.put("service_name", appointment.getServiceName());
            appointmentMap.put("date", appointment.getDate());

            db.collection("appointments")
            .add(appointmentMap)
            .addOnSuccessListener(documentReference -> {
                Toast.makeText(this, "Запись успешно добавлена", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Ошибка добавления записи", Toast.LENGTH_SHORT).show();
            });
        }
    }
}