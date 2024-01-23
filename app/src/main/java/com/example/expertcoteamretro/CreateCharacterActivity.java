package com.example.expertcoteamretro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.expertcoteamretro.database.DatabaseHelper;
import android.widget.TextView;
import android.view.Gravity;
import android.widget.Toast;

public class CreateCharacterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcharacter);

        dbHelper = new DatabaseHelper(this);

        EditText editTextCharacterName = findViewById(R.id.editTextCharacterName);
        Button buttonNewCharacter = findViewById(R.id.buttonNewCharacter);

        long userId = getIntent().getLongExtra("userId", -1);

        buttonNewCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String characterName = editTextCharacterName.getText().toString();
                if (characterName.isEmpty()) {
                    Toast.makeText(CreateCharacterActivity.this, "Veuillez remplir le nom du personnage", Toast.LENGTH_SHORT).show();
                    return;
                }

                long characterId = dbHelper.createNewCharacter(userId, characterName);
                if (characterId != -1) {
                    Toast.makeText(CreateCharacterActivity.this, "Création du personnage réussie", Toast.LENGTH_SHORT).show();

                    // Rediriger vers l'activité de connexion après l'inscription réussie
                    Intent intent = new Intent(CreateCharacterActivity.this, MyCharactersActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateCharacterActivity.this, "Erreur lors de la création du personnage", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
