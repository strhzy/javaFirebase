package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmpActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminAdapter adapter;
    private List<Object> dataList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdminAdapter(dataList, this, (position, item) -> {
            Service service = (Service) item;
            Toast.makeText(this, "Сервис: " + service.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ServiceCRUDActivity.class);
            intent.putExtra("service", (Serializable) service);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        loadData();
    }

    private void addItem(String collection) {
        try {
            Service service = new Service();
            Toast.makeText(this, "Сервис: " + service.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ServiceCRUDActivity.class);
            intent.putExtra("service", (Serializable) service);
            startActivity(intent);
        } catch (Exception e){
            Toast.makeText(this, "Коллекция не выбрана", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        db.collection("services").get().addOnSuccessListener(queryDocumentSnapshots -> {
            dataList.clear();
            for (var doc : queryDocumentSnapshots) {
                Service service = doc.toObject(Service.class);
                if (service != null) {
                    service.setId(doc.getId());
                    dataList.add(service);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}