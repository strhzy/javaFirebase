package com.example.firebase;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserCRUDActivity extends AppCompatActivity {
    private EditText editId, editEmail, editRole;
    private Button btnSave, btnDelete;
    private FirebaseFirestore db;

    private User Usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_crudactivity);
        User user = (User) getIntent().getSerializableExtra("user");
        Usr = user;
        editEmail = findViewById(R.id.editEmail);
        editRole = findViewById(R.id.editRole);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        editEmail.setText(user.getEmail());
        editRole.setText(user.getRole());

        db = FirebaseFirestore.getInstance();

        btnSave.setOnClickListener(v -> saveUser());
        btnDelete.setOnClickListener(v -> deleteUser());
    }

    private void saveUser() {
        String email = editEmail.getText().toString();
        String role = editRole.getText().toString();

        if (email.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(Usr.getId())
                .update("email", email, "role", role)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Ошибка обновления", Toast.LENGTH_SHORT).show());
    }

    private void deleteUser() {
        String id = editId.getText().toString();
        if (id.isEmpty()) {
            Toast.makeText(this, "Введите ID", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("users").document(id).delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show());
    }
}
