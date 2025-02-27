package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private String selectCollection;
    private AdminAdapter adapter;
    private List<Object> dataList = new ArrayList<>();
    private FirebaseFirestore db;
    private Button btnUsers, btnAppointments, btnServices, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdminAdapter(dataList, this, (position, item) -> {
            if (item instanceof User) {
                User user = (User) item;
                Toast.makeText(this, "Выбран пользователь: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, UserCRUDActivity.class);
                intent.putExtra("user", (Serializable) user); // Передаём объект
                startActivity(intent);
            } else if (item instanceof Appointment) {
                Appointment appointment = (Appointment) item;
                Toast.makeText(this, "Запись: " + appointment.getClientName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AppointmentCRUDActivity.class);
                intent.putExtra("appointment", (Serializable) appointment); // Передаём объект
                startActivity(intent);
            } else if (item instanceof Service) {
                Service service = (Service) item;
                Toast.makeText(this, "Сервис: " + service.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ServiceCRUDActivity.class);
                intent.putExtra("service", (Serializable) service);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        btnUsers = findViewById(R.id.btnUsers);
        btnAppointments = findViewById(R.id.btnAppointments);
        btnServices = findViewById(R.id.btnServices);
        btnAdd = findViewById(R.id.btnAdd);

        btnUsers.setOnClickListener(v -> loadData("users"));
        btnAppointments.setOnClickListener(v -> loadData("appointments"));
        btnServices.setOnClickListener(v -> loadData("services"));
        btnAdd.setOnClickListener(v->addItem(selectCollection));
    }

    private void addItem(String collection) {
        try {
            if (collection == "users") {
                User user = new User();
                Toast.makeText(this, "Выбран пользователь: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, UserCRUDActivity.class);
                intent.putExtra("user", (Serializable) user);
                startActivity(intent);
            } else if (collection == "appointments") {
                Appointment appointment = new Appointment();
                Toast.makeText(this, "Запись: " + appointment.getClientName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AppointmentCRUDActivity.class);
                intent.putExtra("appointment", (Serializable) appointment);
                startActivity(intent);
            } else if (collection == "services") {
                Service service = new Service();
                Toast.makeText(this, "Сервис: " + service.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ServiceCRUDActivity.class);
                intent.putExtra("service", (Serializable) service);
                startActivity(intent);
            }
        } catch (Exception e){
            Toast.makeText(this, "Коллекция не выбрана", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData(String collection) {
        selectCollection = collection;
        db.collection(collection).get().addOnSuccessListener(queryDocumentSnapshots -> {
            dataList.clear();
            for (var doc : queryDocumentSnapshots) {
                if (collection.equals("users")) {
                    User user = doc.toObject(User.class);
                    if (user != null) {
                        user.setId(doc.getId()); // 🔥 Добавляем ID вручную
                        dataList.add(user);
                    }
                } else if (collection.equals("appointments")) {
                    Appointment appointment = doc.toObject(Appointment.class);
                    if (appointment != null) {
                        appointment.setId(doc.getId()); // 🔥 Если у Appointment есть id, тоже добавляем
                        dataList.add(appointment);
                    }
                } else if (collection.equals("services")) {
                    Service service = doc.toObject(Service.class);
                    if (service != null) {
                        service.setId(doc.getId()); // 🔥 Аналогично для сервисов
                        dataList.add(service);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

}
