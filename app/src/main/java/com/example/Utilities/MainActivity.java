package com.example.Utilities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View v = findViewById(R.id.button_calculator);
        v.setOnClickListener(this);
        View v2 = findViewById(R.id.button_notes);
        v2.setOnClickListener(this);
    }


    @Override
    public void onClick(View arg) {
        if (arg.getId() == R.id.button_calculator) {
            Intent intent = new Intent(this, Calculator.class);
            this.startActivity(intent);
        }

        if (arg.getId() == R.id.button_notes) {
            Intent intent = new Intent(this, NoteSelect.class);
            this.startActivity(intent);
        }
    }

}
