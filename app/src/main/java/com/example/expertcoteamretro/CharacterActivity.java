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

public class CharacterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        dbHelper = new DatabaseHelper(this);

        TextView textViewTestCharacter = findViewById(R.id.textViewTestCharacter);

        String characterName = getIntent().getStringExtra("character_name");
        String characterId = getIntent().getStringExtra("character_id");
        String StringConcat = characterName + " id : " + characterId;

        String formattedText = getString(R.string.Hello_label, StringConcat);
        textViewTestCharacter.setText(formattedText);

    }
}
