package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button loginButton, registerButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        auth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v -> loginUser());
        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Ошибка: поля должны быть заполнены", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Ошибка: пароль короче 6 символов", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ошибка: почта введена некорректно", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                saveUserToFirestore(email, password);
            } else {
                if (task.getException() != null) {
                    String errormessage = task.getException().getMessage();
                }
                Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
                Log.e("AuthError", "Ошибка регистрации", task.getException());
            }
        });
    }

    private void loginUser() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        try{
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    checkUserRole();
                } else {
                    Toast.makeText(this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                }
            });
        } catch(Exception e) {
            Toast.makeText(this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserRole() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(firebaseUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String role = documentSnapshot.getString("role");
                        if (role != null) {
                            User user = new User(
                                    firebaseUser.getUid(),
                                    firebaseUser.getEmail(),
                                    role
                            );

                            Intent intent;
                            switch (role) {
                                case "admin":
                                    intent = new Intent(AuthActivity.this, AdminActivity.class).putExtra("user", user);
                                    break;
                                case "employee":
                                    intent = new Intent(AuthActivity.this, EmpActivity.class).putExtra("user", user);
                                    break;
                                case "user":
                                    intent = new Intent(AuthActivity.this, UserActivity.class).putExtra("user", user);
                                    break;
                                default:
                                    return;
                            }
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AuthActivity.this, "Ошибка при проверке роли пользователя", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserToFirestore(String email, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password);
        user.put("role", "employee");
        db.collection("users").document(auth.getCurrentUser().getUid()).set(user).addOnSuccessListener(v -> {
            Toast.makeText(AuthActivity.this, "Данные пользователя успешно сохранены", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AuthActivity.this, MainActivity.class));
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(AuthActivity.this, "Данные пользователя не сохранены" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}