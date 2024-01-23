package com.example.expertcoteamretro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.expertcoteamretro.database.DatabaseHelper;

public class MainPageActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        dbHelper = new DatabaseHelper(this);

        long userId = getIntent().getLongExtra("userId", -1);

        String connectedUsername = dbHelper.getConnectedUsername(userId);

        // Récupérer les vues
        TextView textviewHello = findViewById(R.id.textViewHello);
        Button buttonMyCharacters = findViewById(R.id.buttonMyCharacters);


        // Récupérer le userId de l'intent

        String formattedText = getString(R.string.Hello_label, connectedUsername);
        textviewHello.setText(formattedText);

        buttonMyCharacters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, MyCharactersActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });
    }
}
