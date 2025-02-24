package com.example.firebase;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ServiceCRUDActivity extends AppCompatActivity {
    private EditText editId, editName;
    private Button btnSave, btnDelete;
    private Service Srvc;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_crudactivity);
        Service service = (Service) getIntent().getSerializableExtra("service");
        Srvc = service;

        editId = findViewById(R.id.editId);
        editName = findViewById(R.id.editName);
        editId.setText(Srvc.getId());
        editName.setText(Srvc.getName());
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        db = FirebaseFirestore.getInstance();

        btnSave.setOnClickListener(v -> saveOrUpdateService());
        btnDelete.setOnClickListener(v -> deleteService());
    }

    private void saveOrUpdateService() {
        String id = editId.getText().toString();
        String name = editName.getText().toString();

        Map<String, Object> service = new HashMap<>();
        service.put("name", name);

        if (!id.isEmpty()) {
            // Обновление существующей записи
            db.collection("services").document(id)
                    .update(service)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Ошибка обновления", Toast.LENGTH_SHORT).show());
        } else {
            // Создание новой записи с автоматической генерацией ID
            db.collection("services")
                    .add(service)
                    .addOnSuccessListener(documentReference -> {
                        editId.setText(documentReference.getId());
                        Toast.makeText(this, "Создано", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Ошибка создания", Toast.LENGTH_SHORT).show());
        }
    }

    private void deleteService() {
        String id = editId.getText().toString();
        if (id.isEmpty()) {
            Toast.makeText(this, "Введите ID", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("services").document(id).delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show());
    }
}