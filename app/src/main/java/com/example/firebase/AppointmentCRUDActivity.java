package com.example.firebase;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppointmentCRUDActivity extends AppCompatActivity {
    private EditText editId, editClientId, editClientName, editServiceId, editServiceName, editDate, editTime;
    private Button btnSave, btnDelete;
    private Appointment Apntmnt;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_crudactivity);
        Appointment appointment = (Appointment) getIntent().getSerializableExtra("appointment");
        Apntmnt = appointment;

        editId = findViewById(R.id.editId);
        editClientId = findViewById(R.id.editClientId);
        editClientName = findViewById(R.id.editClientName);
        editServiceId = findViewById(R.id.editServiceId);
        editServiceName = findViewById(R.id.editServiceName);
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);


        editId.setText(appointment.getId());
        editClientId.setText(appointment.getClientId());
        editClientName.setText(appointment.getClientName());
        editServiceId.setText(appointment.getServiceId());
        editServiceName.setText(appointment.getServiceName());
        editDate.setText(appointment.getDate());
        editTime.setText(appointment.getTime());

        db = FirebaseFirestore.getInstance();

        btnSave.setOnClickListener(v -> saveOrUpdateAppointment());
        btnDelete.setOnClickListener(v -> deleteAppointment());
    }

    private void saveOrUpdateAppointment() {
        String id = editId.getText().toString();
        String clientId = editClientId.getText().toString();
        String clientName = editClientName.getText().toString();
        String serviceId = editServiceId.getText().toString();
        String serviceName = editServiceName.getText().toString();
        String dateStr = editDate.getText().toString();
        String timeStr = editTime.getText().toString();

        if (clientId.isEmpty() || clientName.isEmpty() || serviceId.isEmpty() || serviceName.isEmpty() ||
                dateStr.isEmpty() || timeStr.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> appointment = new HashMap<>();
        appointment.put("client_id", Integer.parseInt(clientId));
        appointment.put("client_name", clientName);
        appointment.put("service_id", Integer.parseInt(serviceId));
        appointment.put("service_name", serviceName);
        appointment.put("date", dateStr);
        appointment.put("time", timeStr);

        if (!id.isEmpty()) {
            db.collection("appointments").document(id)
                    .update(appointment)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Ошибка обновления", Toast.LENGTH_SHORT).show());

            Log log = new Log();

            log.setTag("update");
            log.setMessage("Админ обновил запись с ID: " + id);
            log.setDate(new Date());

            db.collection("logs").add(log);
        } else {
            db.collection("appointments")
                    .add(appointment)
                    .addOnSuccessListener(documentReference -> {
                        editId.setText(documentReference.getId());
                        Toast.makeText(this, "Создано", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Ошибка создания", Toast.LENGTH_SHORT).show());

            Log log = new Log();

            log.setTag("add");
            log.setMessage("Админ добавил запись");
            log.setDate(new Date());
            db.collection("logs").add(log);
        }
    }

    private void deleteAppointment() {
        String id = editId.getText().toString();
        if (id.isEmpty()) {
            Toast.makeText(this, "Введите ID", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("appointments").document(id).delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show());

        Log log = new Log();

        log.setTag("delete");
        log.setMessage("Админ удалил запись с ID: " + id);
        log.setDate(new Date());

        db.collection("logs").add(log);
    }
}