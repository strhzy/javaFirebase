package com.example.firebase;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private Service selectedService;
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
            selectedService = (Service) item;
            Toast.makeText(this, "Выбрана услуга: " + selectedService.getName(), Toast.LENGTH_SHORT).show();
        });

        appointmentAdapter = new AppointmentAdapter(appointmentList);

        services.setAdapter(serviceAdapter);
        appointments.setAdapter(appointmentAdapter);

        services.setLayoutManager(new LinearLayoutManager(this));
        appointments.setLayoutManager(new LinearLayoutManager(this));

        appoint.setOnClickListener(v -> addAppointment());

        loadData();

    }

    private void loadData(){
        db.collection("appointments").whereEqualTo("client_id",user.getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            appointmentList.clear();
            for (var doc : queryDocumentSnapshots) {
                Appointment appointment = new Appointment();
                appointment.setId(doc.getId());
                appointment.setClientId(doc.getString("client_id"));
                appointment.setClientName(doc.getString("client_name"));
                appointment.setServiceId(doc.getString("service_id"));
                appointment.setServiceName(doc.getString("service_name"));
                appointment.setDate(doc.getString("date"));
                appointment.setTime(doc.getString("time"));

                if (appointment.getClientName() != null && appointment.getServiceName() != null) {
                    appointmentList.add(appointment);
                } else {
                    Log.d("AppointmentLoad", "Missing client or service data for appointment " + doc.getId());
                }
            }
            appointmentAdapter.notifyDataSetChanged();
        });
        db.collection("services").get().addOnSuccessListener(queryDocumentSnapshots -> {
            serviceList.clear();
            for (var doc : queryDocumentSnapshots) {
                Service service = doc.toObject(Service.class);
                if (service != null) {
                    service.setId(doc.getId());
                    serviceList.add(service);
                }
            }
            serviceAdapter.notifyDataSetChanged();
        });
    }

    private void addAppointment(){
        if(selectedService == null){
            Toast.makeText(this, "Выберите услугу!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Map<String, Object> appointmentMap = new HashMap<>();
                Appointment appointment = new Appointment();
                appointment.setClientId(user.getId());
                appointment.setClientName(user.getEmail());
                appointment.setServiceId(selectedService.getId());
                appointment.setServiceName(selectedService.getName());
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                Calendar timeCalendar = Calendar.getInstance();
                timeCalendar.set(Calendar.HOUR_OF_DAY, hour);
                timeCalendar.set(Calendar.MINUTE, minute);
                timeCalendar.set(Calendar.SECOND, 0);
                timeCalendar.set(Calendar.MILLISECOND, 0);
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String formattedTime = timeFormat.format(timeCalendar.getTime());
                appointment.setTime(formattedTime);
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Calendar dateCalendar = Calendar.getInstance();
                dateCalendar.set(year, month, day);
                dateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                dateCalendar.set(Calendar.MINUTE, 0);
                dateCalendar.set(Calendar.SECOND, 0);
                dateCalendar.set(Calendar.MILLISECOND, 0);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = dateFormat.format(dateCalendar.getTime());
                appointment.setDate(formattedDate);
                appointmentMap.put("client_id", appointment.getClientId());
                appointmentMap.put("client_name", appointment.getClientName());
                appointmentMap.put("service_id", appointment.getServiceId());
                appointmentMap.put("service_name", appointment.getServiceName());
                appointmentMap.put("date", appointment.getDate());
                appointmentMap.put("time", appointment.getTime());

                db.collection("appointments")
                        .add(appointmentMap)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Запись успешно добавлена", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Ошибка добавления записи", Toast.LENGTH_SHORT).show();
                        });
                loadData();
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка добавления записи ", Toast.LENGTH_SHORT).show();
                Log.d("error",e.toString());
            }
        }
    }
}