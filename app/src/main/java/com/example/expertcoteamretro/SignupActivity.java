package com.example.expertcoteamretro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.expertcoteamretro.database.DatabaseHelper;

public class SignupActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DatabaseHelper(this);

        // Récupérer les vues
        EditText editTextNewUsername = findViewById(R.id.editTextNewUsername);
        EditText editTextNewPassword = findViewById(R.id.editTextNewPassword);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        // Ajouter un écouteur de clic au bouton d'inscription
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer le nouveau nom d'utilisateur et le nouveau mot de passe
                String newUsername = editTextNewUsername.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();

                // Vérifier si les champs sont vides
                if (newUsername.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Créer un nouvel utilisateur dans la base de données
                long userId = dbHelper.createUser(newUsername, newPassword);

                // Vérifier si l'inscription a réussi
                if (userId != -1) {
                    Toast.makeText(SignupActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();

                    // Rediriger vers l'activité de connexion après l'inscription réussie
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Terminer l'activité d'inscription pour empêcher le retour en arrière
                } else {
                    Toast.makeText(SignupActivity.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
