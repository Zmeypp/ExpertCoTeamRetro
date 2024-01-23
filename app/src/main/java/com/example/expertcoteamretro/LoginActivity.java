// LoginActivity.java
package com.example.expertcoteamretro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.expertcoteamretro.database.DatabaseHelper;



public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        // Récupérer les vues
        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);


        // Récupérer le bouton "Se connecter"
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer le nouveau nom d'utilisateur et le nouveau mot de passe
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }
                long userId = dbHelper.checkUserLogin(username, password);

                // Vérifier si la connexion a réussi
                if (userId != -1) {
                    Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();

                    // Rediriger vers l'activité de connexion après l'inscription réussie
                    Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Erreur lors de la connexion", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Récupérer le bouton "Créer un compte"

        // Ajouter un écouteur de clic au bouton "Créer un compte"
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lorsque le bouton est cliqué, démarrer SignUpActivity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
