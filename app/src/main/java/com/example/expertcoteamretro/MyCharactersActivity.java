package com.example.expertcoteamretro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.expertcoteamretro.database.DatabaseHelper;
import android.widget.TextView;
import android.view.Gravity;

public class MyCharactersActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycharacters);

        dbHelper = new DatabaseHelper(this);

        Button buttonCreateCharacter = findViewById(R.id.buttonCreateCharacter);

        long userId = getIntent().getLongExtra("userId", -1);
        long connectedUserNumberCharacter = dbHelper.getConnectedUserNumberCharacter(userId);

        LinearLayout linearLayout = findViewById(R.id.linearLayout); // Remplacez R.id.linearLayout par l'ID de votre layout parent

        if (connectedUserNumberCharacter > 0) {
            String[] connectedUserCharacters = dbHelper.getConnectedUserCharacters(userId);

            // Ajouter des boutons aux noms des personnages de l'utilisateur
            for (String characterName : connectedUserCharacters) {
                Button button = new Button(this);
                button.setText(characterName);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Logique à exécuter lorsqu'un bouton est cliqué
                        // Vous pouvez par exemple démarrer une nouvelle activité avec les détails du personnage
                    }
                });

                // Ajouter le bouton au layout parent (linearLayout)
                linearLayout.addView(button);
            }
        } else {
            // Créer un TextView indiquant que l'utilisateur n'a aucun personnage
            TextView textView = new TextView(this);
            textView.setText("Vous n'avez encore aucun personnage");

            // Définir les paramètres de mise en page pour centrer le texte
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            layoutParams.gravity = Gravity.CENTER;

            // Appliquer les paramètres de mise en page au TextView
            textView.setLayoutParams(layoutParams);

            // Ajouter le TextView au layout parent
            linearLayout.addView(textView);
        }


        buttonCreateCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCharactersActivity.this, CreateCharacterActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });
    }
}
